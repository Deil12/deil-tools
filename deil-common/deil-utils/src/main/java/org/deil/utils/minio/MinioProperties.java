package org.deil.utils.minio;

import io.minio.MinioClient;
import lombok.Data;
import org.deil.utils.signature.UnEnableDecryptRequest;
import org.deil.utils.signature.UnEnableEncryptResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

@Data
//@Component
@Configuration
@ConfigurationProperties(prefix = "deil.minio")
public class MinioProperties {

    private boolean enabled = true;

    private String url = "werty";

    private String accessKey = "sdfghj";

    private String secretKey = "sdfgh";

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().endpoint(url).credentials(accessKey, secretKey).build();
    }

}
