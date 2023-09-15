package org.deil.gateway.common;

public enum AppAuth {
    /**
     * Tang后台网关
     */
    Tang后台网关("Tang", "R2F0ZVdheV9UYW5nXzIwMjMwNjAxMDAwMDAwMDAw");

    private final String appId;
    private final String appSecret;

    AppAuth(String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
    }

    public String appId() {
        return this.appId;
    }

    public String appSecret() {
        return this.appSecret;
    }

    /**
     * 根据appId获取密钥
     *
     * @param appId
     * @return
     */
    public static String getSecretByAppId(String appId) {
        AppAuth[] values = values();
        for (AppAuth value : values) {
            if (value.appId.equals(appId)) {
                return value.appSecret;
            }
        }
        return "";
    }

    /**
     * 根据appId获取账号信息
     *
     * @param appId
     * @return
     */
    public static AppAuth getByAppId(String appId) {
        for (AppAuth value : values()) {
            if (value.appId.equals(appId)) {
                return value;
            }
        }
        return null;
    }
}
