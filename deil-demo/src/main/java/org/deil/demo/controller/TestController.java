//package org.deil.demo.controller;
//
//import org.deil.demo.dao.TestMapper;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @PURPOSE
// * @DATE 2022/11/29
// */
//@RestController
//@RequestMapping
//public class TestController {
//
//    private final TestMapper testMapper;
//    public TestController(
//            TestMapper testMapper
//    ) {
//        this.testMapper = testMapper;
//    }
//
//    @GetMapping("/test")
//    public String test() {
//        return testMapper.test();
//    }
//
//}
