package org.deil.utils.log;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PURPOSE 
 * @DATE 2022/11/20
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
