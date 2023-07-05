package io.netty.example.study.client.handler.dispatcher;

import io.netty.example.study.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhk
 * @description:请求处理
 * @date: 2023/6/22 21:49
 * @version: 1.0
 */
public class RequestPendingCenter {
    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();

    /**
     * 发送请求时，将请求中的信息放到map中
     * @param streamId
     * @param future
     */
    public void add(Long streamId, OperationResultFuture future){
        this.map.put(streamId, future);
    }

    /**
     * 根据streamId设置结果
     * @param streamId
     * @param operationResult
     */
    public void set(Long streamId, OperationResult operationResult){
        OperationResultFuture operationResultFuture = this.map.get(streamId);
        if(operationResultFuture != null){
            operationResultFuture.setSuccess(operationResult);
            //防止map无限增长
            this.map.remove(streamId);
        }
    }
}
