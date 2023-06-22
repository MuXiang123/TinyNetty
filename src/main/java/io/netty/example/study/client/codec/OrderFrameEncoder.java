package io.netty.example.study.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @author: zhk
 * @description:为了让服务器端处理粘包和半包问题
 * @date: 2023/6/22 20:09
 * @version: 1.0
 */
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super( 2);
    }
}
