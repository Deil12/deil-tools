package org.deil.utils.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//@Data
//@Component
@ConfigurationProperties(prefix = "deil.http")
public class HttpProperties {

    private boolean enabled = true;

    /**
     * 设置最大连接数
     */
    private int maxTotal = 300;

    /**
     * 设置连接超时时间
     */
    private int connectTimeout = 10;

    /**
     * 每个主机地址的并发数
     */
    private int maxPerRoute = 100;

    /**
     * 桶超时
     */
    private int socketTimeout = 3;

    /**
     * 桶复用
     */
    private boolean socketReuseAddress = true;

    private int readTimeout = 3;

    private int writeTimeout = 3;

    private String uri;

    private String api;

}
