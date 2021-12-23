package com.steeplesoft.inkscape.client

import com.steeplesoft.inkscape.messages.DummyMessage
import com.steeplesoft.inkscape.messages.InkDeckMessage
import com.steeplesoft.inkscape.shared.InkDeckMessageEncoder
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.util.CharsetUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.InetSocketAddress
import java.util.Date
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class InkDeckClient(private val host: String, private val port: Int) {
    private val clientHandler = InkDeckClientHandler()
    private val group: EventLoopGroup = NioEventLoopGroup()

    fun connect() {
        val clientBootstrap = Bootstrap()
        clientBootstrap.group(group)
        clientBootstrap.channel(NioSocketChannel::class.java)
//        clientBootstrap.remoteAddress(InetSocketAddress(host, port))
        clientBootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            override fun initChannel(socketChannel: SocketChannel) {
                socketChannel.pipeline().addLast(
                    InkDeckMessageEncoder(),
                    StringDecoder(),
                    clientHandler)
            }
        })
        val channelFuture = clientBootstrap.connect(host, port).sync()
        channelFuture.channel().closeFuture().addListener {
            group.shutdownGracefully().sync()
        }
    }

    fun sendMessage(msg: InkDeckMessage) {
        clientHandler.queue.add(msg)
    }

    fun disconnect() {
        clientHandler.clientClosed = true

    }

    private class InkDeckClientHandler : SimpleChannelInboundHandler<ByteBuf>() {
        val queue = ArrayBlockingQueue<InkDeckMessage>(10, true)
        var clientClosed = false

        override fun channelActive(ctx: ChannelHandlerContext) {
            ctx.writeAndFlush(DummyMessage().apply {
                text = "This is a test!!! ? (${Date()})"
            })
//            thread(start = true) {
//                while (!clientClosed) {
//                    val message = queue.poll(500, TimeUnit.MILLISECONDS)
//                    message?.let {
////                        ctx.write(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
////                        ctx.write(message.size())
////                        ctx.write(message.type.code)
////                        ctx.write(message.toBytes())
////                        ctx.flush()
//                        println("Writing $message")
//                        ctx.writeAndFlush(message)
//                    }
//                }
//                ctx.close()
//            }
        }

        override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf) {
            println("Client received: " + msg.toString(CharsetUtil.UTF_8))
        }

    }
}
