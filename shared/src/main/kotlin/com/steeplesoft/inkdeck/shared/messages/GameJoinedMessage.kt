package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType
import java.util.UUID

class GameJoinedMessage(val gameId : UUID)  : InkDeckMessage(InkDeckMessageType.GAME_JOINED) {
    override fun size(): Int {
        return 36
    }

    override fun toBytes(): ByteArray {
        return gameId.toString().toByteArray()
    }
}