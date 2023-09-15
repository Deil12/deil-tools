package org.deil.gateway.common;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultType {

    SUCCESS(200,"成功"),
    FAIL(400, "失败"),
    ERROR(999, "服务异常"),
    DENIED(403, "拒绝访问")

    ;

    private Integer code;

    private String message;

    private ResultType(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
