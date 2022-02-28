package com.steeplesoft.inkdeck.shared.encoding

import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.netty.util.CharsetUtil

/*
 * Message header: ID
 * Message size: Size of message + 4 bytes for length + 4 bytes for messageId
 */
class InkDeckMessageEncoder : MessageToByteEncoder<InkDeckMessage>() {
    override fun encode(ctx: ChannelHandlerContext, msg: InkDeckMessage, out: ByteBuf) {
        out.writeBytes(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
        out.writeInt(msg.size() + 4 * 2)
        out.writeInt(msg.messageId!!)
        out.writeInt(msg.type.code)
        out.writeBytes(msg.toBytes())
        ctx.flush()
    }
}
