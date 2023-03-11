package org.deil.demo.controller;

import lombok.RequiredArgsConstructor;
import org.deil.demo.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class TestController {

    //private final TestMapper testMapper;
    private final TestService testService;

    @GetMapping("/test")
    public String test() {
        return "";
    }

}
