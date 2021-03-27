package com.chengzw.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 客户端程序
 * @author 程治玮
 * @since 2021/3/21 2:25 下午
 */
public class NioClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",9000));
        socketChannel.write(ByteBuffer.wrap("2222HelloServer".getBytes()));
        System.out.println("客户端发送数据完毕...");
    }
}
