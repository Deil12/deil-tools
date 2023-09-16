package org.deil.gateway.common;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GatewayResult<T> {

    private String rerult;

    private T data;

}
