package org.deil.utils.web;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @PURPOSE 属性
 * @DATE 2022/09/06
 */
@Data
@Component
@ConfigurationProperties(prefix = "deil.access")
public class AccessProperties {

    private boolean enabled = true;

    private String whiteIPs;

    //private List<String> whiteIP;

    private String blackIPs;

    //private List<String> blackIP;

}
