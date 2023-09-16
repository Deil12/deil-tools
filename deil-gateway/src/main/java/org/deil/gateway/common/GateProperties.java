package org.deil.gateway.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 网关属性
 *
 * @DATE 2023/06/12
 * @CODE Deil
 * @SINCE 1.0.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "deil.gateway")
public class GateProperties {

    private String externalAppId;

    private String externalApi;

    private String thirdPartyAppId;

    private String thirdPartyPath;

}
