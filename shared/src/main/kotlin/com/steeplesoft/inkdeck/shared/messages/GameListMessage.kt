package com.steeplesoft.inkdeck.shared.messages

import com.google.gson.Gson
import com.steeplesoft.inkdeck.shared.model.Game
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType

class GameListMessage(val games : List<Game>) : InkDeckMessage(InkDeckMessageType.GAMES_LIST) {
    private val json : String = Gson().toJson(games)

    override fun size(): Int {
        return json.length
    }

    override fun toBytes(): ByteArray {
        return json.toByteArray()
    }
}
