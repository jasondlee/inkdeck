package com.steeplesoft.inkscape.server

import com.steeplesoft.inkscape.client.InkDeckClient
import com.steeplesoft.inkscape.messages.DummyMessage
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
import io.netty.util.CharsetUtil
import org.junit.jupiter.api.Test
import java.awt.SystemColor.text
import java.net.InetSocketAddress
import java.nio.charset.Charset


class ServerSmokeTest {
    @Test
    fun testServer() {
        val client = InkDeckClient("localhost", 49152)
        client.connect()
        client.sendMessage(DummyMessage().apply {
            text = "This is a fancy client!"
        })
        client.sendMessage(DummyMessage().apply {
            text = "This is a another test!"
        })
        Thread.sleep(10000)
//        val host = "localhost"
//        val port = 49152
//        val group: EventLoopGroup = NioEventLoopGroup()
//        try {
//            val clientBootstrap = Bootstrap()
//            clientBootstrap.group(group)
//            clientBootstrap.channel(NioSocketChannel::class.java)
//            clientBootstrap.remoteAddress(InetSocketAddress(host, port))
//            clientBootstrap.handler(object : ChannelInitializer<SocketChannel>() {
//                @Throws(Exception::class)
//                override fun initChannel(socketChannel: SocketChannel) {
//                    socketChannel.pipeline().addLast(SmokeTestHandler())
//                }
//            })
//            val channelFuture = clientBootstrap.connect().sync()
//            channelFuture.channel().closeFuture().sync()
//        } finally {
//            group.shutdownGracefully().sync()
//        }
    }
}

class SmokeTestHandler : SimpleChannelInboundHandler<ByteBuf>() {
    override fun channelActive(context: ChannelHandlerContext) {
        val msg = "This is just a test";
        context.write(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
        context.write(Unpooled.copyInt(msg.length + 4))
        context.write(Unpooled.copyInt(255))
        context.write(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8))
        context.flush()
//        context.writeAndFlush(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
    }

    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, msg: ByteBuf) {
        println("Client received: " + msg.toString(CharsetUtil.UTF_8))
        channelHandlerContext.close()
    }

}
