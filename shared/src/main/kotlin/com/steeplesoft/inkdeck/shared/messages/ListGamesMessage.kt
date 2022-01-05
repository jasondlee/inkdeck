package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType

class ListGamesMessage : InkDeckMessage(InkDeckMessageType.LIST_GAMES) {
    override fun size(): Int {
        return 0
    }

    override fun toBytes(): ByteArray {
        return ByteArray(0)
    }
}
