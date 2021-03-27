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

public class MyClientHandler extends SimpleChannelInboundHandler<MyMessageProtocol> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0; i< 10; i++) {
            String msg = "你好，我是张三！";
            //创建协议包对象
            MyMessageProtocol messageProtocol = new MyMessageProtocol();
            messageProtocol.setLen(msg.getBytes(CharsetUtil.UTF_8).length);
            messageProtocol.setContent(msg.getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyMessageProtocol msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
