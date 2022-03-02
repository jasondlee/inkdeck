package com.steeplesoft.inkdeck.shared.messages

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.steeplesoft.inkdeck.shared.model.Game
import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import java.lang.reflect.Modifier

class GameListMessage(val games : List<Game>) : InkDeckMessage(InkDeckMessageType.GAMES_LIST) {
    private val gson = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()
    private val json : String = gson.toJson(games)

    override fun size(): Int {
        return json.length
    }

    override fun toBytes(): ByteArray {
        return json.toByteArray()
    }
}
