package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.shared.model.Game
import java.util.UUID
import kotlin.random.Random

class InkDeckGameManager {
    private val games = mutableListOf<Game>()

    init {
        games.add(Game(UUID.randomUUID(), "dummy", Random.nextInt(5), Random.nextBoolean()))
        games.add(Game(UUID.randomUUID(), "dummy", Random.nextInt(5), Random.nextBoolean()))
    }

    fun listGames() : List<Game> {
        return games
    }

    fun addGame(): Game {
        val game = Game(UUID.randomUUID(), "dummy", Random.nextInt(5), Random.nextBoolean())
        games.add(game)
        return game
    }
}
