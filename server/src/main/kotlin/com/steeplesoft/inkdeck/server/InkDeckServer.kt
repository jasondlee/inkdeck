package com.steeplesoft.inkdeck.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.lang.Exception


class InkDeckServer {
    private val SSL: Boolean = (System.getProperty("ssl") != null)
    private val PORT: Int = System.getProperty("port", "49152").toInt()

    private val bossGroup: EventLoopGroup = NioEventLoopGroup(1)
    private val workerGroup: EventLoopGroup = NioEventLoopGroup()
    private var channel: Channel? = null

    fun start() {
        val sslCtx: SslContext? = if (SSL) {
            val ssc = SelfSignedCertificate()
            SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build()
        } else {
            null
        }

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(InkDeckServerInitializer(sslCtx))
            channel = b.bind(PORT).sync().channel()
        } catch (e: Exception) {
            stop()
        }
    }

    fun stop() {
//        channel?.closeFuture()?.sync()

        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}

fun main(args: Array<String>) {
    InkDeckServer().start()
}
