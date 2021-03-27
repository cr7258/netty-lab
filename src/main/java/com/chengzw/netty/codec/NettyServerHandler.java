package com.chengzw.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;


/**
 * 处理服务端端编解码
 * @author 程治玮
 * @since 2021/3/25 9:54 下午
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //测试String编码
        //System.out.println("从客户端读取到String：" + msg);

        //测试对象编码
        System.out.println("从客户端读取到Object：" + ((User)msg).toString());

        //测试用protostuff对对象编解码
        //ByteBuf buf = (ByteBuf) msg;
        //byte[] bytes = new byte[buf.readableBytes()];
        //buf.readBytes(bytes);
        //System.out.println("从客户端读取到Object：" + ProtostuffUtil.deserializer(bytes, User.class));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
