package org.deil.utils.mode.strategy;

import org.deil.utils.mode.factory.PaymentStrategy;

import java.math.BigDecimal;

/**
 *
 *
 * @DATE 2023/09/23
 * @CODE Deil
 */
public class AlipayStrategy implements PaymentStrategy {

    @Override
    public String createOrder(BigDecimal amount) {
        // 调用支付宝创建订单接口的具体实现
        String orderId = "alipay_order_12345"; // 这里假设返回的订单ID
        System.out.println("调用支付宝创建订单接口，支付金额：" + amount.toString());
        return orderId;
    }

    @Override
    public String queryResult(String orderId) {
        // 调用支付宝查询订单结果接口的具体实现
        System.out.println("调用支付宝查询订单结果接口，订单ID：" + orderId);
        return "Paid";
    }
}
