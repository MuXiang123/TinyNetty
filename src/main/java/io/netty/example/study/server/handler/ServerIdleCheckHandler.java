package io.netty.example.study.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author: zhk
 * @description:
 * @date: 2023/7/5 10:24
 * @version: 1.0
 */
@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {
    public ServerIdleCheckHandler() {
        super(10, 0 , 0, TimeUnit.SECONDS);
    }

    /**
     * 关闭连接
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        if(evt == IdleStateEvent.FIRST_READER_IDLE_STATE_EVENT){
            log.info("idle check happen, so close the connection");
            ctx.close();
            return;
        }
        super.channelIdle(ctx, evt);
    }
}
