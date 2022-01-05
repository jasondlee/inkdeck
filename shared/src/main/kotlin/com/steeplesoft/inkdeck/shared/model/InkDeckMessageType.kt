package com.steeplesoft.inkdeck.shared.model

enum class InkDeckMessageType(val code: Int) {
    LIST_GAMES(1),
    GAMES_LIST(2),


    CLIENT_DISCONNECT(3),


    ERROR(254),
    DUMMY(255)
    ;

    companion object {
        fun valueOf(value : Int): InkDeckMessageType? {
            return values().firstOrNull { it.code == value }
        }
    }
}
