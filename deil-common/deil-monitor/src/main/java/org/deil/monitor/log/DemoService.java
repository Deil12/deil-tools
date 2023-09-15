package org.deil.monitor.log;

import org.deil.monitor.log.annotation.Log;
import org.springframework.stereotype.Service;

/**
 * @PURPOSE 演示 service 层
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Service
public class DemoService {

    @Log(name = "日志切面测试")
    public String test() {
        return "abaaba";
    }

}
