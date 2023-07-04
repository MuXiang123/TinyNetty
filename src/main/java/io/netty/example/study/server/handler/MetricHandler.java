package io.netty.example.study.server.handler;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jmx.JmxReporter;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: zhk
 * @description: 连接数可视化
 * @date: 2023/7/4 20:43
 * @version: 1.0
 */

@ChannelHandler.Sharable
public class MetricHandler extends ChannelDuplexHandler {
    private AtomicLong totalConnectionNumber = new AtomicLong();

    {
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register("totalConnectionNumber", new Gauge<Long>() {
            @Override
            public Long getValue() {
                return totalConnectionNumber.longValue();
            }
        });

        //console 打印
        ConsoleReporter.forRegistry(metricRegistry)
                .build()
                .start(10, java.util.concurrent.TimeUnit.SECONDS);

        //jmx 打印
        JmxReporter.forRegistry(metricRegistry).build().start();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        totalConnectionNumber.incrementAndGet();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        totalConnectionNumber.decrementAndGet();
        super.channelInactive(ctx);
    }
}
