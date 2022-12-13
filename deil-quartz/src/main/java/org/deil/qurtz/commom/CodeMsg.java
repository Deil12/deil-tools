package org.deil.qurtz.commom;

import java.util.HashMap;
import java.util.Map;

/**
 * @PURPOSE 代码味精
 * @DATE 2022/12/13
 * @CODE Deil
 */
public class CodeMsg {

    private static final Map<Integer, String> MSG = new HashMap<Integer, String>();

    //系统
    public static final int SUCCESS = 200;
    public static final int ERROR = 500;

    //任务
    public static final int TASK_NOT_EXITES = 100001;
    public static final int TASK_EXCEPTION = 100002;
    public static final int TASK_CRON_ERROR = 100003;
    public static final int TASK_CRON_DOUBLE = 100004;

    static {
        //系统
        MSG.put(SUCCESS, "请求成功.");
        MSG.put(ERROR, "服务器异常.");

        //任务
        MSG.put(TASK_NOT_EXITES, "定时任务不存在");
        MSG.put(TASK_EXCEPTION, "设置定时任务失败");
        MSG.put(TASK_CRON_ERROR, "表达式有误");
        MSG.put(TASK_CRON_DOUBLE, "定时任务已经存在");
    }

    public static String getMsg(int errcode) {
        return MSG.get(errcode);
    }

}
