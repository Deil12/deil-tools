package org.deil.demo;

import com.alibaba.fastjson2.JSONObject;
import org.deil.utils.web.ApacheHttpPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
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

    //@Resource
    //private ApacheHttpPoolUtil apacheHttpPoolUtil;
    //@Async
    @Async("getDeilUtilsExecutor")
    //@Retryable(retryTimes = 4, retryInterval = 60)
    @Override
    public void testHttp() {
        Map<String, String> map = new HashMap<>();
        map.put("flightNo", "OQ2601");
        map.put("flightDate", "2023-10-08");
        map.put("flightDepDate", "2023-10-08");
        map.put("flightDep", "CAN");
        map.put("flightDest", "SHE");
        new ApacheHttpPoolUtil().post("http://localhost:9522/Manifest/sendManifestMail", new JSONObject(map));

        //new ApacheHttpUtil("http://10.79.10.29:8090", "", "").post("/HealthCheck/Check", "");
        //new ApacheHttpPoolUtil("http://10.79.10.29:8090").post("/HealthCheck/Check");
        //new ApacheHttpPoolUtil().post("http://localhost:9522/Manifest/sendManifestMail", "flightNo=CZ44332&flightDate=2023-09-12&flightDepDate=2023-09-12&flightDep=CAN&flightDest=DLC");

        //apacheHttpPoolUtil.post("http://10.79.10.29:8090/HealthCheck/Check", new JSONObject());
        //apacheHttpPoolUtil.post("http://10.79.10.29:8090/HealthCheck/Check", new JSONObject());
    }

}
