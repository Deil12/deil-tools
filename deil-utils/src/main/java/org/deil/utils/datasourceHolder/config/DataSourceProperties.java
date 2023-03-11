package org.deil.utils.datasourceHolder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 属性
 * @DATE 2022/09/06
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.resources")
public class DataSourceProperties {

    private boolean enabled;

    private String dao;

    private String mapper;

}
