package io.netty.example.study.client.handler.dispatcher;

import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author: zhk
 * @description: 检测客户端是否空闲
 * @date: 2023/7/5 10:32
 * @version: 1.0
 */
public class ClientIdleCheckHandler extends IdleStateHandler {
    public ClientIdleCheckHandler() {
        super(0, 5, 0);
    }
}
