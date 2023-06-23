package io.netty.example.study.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.ResponseMessage;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author: zhk
 * @description: 二次解码器，将一次解码过后的信息处理成业务能用的responseMessage对象
 * @date: 2023/6/22 20:12
 * @version: 1.0
 *
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    /**
     * 将bytebuf转化为request msg
     * @param ctx
     * @param byteBuf
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(byteBuf);
//        将msg传递出去
        out.add(responseMessage);
    }
}
