package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.shared.encoding.InkDeckMessageCodec
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext


class InkDeckServerInitializer(private val sslCtx: SslContext?) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        val pipeline = ch.pipeline()

        if (sslCtx != null) {
//            pipeline.addLast(sslCtx.newHandler(ch!!.alloc(), FactorialClient.HOST, FactorialClient.PORT))
        }

        pipeline.addLast(LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4, 0, 0))
        pipeline.addLast(InkDeckMessageCodec())
//        pipeline.addLast(InkDeckMessageDecoder())
        pipeline.addLast(StringEncoder())
//        pipeline.addLast(InkDeckMessageEncoder())

        pipeline.addLast(InkDeckServerHandler(InkDeckGameManager()))
    }
}
