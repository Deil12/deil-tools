package com.csair.exchange.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @PURPOSE 
 * @DATE 2022/11/29
 * @COPYRIGHT © csair.com 
 */
@RestController
public class DemoController {

    @Resource
    private DemoService demoService;

    @GetMapping("/demo")
    public String demo() {
        return demoService.demo();
    }

}
