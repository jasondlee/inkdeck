package com.steeplesoft.inkscape.messages

import com.steeplesoft.inkscape.model.InkDeckMessageType

abstract class InkDeckMessage(var type: InkDeckMessageType) {
    abstract fun size() : Int
    abstract fun toBytes(): ByteArray
}
