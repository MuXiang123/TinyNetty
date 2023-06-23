package io.netty.example.study.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.client.codec.*;
import io.netty.example.study.client.codec.dispatcher.OperationResultFuture;
import io.netty.example.study.client.codec.dispatcher.RequestPendingCenter;
import io.netty.example.study.client.codec.dispatcher.ResponseDispatcherHandler;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.order.OrderOperation;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @author: zhk
 * @description: 客户端v2，引入响应分发机制
 * @date: 2023/6/22 20:44
 * @version: 1.0
 */
public class ClientV2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        bootstrap.group(new NioEventLoopGroup());

        RequestPendingCenter requestPendingCenter = new RequestPendingCenter();

        //client不区分childHandler
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new OrderFrameDecoder());
                pipeline.addLast(new OrderFrameEncoder());
                pipeline.addLast(new OrderProtocolEncoder());
                pipeline.addLast(new OrderProtocolDecoder());

                pipeline.addLast(new ResponseDispatcherHandler(requestPendingCenter));

                pipeline.addLast(new OperationToRequestMessageEncoder());

                pipeline.addLast(new LoggingHandler(LogLevel.INFO));
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8090);
        //确保连接成功
        channelFuture.sync();
        long streamId = IdUtil.nextId();
        //构造消息
        RequestMessage requestMessage = new RequestMessage(streamId, new OrderOperation(1001, "tudou"));
        //在发送信息的时候将id和future对应到map上
        OperationResultFuture operationResultFuture = new OperationResultFuture();
        //先增加
        requestPendingCenter.add(streamId, operationResultFuture);
        //发送消息
        channelFuture.channel().writeAndFlush(requestMessage);
        //阻塞等待结果
        OperationResult operationResult = operationResultFuture.get();

        System.out.println(operationResult);

        channelFuture.channel().closeFuture().get();
    }
}
