package com.net.netty.helloworld;

import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.Date;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/11/20</p>
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
	/**
	- 建⽴立连接时，发送⼀一条庆祝消息
	*/
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//为新连接发送庆祝
        ctx.write("Welcome to" + InetAddress.getLocalHost().getHostName()+"!\r\n");
        ctx.write("It is " + new Date() + " now.\r\n");
        ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

	//异常处理理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

	//业务逻辑处理
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        //Generate and write a response.
		String response;
        boolean close = false;
        if(request.isEmpty()){
            response = "Please type something.\r\n";
        }else if("bye".equals(request.toLowerCase())){
            response = "Have a good day!\r\n";
            close = true;
        }else {
            log.info("server received msg: " + request);
            response = "Did you say '"+ request + "'?\r\n";
        }
        ChannelFuture future = ctx.write(response);
        if(close){
            future.addListener(ChannelFutureListener.CLOSE);
        }

    }
}
