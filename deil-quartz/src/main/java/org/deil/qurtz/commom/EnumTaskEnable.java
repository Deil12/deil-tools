package org.deil.qurtz.commom;

/**
 * @PURPOSE enum任务启用
 * @DATE 2022/12/13
 * @CODE Deil
 */
public enum EnumTaskEnable {

    START("1", "开启"),
    STOP("0", "关闭");

    private String code;

    private String msg;

    EnumTaskEnable(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

}
