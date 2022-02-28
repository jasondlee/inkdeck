package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType

abstract class InkDeckMessage(var type: InkDeckMessageType) {
    var messageId: Int? = null
    abstract fun size() : Int
    abstract fun toBytes(): ByteArray
    override fun toString(): String {
        return "InkDeckMessage(type=$type, messagedId = $messageId)"
    }
}
