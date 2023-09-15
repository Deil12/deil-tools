package org.deil.gateway.scheduling;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 上传日志压缩包到FTP配置类
 */
@Configuration
@ConfigurationProperties(prefix = "tang.log.ftp")
@Getter
@Setter
public class SendLogToFtpConfig {
    private boolean enabled;
    //服务运行日志保存的路径
    private String logFilePath;

    //日志上传文件夹，可用服务器IP表示
    private String logIp;
    private String hostIp;
    private String port;
    private String userName;
    private String password;
    private String path;
}
