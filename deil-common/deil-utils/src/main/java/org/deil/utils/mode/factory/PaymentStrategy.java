package org.deil.utils.mode.factory;

import java.math.BigDecimal;

/**
 * 
 *
 * @DATE 2023/09/23
 * @CODE Deil
 */
public interface PaymentStrategy {
    // 创建支付订单
    String createOrder(BigDecimal amount);

    // 查询支付结果
    String queryResult(String orderId);
}
