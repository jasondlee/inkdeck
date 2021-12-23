package com.steeplesoft.inkscape.shared

import com.steeplesoft.inkscape.messages.InkDeckMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.CharsetUtil

class InkDeckMessageEncoder : MessageToByteEncoder<InkDeckMessage>() {
    override fun encode(ctx: ChannelHandlerContext, msg: InkDeckMessage, out: ByteBuf) {
        out.writeBytes(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
        out.writeInt(msg.size() + 4)
        out.writeInt(msg.type.code)
        out.writeBytes(msg.toBytes())
        ctx.flush()
    }
}
