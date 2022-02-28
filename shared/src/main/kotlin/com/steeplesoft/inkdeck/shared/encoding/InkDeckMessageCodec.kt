package com.steeplesoft.inkdeck.shared.encoding

import com.google.gson.Gson
import com.steeplesoft.inkdeck.shared.TypeTokens
import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.ErrorMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import com.steeplesoft.inkdeck.shared.messages.ListGamesMessage
import com.steeplesoft.inkdeck.shared.model.Game
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageCodec
import io.netty.util.CharsetUtil
import java.lang.IllegalArgumentException

class InkDeckMessageCodec :ByteToMessageCodec<InkDeckMessage>() {
    override fun encode(ctx: ChannelHandlerContext, msg: InkDeckMessage, out: ByteBuf) {
        out.writeBytes(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
        out.writeInt(msg.size() + 4 * 2)
        out.writeInt(msg.messageId!!)
        out.writeInt(msg.type.code)
        out.writeBytes(msg.toBytes())
        ctx.flush()
    }

    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, list: MutableList<Any>) {
        val header = msg.readCharSequence(2, CharsetUtil.UTF_8)
        val length = msg.readInt()
        val messageId = msg.readInt()
        val type = InkDeckMessageType.valueOf(msg.readInt())
        val body = if (length > 0) msg.readBytes(length - (4 * 2)).toString(CharsetUtil.UTF_8) else null

        if (header != "ID") {
            println("Invalid message: header=$header type=$type body=$body")
            throw IllegalArgumentException("Not an InkDeck message: $header")
        }

        val message = when (type) {
            InkDeckMessageType.LIST_GAMES -> ListGamesMessage()
            InkDeckMessageType.GAMES_LIST -> GameListMessage(Gson().fromJson<List<Game>>(body!!, TypeTokens.GAMES_LIST_TYPE))
            InkDeckMessageType.DUMMY -> DummyMessage(body!!)
            else -> ErrorMessage("Not a known InkDeck message type: $type")
        }
        message.messageId = messageId
        list.add(message)
    }


}