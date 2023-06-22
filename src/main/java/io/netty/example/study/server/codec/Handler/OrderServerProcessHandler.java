package io.netty.example.study.server.codec.Handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.example.study.common.Operation;
import io.netty.example.study.common.OperationResult;
import io.netty.example.study.common.RequestMessage;
import io.netty.example.study.common.ResponseMessage;

/**
 * @author: zhk
 * @description: 处理二次编码后的msg
 * @date: 2023/6/22 20:26
 * @version: 1.0
 *
 * 为什么不直接继承ChannelInboundHandlerAdapter？
 * 因为SimpleChannelInboundHandler会帮我们释放bytebuf
 * if (this.autoRelease && release) {
 *    ReferenceCountUtil.release(msg);
 * }
 */
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        //没有out参数了，所以需要自己写出去
        ctx.writeAndFlush(requestMessage);
    }
}
