package com.steeplesoft.inkdeck.shared.messages

import com.steeplesoft.inkdeck.shared.model.InkDeckMessageType

abstract class InkDeckMessage(var type: InkDeckMessageType) {
    abstract fun size() : Int
    abstract fun toBytes(): ByteArray
    override fun toString(): String {
        return "InkDeckMessage(type=$type)"
    }
}
