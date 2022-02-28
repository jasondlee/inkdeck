package com.steeplesoft.inkdeck.server

import com.steeplesoft.inkdeck.client.InkDeckClient
import com.steeplesoft.inkdeck.shared.messages.DummyMessage
import com.steeplesoft.inkdeck.shared.messages.GameListMessage
import com.steeplesoft.inkdeck.shared.messages.InkDeckMessage
import com.steeplesoft.inkdeck.shared.messages.ListGamesMessage
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.util.CharsetUtil
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.Promise
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean


class ServerSmokeTest {
    companion object {
        private lateinit var server: InkDeckServer

        @BeforeAll
        @JvmStatic
        fun startServer() {
            System.err. println("***** Starting server")
            server = InkDeckServer()
            server.start()
        }

        @AfterAll
        @JvmStatic
        fun stopServer() {
            System.err.println("***** Stopping server")
            server.stop()
        }
    }

    @Test
    fun testServer() {
        val client1 = InkDeckClient("localhost", 49152)
        val client2 = InkDeckClient("localhost", 49152)

        client1.sendMessage(DummyMessage("Message from client 1"))
        client2.sendMessage(DummyMessage("Message from client 2"))
        client1.sendMessage(DummyMessage("Another message from client 1"))

        client1.disconnect()
        client2.disconnect()
    }

    @Test
    fun testListGames() {
        val client1 = InkDeckClient("localhost", 49152)

        var waiting = true

        client1.sendMessage(ListGamesMessage()).addListener {
            val message = it.get() as InkDeckMessage
            println("games = ${(message as GameListMessage).games}")
            waiting = false
            client1.disconnect()
        }

        while (waiting) {
            Thread.sleep(500)
        }
    }
}

class SmokeTestHandler : SimpleChannelInboundHandler<ByteBuf>() {
    override fun channelActive(context: ChannelHandlerContext) {
        val msg = "This is just a test";
        context.write(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
        context.write(Unpooled.copyInt(msg.length + 4))
        context.write(Unpooled.copyInt(255))
        context.write(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8))
        context.flush()
//        context.writeAndFlush(Unpooled.copiedBuffer("ID", CharsetUtil.UTF_8))
    }

    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, msg: ByteBuf) {
        println("Client received: " + msg.toString(CharsetUtil.UTF_8))
        channelHandlerContext.close()
    }

}
