package io.netty.example.study.client.handler.dispatcher;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.keepalive.KeepaliveOperation;
import io.netty.example.study.common.keepalive.KeepaliveOperationResult;
import io.netty.example.study.util.IdUtil;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhk
 * @description: 捕获check，发送keepalive消息
 * @date: 2023/7/5 10:35
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class KeepaliveHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt == IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
            log.info("write idle happen. so need to send keepalive to keep connection not closed by server");
            KeepaliveOperation keepaliveOperation = new KeepaliveOperation();
            RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), keepaliveOperation);
            ctx.writeAndFlush(requestMessage);
        }
        super.userEventTriggered(ctx, evt);
    }
}
