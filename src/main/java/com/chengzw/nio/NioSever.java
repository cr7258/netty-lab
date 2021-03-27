package com.chengzw.nio;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * NIO 非阻塞，服务端，每次遍历轮询所有的客户端连接去读取数据
 * @author 程治玮
 * @since 2021/3/21 12:36 下午
 */
public class NioSever {

    // 保存客户端连接
    static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        //创建NIO ServerSocketChannel,与 BIO 的 serverSocket 类似
        //创建一个在本地端口进行监听的服务 Socket 通道，并设置为非阻塞方式
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(9000));  //监听 9000 端口
        //设置 ServerSocketChannel 为非阻塞
        serverSocket.configureBlocking(false); //false 非阻塞，配置成 true ，就成 BIO 了
        System.out.println("服务启动成功");

        while (true) {
            // 非阻塞模式 accept() 方法不会阻塞
            // NIO 的非阻塞是由操作系统内部实现的，底层调用了 linux 内核的 accept 函数
            SocketChannel socketChannel = serverSocket.accept();
            if (socketChannel != null) { // 如果有客户端进行连接
                System.out.println("连接成功");
                // 设置 SocketChannel 为非阻塞
                socketChannel.configureBlocking(false); //false 非阻塞，配置成 true ，就成 BIO 了
                // 保存客户端连接在 List 中
                channelList.add(socketChannel);
            }
            // 遍历客户端连接 SocketChannel 进行数据读取
            Iterator<SocketChannel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                SocketChannel sc = iterator.next();
                ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                // 非阻塞模式 read() 方法不会阻塞
                int len = sc.read(byteBuffer);
                // 如果有数据，把数据打印出来
                if (len > 0) {
                    System.out.println("接收到消息：" + new String(byteBuffer.array()));
                } else if (len == -1) { // 如果客户端断开，把 socket 从集合中去掉
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }
        }
    }
}
