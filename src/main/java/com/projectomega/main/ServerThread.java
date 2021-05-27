package com.projectomega.main;

import com.projectomega.main.packets.PacketUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ServerThread extends Thread {

    private int port;
    private String address;

    private ServerBootstrap b;
    private static Channel channel;

    private HashMap<SocketAddress, ChannelFuture> connections = new HashMap<SocketAddress, ChannelFuture>();


    public ServerThread() throws IOException {
        this.start();
        port = 25565;
        address = "localhost";
    }

    public static Channel getChannel() {
        return channel;
    }

    @Override
    public void run() {
        PacketUtil.init(this);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //if (sslCtx != null) {
                            //   p.addLast(sslCtx.newHandler(ch.alloc()));
                            //}
                            p.addLast(new ServerInputHandler());
                        }
                    });


            // Start the server.
            ChannelFuture f = b.bind(port).sync();

            channel= f.channel();
            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("Failed to start server: ");
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
