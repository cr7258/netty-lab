package com.chengzw.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义 Handler 需要继承 netty 规定好的某个 HandlerAdapter(规范)
 * @author 程治玮
 * @since 2021/3/25 9:31 下午
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读取客户端发送的数据
     *
     * @param ctx 上下文对象, 含有通道 channel，管道 pipeline
     * @param msg 就是客户端发送的数据
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
        //将 msg 转成一个 ByteBuf，类似 NIO 的 ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是:" + buf.toString(CharsetUtil.UTF_8));
    }


    /**
     * 数据读取完毕后的处理方法
     *
     * @param ctx 上下文对象, 含有通道 channel，管道 pipeline
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("HelloClient".getBytes(CharsetUtil.UTF_8));
        ctx.writeAndFlush(buf);
    }

    /**
     * 处理异常, 一般是需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}

