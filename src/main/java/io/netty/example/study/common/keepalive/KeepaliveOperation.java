package io.netty.example.study.common.keepalive;


import io.netty.example.study.common.Operation;
import lombok.Data;
import lombok.extern.java.Log;

/**
 * keepalive实现，使用一个时间字段简化实现
 */
@Data
@Log
public class KeepaliveOperation extends Operation {

    private long time ;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public KeepaliveOperationResult execute() {
        KeepaliveOperationResult orderResponse = new KeepaliveOperationResult(time);
        return orderResponse;
    }
}
