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

    //@Resource
    //private Redisson redisson;

    @Override
    public void test() {
        //RLock lock = redisson.getLock("123456789");
        try {
        //    lock.lock();
            TimeUnit.SECONDS.sleep(50);
        } catch (InterruptedException e){
        }
    }

    //@Async
    @Async("getDeilUtilsExecutor")
    //@Async("getDeilUtilsExecutor")
    //@Retryable(retryTimes = 4, retryInterval = 60)
    @Override
    public void testPost() {
        //int i = 0 / 1;
        new ApacheHttpUtil("www.baidu.com", "", "").httpPost("www.baidu.com", "");
    }

}
