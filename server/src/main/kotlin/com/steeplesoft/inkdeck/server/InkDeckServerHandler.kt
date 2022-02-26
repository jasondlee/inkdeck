package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.ErrorMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class InkDeckServerHandler(private val gameHandler : InkDeckGameManager) : SimpleChannelInboundHandler<InkDeckMessage>() {
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        println("[Server] InkDeckServerHandler added: $this - $ctx")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        println("[Server] InkDeckServerHandler removed")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: InkDeckMessage) {
        println("[Server] channelRead0: msg = '$msg'")

        val response : InkDeckMessage = when (msg.type) {
            InkDeckMessageType.LIST_GAMES -> GameListMessage(gameHandler.listGames())
            InkDeckMessageType.DUMMY -> DummyMessage("Message received: " + (msg as DummyMessage).text)
            else -> ErrorMessage("Not a known InkDeck message type: ${msg.type}")
        }

        ctx.writeAndFlush(response)
    }
}
