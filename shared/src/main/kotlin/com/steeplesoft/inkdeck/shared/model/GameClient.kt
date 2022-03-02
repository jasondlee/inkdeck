package com.steeplesoft.inkdeck.shared.model

import io.netty.channel.ChannelId
import java.util.UUID

data class GameClient(val clientId: UUID, var channelId: ChannelId)