package io.netty.example.study.common.order;


import io.netty.example.study.common.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class OrderOperation extends Operation {

    private int tableId;
    private String dish;

    public OrderOperation(int tableId, String dish) {
        this.tableId = tableId;
        this.dish = dish;
    }

    /**
     * 打印日志作为业务处理，假逻辑
     * @return
     */
    @Override
    public OrderOperationResult execute() {
        log.info("order's executing startup with orderRequest: " + toString());
        log.info("order's executing complete");

//        System.out.println("order's executing startup with orderRequest: " + toString());
//        System.out.println("order's executing complete");

        //execute order logic
        OrderOperationResult orderResponse = new OrderOperationResult(tableId, dish, true);
        return orderResponse;
    }
}
