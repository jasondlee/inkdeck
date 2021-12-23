package com.steeplesoft.inkscape.server

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.CharsetUtil
import java.nio.charset.Charset

class InkDeckServerHandler : SimpleChannelInboundHandler<ByteBuf>() {
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        println("InkDeckServerHandler added")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        println("InkDeckServerHandler removed")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf) {
        val header = msg.readCharSequence(2, CharsetUtil.UTF_8)
        val length = msg.readInt()
        val type = msg.readInt()
        val body = msg.readBytes(length-4)
        println("header = $header")
        println("length = $length")
        println("type = $type")
        println("body = ${body.toString(CharsetUtil.UTF_8)}")

        ctx.writeAndFlush("${length + 10} bytes processed")
    }
}
