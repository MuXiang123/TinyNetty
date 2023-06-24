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
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @author: zhk
 * @description: 客户端
 * @date: 2023/6/22 20:44
 * @version: 1.0
 */
public class Client {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        //调整连接超时
        bootstrap.option(NioChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.option(NioChannelOption.SO_REUSEADDR, true);
        //client不区分childHandler
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        //确保连接成功
        channelFuture.sync();
        //构造消息，如果不想要每次都这样构造消息该怎么办？
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), new OrderOperation(1001, "tudou"));
        //发送消息
        channelFuture.channel().writeAndFlush(requestMessage);

        channelFuture.channel().closeFuture().get();
    }
}
