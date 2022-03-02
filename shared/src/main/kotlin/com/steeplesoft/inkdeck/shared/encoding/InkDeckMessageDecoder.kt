package com.steeplesoft.inkdeck.shared.encoding

import com.google.gson.Gson
import com.steeplesoft.inkdeck.shared.TypeTokens.Companion.GAMES_LIST_TYPE
import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.ErrorMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.ListGamesMessage
import com.steeplesoft.inkdeck.shared.model.Game
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType.DUMMY
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType.GAMES_LIST
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType.LIST_GAMES
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.util.CharsetUtil

class InkDeckMessageDecoder : ByteToMessageDecoder() {
    override fun decode(ctx: ChannelHandlerContext, msg: ByteBuf, list: MutableList<Any>) {
        val header = msg.readCharSequence(2, CharsetUtil.UTF_8)
        val length = msg.readInt()
        val messageId = msg.readInt()
        val type = InkDeckMessageType.valueOf(msg.readInt())
        val body = if (length > 0) msg.readBytes(length - (4 * 2)).toString(CharsetUtil.UTF_8) else null

        if (header != "ID") {
            throw IllegalArgumentException("Not an InkDeck message: $header")
        }

        val message = when (type) {
            LIST_GAMES -> ListGamesMessage()
            GAMES_LIST -> GameListMessage(Gson().fromJson<List<Game>>(body!!, GAMES_LIST_TYPE))
            DUMMY -> DummyMessage(body!!)
            else -> ErrorMessage("Not a known InkDeck message type: $type")
        }
        message.messageId = messageId
        list.add(message)
    }

    private fun readBody(msg: ByteBuf, length: Int) =
        msg.readBytes(length - 4).toString(CharsetUtil.UTF_8)
}
