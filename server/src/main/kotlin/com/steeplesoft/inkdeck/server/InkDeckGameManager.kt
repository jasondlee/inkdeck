package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.ErrorMessage
import com.steeplesoft.inkdeck.shared.messages.GameJoinedMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import com.steeplesoft.inkdeck.shared.messages.JoinGameMessage
import com.steeplesoft.inkdeck.shared.model.Game
import com.steeplesoft.inkdeck.shared.model.GameClient
import io.netty.channel.Channel
import io.netty.channel.group.ChannelMatchers
import java.lang.Error
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class InkDeckGameManager {
    private val games = ConcurrentHashMap<UUID, Game>()

    init {
        val game1 = UUID.fromString("6ba33f74-22f6-4a93-8a33-ec13ecf4fc25")
        val game2 = UUID.fromString("ef0863b6-2773-473b-b9ac-ac06225bbffd")
        games[game1] = Game(game1, "Owner 1", Random.nextInt(5), Random.nextBoolean())
        games[game2] = Game(game2, "Owner 2", Random.nextInt(5), Random.nextBoolean())
    }

    fun listGames() : GameListMessage {
        return GameListMessage(games.values.toList())
    }

    fun addGame(): Game {
        val id = UUID.randomUUID()
        val game = Game(id, "dummy", Random.nextInt(5), Random.nextBoolean())
        games[id] = game
        return game
    }

    fun joinGame(msg: JoinGameMessage, channel: Channel) : InkDeckMessage {
        val game = games[msg.gameId]

        if (game != null) {
            val existing = game.clients.find { it.clientId == msg.clientId }
            if (existing != null) {
                game.channels.disconnect(ChannelMatchers.`is`(game.channels.find(existing.channelId)))
                existing.channelId = channel.id()
            } else {
                game.clients.add(GameClient(msg.clientId, channel.id()))
            }
            game.channels.add(channel)

            return GameJoinedMessage(msg.gameId)
        } else {
            return ErrorMessage("Game ${msg.gameId} not found")
        }
    }
}
