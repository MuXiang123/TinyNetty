package io.netty.example.study.client;

import io.netty.bootstrap.Bootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.codec.OrderFrameDecoder;
import io.netty.example.study.client.codec.OrderFrameEncoder;
import io.netty.example.study.client.codec.OrderProtocolDecoder;
import io.netty.example.study.client.codec.OrderProtocolEncoder;
import io.netty.example.study.client.handler.dispatcher.ClientIdleCheckHandler;
import io.netty.example.study.client.handler.dispatcher.KeepaliveHandler;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.auth.AuthOperation;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.util.concurrent.ExecutionException;

/**
 * @author: zhk
 * @description: 客户端
 * @date: 2023/6/22 20:44
 * @version: 1.0
 */
public class Client {
    public static void main(String[] args) throws InterruptedException, ExecutionException, SSLException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        //调整连接超时
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        bootstrap.option(NioChannelOption.SO_REUSEADDR, true);
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group);
            KeepaliveHandler keepaliveHandler = new KeepaliveHandler();
            LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
            SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();

            //下面这行，先直接信任自签证书，以避免没有看到ssl那节课程的同学运行不了；
            //学完ssl那节后，可以去掉下面这行代码，安装证书，安装方法参考课程，执行命令参考resources/ssl.txt里面
            sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE);

            SslContext sslContext = sslContextBuilder.build();
            //client不区分childHandler
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();

                    pipeline.addLast(new ClientIdleCheckHandler());
                    pipeline.addLast(sslContext.newHandler(ch.alloc()));

                    pipeline.addLast(new OrderFrameDecoder());
                    pipeline.addLast(new OrderFrameEncoder());
                    pipeline.addLast(new OrderProtocolEncoder());
                    pipeline.addLast(new OrderProtocolDecoder());

                    pipeline.addLast(keepaliveHandler);
                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
            //确保连接成功
            channelFuture.sync();

            //提前发送授权消息
            AuthOperation authOperation = new AuthOperation("admin", "password");
            channelFuture.channel().writeAndFlush(new RequestMessage(IdUtil.nextId(), authOperation));

            //构造消息，如果不想要每次都这样构造消息该怎么办？
            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "tudou"));

            //模拟内存泄漏，发送多次消息
//        for (int i = 0; i < 10000; i++) {
//            channelFuture.channel().writeAndFlush(requestMessage);
//        }
            //发送消息
            channelFuture.channel().writeAndFlush(requestMessage);

            channelFuture.channel().closeFuture().get();
        }finally {
            group.shutdownGracefully();
        }
    }
}
