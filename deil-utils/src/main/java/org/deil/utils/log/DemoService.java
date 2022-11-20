package org.deil.utils.log;

import org.deil.utils.log.annotation.Log;
import org.springframework.stereotype.Service;

/**
 * @PURPOSE 
 * @DATE 2022/11/20
 */
@Service
public class DemoService {

    @Log(name = "日志切面测试")
    public String test() {
        return "abaaba";
    }

}
