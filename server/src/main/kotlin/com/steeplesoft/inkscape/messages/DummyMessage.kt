package com.steeplesoft.inkscape.messages

import com.steeplesoft.inkscape.model.InkDeckMessageType

class DummyMessage : InkDeckMessage(InkDeckMessageType.DUMMY) {
    lateinit var text : String

    override fun size(): Int {
        return text.length
    }

    override fun toBytes(): ByteArray {
        return text.toByteArray()
    }
}
