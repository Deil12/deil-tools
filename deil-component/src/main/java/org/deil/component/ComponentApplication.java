package org.deil.component;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @PURPOSE 组件应用程序
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@SpringBootApplication
@EnableEncryptableProperties
public class ComponentApplication {

    public static void main(String[] args) {
        if (0 < args.length) {
            PSW = args[0].split("=")[1];
        }
        SpringApplication.run(ComponentApplication.class, args);
    }

    private static String PSW;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        config.setPassword("password");
        config.setPassword(PSW);
//        config.setAlgorithm("PBEWithMD5AndDES");//默认配置
//        config.setKeyObtentionIterations("1000");//默认配置
        config.setPoolSize("4");
//        config.setProviderName("SunJCE");//默认配置
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");//默认配置
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
//        config.setStringOutputType("base64");//默认配置
        encryptor.setConfig(config);
        return encryptor;
    }

}
