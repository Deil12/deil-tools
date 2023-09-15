package org.deil.monitor.log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PURPOSE 演示 controller 层
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/demo")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok(demoService.test());
    }

}
