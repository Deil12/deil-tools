package org.deil.demo;

import lombok.RequiredArgsConstructor;
import org.deil.utils.log.annotation.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DemoController {

    //private final TestMapper testMapper;
    //private final TestService testService;

    @GetMapping("/test")
    public String test() {
        return "";
    }

}
