package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType

class ErrorMessage(private var text: String) : InkDeckMessage(InkDeckMessageType.ERROR) {
    override fun size(): Int {
        return text.length
    }

    override fun toBytes(): ByteArray {
        return text.toByteArray()
    }
}
