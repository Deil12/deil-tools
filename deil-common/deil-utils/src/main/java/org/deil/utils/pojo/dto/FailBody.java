package org.deil.utils.pojo.dto;

import org.deil.utils.pojo.vo.ServiceResponseBody;

/**
 *
 *
 * @DATE 2023/04/14
 * @CODE Deil
 * @SINCE 1.0.0
 * @see ServiceResponseBody
 */
public class FailBody extends ServiceResponseBody {

    private FailBody(String logId, int resCode, String resMsg) {
        super(logId, resCode, resMsg);
    }

    public static FailBody errorMsg(String logId, int resCode, String msg) {
        return new FailBody(logId, resCode, msg);
    }

}