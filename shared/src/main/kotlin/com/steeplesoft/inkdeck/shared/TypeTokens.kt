package com.steeplesoft.inkdeck.shared

import com.google.gson.reflect.TypeToken
import com.steeplesoft.inkdeck.shared.model.Game

class TypeTokens {
    companion object {
        val GAMES_LIST_TYPE = object: TypeToken<List<Game>>(){}.type!!
    }
}
