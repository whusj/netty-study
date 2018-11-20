package com.net.gupao.netty;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * <p>ClassName:</p>
 * <p>Description:</p>
 * <p>Author:Songjiang</p>
 * <p>CreateDate:2018/11/19</p>
 */
public class NettyClient implements Runnable {
    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                            pipeline.addLast("decider",new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));
                            pipeline.addLast("handler",new MyClient());
                        }
                    });
            for (int i = 0; i < 10; i++) {
                ChannelFuture f = bootstrap.connect("127.0.0.1",6666).sync();
                f.channel().write("hello service " + Thread.currentThread().getName() + ":------->" + i);
                f.channel().closeFuture().sync();
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(new NettyClient(),">>> this thread "+ i).start();
        }
    }
}
