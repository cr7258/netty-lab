package com.chengzw.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty 服务端
 * @author 程治玮
 * @since 2021/3/25 9:31 下午
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        // 创建两个线程组 bossGroup 和 workerGroup, 含有的子线程 NioEventLoop 的个数默认为cpu核数的两倍
        // bossGroup 只是处理连接请求 ,真正的和客户端业务处理，会交给 workerGroup 完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(1); //1个线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(8); //8个线程

        try {
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来配置参数
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    // 使用 NioServerSocketChannel 作为服务器的通道实现，该类用于实例化新的 Channel 来接收客户端的连接
                    .channel(NioServerSocketChannel.class)
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                    // 多个客户端同时来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建通道初始化对象，设置初始化参数，在 SocketChannel 建立起来之前执行
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //对 workerGroup的SocketChannel 设置处理器，调用我们自定义的 NettyServerHandler
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("netty server start ...");
            // 绑定一个端口并且同步, 生成了一个 ChannelFuture 异步对象，通过 isDone() 等方法可以判断异步事件的执行情况
            // 启动服务器(并绑定端口)，bind 是异步操作，sync 方法是等待异步操作执行完毕
            // sync 同步
            ChannelFuture channelFuture = bootstrap.bind(9000).sync();

            // 异步
            // 给cf注册监听器，监听我们关心的事件
            /*channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("监听端口9000成功");
                    } else {
                        System.out.println("监听端口9000失败");
                    }
                }
            });*/
            // 等待服务端监听端口关闭，closeFuture是异步操作
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成，内部调用的是Object的wait()方法
            // 在这里面cf.channel().closeFuture().sync();这个语句的主要目的是，如果缺失上述代码，则main方法所在的线程，
            // 即主线程会在执行完bind().sync()方法后，会进入finally 代码块，之前的启动的nettyserver也会随之关闭掉，整个程序都结束了。
            // 原文的例子有英文注释：
            // Wait until the server socket is closed，In this example, this does not happen, but you can do that to gracefully shut down your server.
            // 线程进入wait状态，也就是main线程暂时不会执行到finally里面，nettyserver也持续运行，如果监听到关闭事件，可以优雅的关闭通道和nettyserver，
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 资源优雅释放
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
