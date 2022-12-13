package org.deil.qurtz.commom;

import lombok.Data;

/**
 * @PURPOSE 结果
 * @DATE 2022/12/13
 * @CODE Deil
 */
@Data
public class Result {

    private int code;

    private String msg;

    private Object retData;

}
