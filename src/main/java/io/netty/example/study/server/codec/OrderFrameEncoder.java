package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author: zhk
 * @description:
 * @date: 2023/6/22 20:09
 * @version: 1.0
 * 为了客户端能够解决粘包和半包问题
 */
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super( 2);
    }
}
