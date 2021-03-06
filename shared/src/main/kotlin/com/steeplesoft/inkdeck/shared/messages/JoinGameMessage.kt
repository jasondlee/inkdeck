package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import java.util.UUID

class JoinGameMessage(val gameId: UUID, val clientId: UUID) :  InkDeckMessage(InkDeckMessageType.JOIN_GAME) {

    val contents = "${gameId}|${clientId}"

    override fun size(): Int {
        return contents.length
    }

    override fun toBytes(): ByteArray {
        return gameId.toString().toByteArray()
    }

}