package io.netty.example.study.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 生成streamID
 */
public final class IdUtil {

    private static final AtomicLong IDX = new AtomicLong();

    private IdUtil(){
        //no instance
    }

    public static long nextId(){
        return IDX.incrementAndGet();
    }

}
