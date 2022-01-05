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
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class InkDeckClient(private val host: String, private val port: Int) {
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

    fun sendMessage(msg: InkDeckMessage) {
        clientHandler.sendMessage(msg)
    }

    fun disconnect() {
        clientHandler.disconnect()
        channelFuture.channel().closeFuture().addListener {
            group.shutdownGracefully().sync()
        }
    }

    private class InkDeckClientHandler : SimpleChannelInboundHandler<InkDeckMessage>() {
        private lateinit var channelContext: ChannelHandlerContext
        private val queue = ArrayBlockingQueue<InkDeckMessage>(10, true)
        private var clientClosed = false

        fun sendMessage(message: InkDeckMessage) {
            if (!clientClosed) {
                queue.add(message)
            }
        }

        fun disconnect() {
            if (::channelContext.isInitialized) {
                var tries = 0;
                while (queue.isNotEmpty() && tries < 10) {
                    tries++
                    Thread.sleep(500)
                }

                clientClosed = true
                channelContext.close()
            }
        }

        override fun channelActive(ctx: ChannelHandlerContext) {
            channelContext = ctx;
            thread(start = true) {
                while (!clientClosed) {
                    val message = queue.poll(500, TimeUnit.MILLISECONDS)
                    message?.let {
                        println("Writing $message")
                        ctx.writeAndFlush(message)
                    }
                }
            }
        }

        override fun channelRead0(ctx: ChannelHandlerContext, msg: InkDeckMessage) {
            println(msg)
        }

    }
}
