package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.ErrorMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import com.steeplesoft.inkdeck.shared.messages.JoinGameMessage
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class InkDeckServerHandler(private val gameHandler : InkDeckGameManager) : SimpleChannelInboundHandler<InkDeckMessage>() {
    override fun handlerAdded(ctx: ChannelHandlerContext) {
        println("[Server] InkDeckServerHandler added: $this - ${ctx.channel().id()}")
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext) {
        println("[Server] InkDeckServerHandler removed: ${ctx.channel().id()}")
    }

    override fun channelRead0(ctx: ChannelHandlerContext, msg: InkDeckMessage) {
        println("[Server] channelRead0: msg = '$msg' from context ${ctx.channel().id()}")

        val response : InkDeckMessage = when (msg.type) {
            InkDeckMessageType.LIST_GAMES -> gameHandler.listGames()
            InkDeckMessageType.JOIN_GAME -> gameHandler.joinGame(msg as JoinGameMessage, ctx.channel())

            InkDeckMessageType.DUMMY -> DummyMessage("Message received: " + (msg as DummyMessage).text)
            else -> ErrorMessage("Not a known InkDeck message type: ${msg.type}")
        }

        response.messageId = msg.messageId

        ctx.writeAndFlush(response)
    }
}
