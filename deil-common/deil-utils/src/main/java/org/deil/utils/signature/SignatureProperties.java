package org.deil.utils.signature;

import lombok.Data;
import org.deil.utils.signature.annotation.UnEnableDecryptRequest;
import org.deil.utils.signature.annotation.UnEnableEncryptResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 签名配置
 *
 * @DATE 2023/04/08
 * @CODE Deil
 */
@Data
@Component
@ConfigurationProperties(prefix = "deil.security.signature")
public class SignatureProperties {

    /**
     * 是否开启
     */
    private boolean enabled = true;
    /**
     * 超时
     */
    private int outOfTime = 3;
    /**
     * 扫描自定义注解
     */
    private Class<? extends Annotation> RespAnnoClass = UnEnableEncryptResponse.class;
    /**
     * 扫描自定义注解
     */
    private Class<? extends Annotation> ReqAnnoClass = UnEnableDecryptRequest.class;

    /**
     * deil.security.signature.appInfo[appId]=appSecret
     */
    private Map<String, String> appInfo = new HashMap<>();

}
