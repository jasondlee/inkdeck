package com.steeplesoft.inkdeck.client

import com.steeplesoft.inkdeck.shared.encoding.InkDeckMessageDecoder
import com.steeplesoft.inkdeck.shared.encoding.InkDeckMessageEncoder
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.Promise
import java.io.IOException
import java.nio.BufferOverflowException
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong


class InkDeckClient(host: String, port: Int) {
    private val clientHandler = InkDeckClientHandler()
    private var channelFuture: ChannelFuture
    private val group: EventLoopGroup = NioEventLoopGroup()

    init {
        val clientBootstrap = Bootstrap()
        clientBootstrap.group(group)
        clientBootstrap.channel(NioSocketChannel::class.java)
        clientBootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(socketChannel: SocketChannel) {
                socketChannel.pipeline().addLast(
                    InkDeckMessageDecoder(),
                    InkDeckMessageEncoder(),
                    StringDecoder(),
                    clientHandler
                )
            }
        })
        channelFuture = clientBootstrap.connect(host, port).sync()

    }

    fun sendMessage(msg: InkDeckMessage): Future<InkDeckMessage> {
        return clientHandler.sendMessage(msg)
    }

    fun disconnect() {
        clientHandler.disconnect()
        channelFuture.channel().closeFuture().addListener {
            group.shutdownGracefully().sync()
        }
    }

    private class InkDeckClientHandler : SimpleChannelInboundHandler<InkDeckMessage>() {
        private var sequence = AtomicInteger()
        private var channelContext: ChannelHandlerContext? = null
        private val queue = HashMap<Int, Promise<InkDeckMessage>>()
//            ArrayBlockingQueue<Promise<InkDeckMessage>>(16)
        private var clientClosed = false

        fun sendMessage(message: InkDeckMessage): Future<InkDeckMessage> {
            channelContext?.let {
                return sendMessage(message, channelContext!!.executor().newPromise());
            } ?: throw IllegalStateException()
        }

        private fun sendMessage(message: InkDeckMessage, prom: Promise<InkDeckMessage>): Future<InkDeckMessage> {
            synchronized(this) {
                if (clientClosed) {
                    // Connection closed
                    prom.setFailure(IllegalStateException())
                } else {
                    message.messageId = sequence.getAndIncrement()
                    queue[message.messageId!!] = prom
                    channelContext!!.writeAndFlush(message)
                }
                return prom
            }
        }

        fun disconnect() {
            clientClosed = true
            channelContext?.let {
                channelContext!!.close()
                channelContext = null
            }
        }

        override fun channelActive(ctx: ChannelHandlerContext) {
            super.channelActive(ctx)
            channelContext = ctx;
        }

        override fun channelInactive(ctx: ChannelHandlerContext?) {
            super.channelInactive(ctx)
            synchronized(this) {
                queue.values.forEach { m -> m.setFailure(IOException("Connection lost")) }
            }
        }

        override fun channelRead0(ctx: ChannelHandlerContext, msg: InkDeckMessage) {
            synchronized(this) {
                if (queue != null) {
                    val prom = queue[msg.messageId]
                    prom?.let {
                        it.setSuccess(msg)
                        println("[Client] channelRead0: msg = '$msg'")
                    }
                }
            }
        }

    }
}
