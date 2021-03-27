package com.chengzw.netty.chat;

import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

/**
 * 聊天程序客户端
 * @author 程治玮
 * @since 2021/3/25 9:54 下午
 */
public class ChatClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });

            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",9000).sync();
            //得到 channle
            Channel channel  = channelFuture.channel();
            System.out.println("========" + channel.localAddress() + "========");
            //客户端需要输入信息， 创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                //通过 channel 发送到服务器端
                channel.writeAndFlush(msg);
            }

        }finally {
            group.shutdownGracefully();
        }
    }
}
