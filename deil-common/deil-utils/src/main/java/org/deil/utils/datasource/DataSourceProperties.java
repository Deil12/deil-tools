package org.deil.utils.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource.resources")
public class DataSourceProperties {

    private boolean enabled;

    private String dao;

    private String mapper;

}
