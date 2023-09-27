package org.deil.demo;

import org.deil.utils.retry.Retryable;
import org.deil.utils.web.ApacheHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class DemoServiceImpl implements DemoService {
    private Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public void test() {
        try {
            TimeUnit.SECONDS.sleep(50);
        } catch (InterruptedException e){
        }
    }

    //@Async
    //@Async("getAsyncExecutor")
    @Async("getLogTraceExecutor")
    //@Retryable(retryTimes = 4, retryInterval = 60)
    @Override
    public void testPost() {
        //int i = 0 / 1;
        new ApacheHttpUtil("www.baidu.com", "", "").httpPost("www.baidu.com", "");
    }

}
