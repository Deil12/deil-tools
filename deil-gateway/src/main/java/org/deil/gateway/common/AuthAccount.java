package org.deil.gateway.common;


import lombok.Data;

import java.util.List;

/**
 * 认证账号
 */
@Data
public class AuthAccount {
    private String appid;

    private String appSecret;

    /**
     * 账号类型(1.内部账号 2.外部账号)
     */
    private int accountType;

    private List<String> listType;

    private List<String> whiteList;

    private List<String> blackList;
}
