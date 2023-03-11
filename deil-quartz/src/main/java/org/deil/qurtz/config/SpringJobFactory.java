package org.deil.qurtz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 解决spring bean注入Job的问题
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Component
public class SpringJobFactory extends AdaptableJobFactory {

    private final AutowireCapableBeanFactory autowireCapableBeanFactory;
    public SpringJobFactory(
            AutowireCapableBeanFactory autowireCapableBeanFactory
    ) {
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

}
