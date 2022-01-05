package com.steeplesoft.inkdeck.shared.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DrawPileTest {
    @Test
    fun testDrawOne() {
        var drawPile = DrawPile()

        val top = drawPile.cards[0]
        val drawn = drawPile.draw(1).first()

        assertThat(drawn).isEqualTo(top)
        assertThat(drawPile.cards[0]).isNotEqualTo(drawn)
        assertThat(drawPile.cards[0]).isNotEqualTo(top)
        assertThat(drawPile.cards.size).isEqualTo(97)
    }

    @Test
    fun testDrawTwo() {
        var drawPile = DrawPile()
        val one = drawPile.cards[0]
        val two = drawPile.cards[1]

        val drawn = drawPile.draw(2)

        assertThat(drawn.size).isEqualTo(2)
        assertThat(drawn[0]).isEqualTo(one)
        assertThat(drawn[1]).isEqualTo(two)
        assertThat(drawPile.cards.size).isEqualTo(96)
    }
}
