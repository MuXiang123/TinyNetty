package io.netty.example.study.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author: zhk
 * @description: 处理粘包和半包问题 一次解码器
 * @date: 2023/6/22 20:09
 * @version: 1.0
 *
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
