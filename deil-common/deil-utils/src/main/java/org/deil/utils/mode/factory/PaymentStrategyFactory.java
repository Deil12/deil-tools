package org.deil.utils.mode.factory;

import org.deil.utils.mode.strategy.AlipayStrategy;
import org.deil.utils.mode.strategy.WeChatPayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @DATE 2023/09/23
 * @CODE Deil
 */
@Component
public class PaymentStrategyFactory {

    private final ApplicationContext applicationContext;

    @Autowired
    public PaymentStrategyFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PaymentStrategy getPaymentStrategy(String paymentChannel) {
        switch (paymentChannel) {
            case "alipay":
                return applicationContext.getBean(AlipayStrategy.class);
            case "wechatpay":
                return applicationContext.getBean(WeChatPayStrategy.class);
            default:
                throw new IllegalArgumentException("Invalid payment channel.");
        }
    }

}
