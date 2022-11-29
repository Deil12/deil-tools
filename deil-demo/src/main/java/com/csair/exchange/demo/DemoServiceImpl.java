package com.csair.exchange.demo;

import com.csair.exchange.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @PURPOSE 
 * @DATE 2022/11/29
 * @COPYRIGHT © csair.com 
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Resource
    private DemoMapper demoMapper;
    @Override
    public String demo() {
        return demoMapper.demo();
    }

}
