package com.chengzw.netty.splitpacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 处理粘包拆包问题
 * 发送每条数据的时候，将数据的长度一并发送，比如可以选择每条数据的前4位是数据的长度，应用层处理时可以根据长度 来判断每条数据的开始和结束。
 * @author 程治玮
 * @since 2021/3/25 9:31 下午
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {
        System.out.println("====服务端接收到消息如下====");
        System.out.println("长度=" + msg.getLen());
        System.out.println("内容=" + new String(msg.getContent(), CharsetUtil.UTF_8));

        System.out.println("服务端接收到消息包数量=" + (++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
