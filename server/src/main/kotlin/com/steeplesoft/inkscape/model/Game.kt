package com.steeplesoft.inkscape.model

import java.util.UUID

data class Game(
    val id: UUID,
    val owner: String,
    var numberOfPlayers : Int,
    var private : Boolean = false
)
