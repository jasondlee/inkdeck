package com.steeplesoft.inkdeck.shared.model

import com.google.gson.annotations.Expose
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.UUID

data class Game(
    @Expose
    val id: UUID,
    @Expose
    val owner: String,
    @Expose
    var numberOfPlayers: Int,
    var private: Boolean = false
) {
    val channels: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    val clients = mutableListOf<GameClient>()
}
