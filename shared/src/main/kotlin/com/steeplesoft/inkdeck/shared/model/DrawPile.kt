package com.steeplesoft.inkdeck.shared.model

class DrawPile {
    var cards : MutableList<Int> = IntRange(2,99).shuffled().toMutableList()

    fun draw(count : Int): List<Int> {
        val drawn = cards.take(count)
        cards = cards.drop(count) as MutableList<Int>
        return drawn
    }
}
