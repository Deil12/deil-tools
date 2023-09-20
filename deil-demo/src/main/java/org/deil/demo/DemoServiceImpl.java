package org.deil.demo;

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
    @Override
    public void testPost() {
        new ApacheHttpUtil("www.baidu.com", "", "").httpPost("www.baidu.com", "");
    }

}
