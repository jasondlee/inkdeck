package com.steeplesoft.inkscape.model

class DrawPile {
    var cards : MutableList<Int> = IntRange(2,99).shuffled().toMutableList()

    fun draw(count : Int): List<Int> {
        val drawn = cards.take(count)
        cards = cards.drop(count) as MutableList<Int>
        return drawn
    }
}
