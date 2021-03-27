package com.chengzw.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 同步非阻塞
 * NIO 引入多路复用器 Selector，只对有事件的 serverSocket（本例是客户端连接或者客户端发送数据）进行处理
 * @author 程治玮
 * @since 2021/3/21 12:49 下午
 */
public class NioSelectorServer {

    public static void main(String[] args) throws IOException, InterruptedException {

        //创建NIO ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(9000)); //监听 9000 端口
        //设置 ServerSocketChannel 为非阻塞
        //必须配置为非阻塞才能往 Selector 上注册，否则会报错，Selector 本身就是非阻塞模式
        serverSocketChannel.configureBlocking(false);  //false 非阻塞
        // 打开 Selector 处理 Channel ，即创建 epoll
        Selector selector = Selector.open();
        // 把 ServerSocketChannel 注册到 Selector 上，并且 Selector 对客户端 accept 连接操作感兴趣
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务启动成功");

        while (true) {
            //阻塞等待需要处理的事件发生
            //轮询监听 channel 里的 key，select()是阻塞的，当有客户端连接事件发生时 serverSocket.register(selector, SelectionKey.OP_ACCEPT)
            //或者是读取客户端传的数据 socketChannel.register(selector, SelectionKey.OP_READ)，才会停止阻塞
            selector.select();

            // 获取 selector 中注册的全部事件的 SelectionKey 实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            // 遍历 SelectionKey 对事件进行处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 如果是OP_ACCEPT事件，则进行连接获取和事件注册
                if (key.isAcceptable()) {
                    //通过 selector 注册事件的 Key 获取到对应的客户端连接的 ServerSocket
                    ServerSocketChannel serverSocket = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = serverSocket.accept(); //连接客户端
                    socketChannel.configureBlocking(false);  //false 非阻塞

                    // 把客户端连接的 socketChannel 注册到 Selector 上，对读操作感兴趣
                    // 这里只注册了读事件，如果需要给客户端发送数据可以注册写事件
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                } else if (key.isReadable()) {  // 如果是OP_READ事件，则进行读取和打印
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                    int len = socketChannel.read(byteBuffer);
                    // 如果有数据，把数据打印出来
                    if (len > 0) {
                        System.out.println("接收到消息：" + new String(byteBuffer.array()));
                    } else if (len == -1) { // 如果客户端断开连接，关闭Socket
                        System.out.println("客户端断开连接");
                        socketChannel.close();
                    }
                }
                //从事件集合里删除本次处理的key，防止下次select重复处理
                iterator.remove();
            }
        }
    }
}
