package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType


class DummyMessage(private var text: String) : InkDeckMessage(InkDeckMessageType.DUMMY) {
    override fun size(): Int {
        return text.length
    }

    override fun toBytes(): ByteArray {
        return text.toByteArray()
    }
}
