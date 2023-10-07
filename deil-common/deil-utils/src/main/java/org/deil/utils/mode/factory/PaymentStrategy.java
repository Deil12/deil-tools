package org.deil.utils.mode.factory;

import java.math.BigDecimal;

public interface PaymentStrategy {
    // 创建支付订单
    String createOrder(BigDecimal amount);

    // 查询支付结果
    String queryResult(String orderId);
}
