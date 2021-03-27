package com.chengzw.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理客户端编解码
 * @author 程治玮
 * @since 2021/3/25 9:54 下午
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到服务器消息:" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler发送数据");

        //测试String编码
        //ctx.writeAndFlush("测试String编解码");

        //测试对象编解码
        ctx.writeAndFlush(new User(1,"zhuge"));

        //测试用protostuff对对象编解码
        //ByteBuf buf = Unpooled.copiedBuffer(ProtostuffUtil.serializer(new User(1, "zhuge")));
        //ctx.writeAndFlush(buf);

    }
}
