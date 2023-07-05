package io.netty.example.study.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.auth.AuthOperation;
import io.netty.example.study.common.auth.AuthOperationResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: zhk
 * @description:授权
 * @date: 2023/7/5 15:09
 * @version: 1.0
 */
@Slf4j
@ChannelHandler.Sharable
public class AuthHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage msg) throws Exception {
        try {
            Operation operation = msg.getMessageBody();
            if (operation instanceof AuthOperation) {
                AuthOperation authOperation = AuthOperation.class.cast(operation);
                AuthOperationResult execute = authOperation.execute();
                if (execute.isPassAuth()) {
                    log.info("pass auth");
                } else {
                    // 认证失败，关闭连接
                    log.error("fail to auth");
                    ctx.close();
                }
            } else {
                log.error("expect first msg is auth but is {}", operation.getClass().getName());
                ctx.close();
            }
        }catch (Exception e){
            log.error("exception happen", e);
            ctx.close();
        }
        finally {
            ctx.pipeline().remove(this);
        }
    }
}
