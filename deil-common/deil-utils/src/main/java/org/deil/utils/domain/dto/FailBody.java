package org.deil.utils.domain.dto;

import org.deil.utils.domain.vo.ServiceRespBody;

/**
 *
 *
 * @DATE 2023/04/14
 * @CODE Deil
 * @SINCE 1.0.0
 * @see ServiceRespBody
 */
public class FailBody extends ServiceRespBody {

    private FailBody(String logId, int resCode, String resMsg) {
        super(logId, resCode, resMsg);
    }

    public static FailBody errorMsg(String logId, int resCode, String msg) {
        return new FailBody(logId, resCode, msg);
    }

}
