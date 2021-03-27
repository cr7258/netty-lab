package com.chengzw.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 聊天程序服务端
 * @author 程治玮
 * @since 2021/3/25 9:54 下午
 */
public class ChatServer {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //责任链设计模式，netty 会依次调用 Handler，handler 分为出站和入站方向
                            //向 pipeline 加入解码器
                            pipeline.addLast("decoder",new StringDecoder());
                            //向 pipeline 加入编码器
                            pipeline.addLast("encoder",new StringEncoder());
                            //向 pipeline 加入自己的业务处理 handler
                            pipeline.addLast(new ChatServerHandler());
                        }
                    });
            System.out.println("聊天室 server 启动...");
            //启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
