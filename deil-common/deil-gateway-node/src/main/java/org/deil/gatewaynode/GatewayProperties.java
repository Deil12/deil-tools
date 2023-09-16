package org.deil.gatewaynode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "deil.gateway")
public class GatewayProperties {
    private boolean enabled = Boolean.FALSE;
}
