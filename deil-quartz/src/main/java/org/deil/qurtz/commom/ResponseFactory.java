package org.deil.qurtz.commom;

/**
 * @PURPOSE 工厂反应
 * @DATE 2022/12/13
 * @CODE Deil
 */
public class ResponseFactory {

    private static Result commonBuild(int code, String errmsg) {
        Result result = new Result();
        result.setCode(code);
        if (errmsg == null || errmsg.trim().length() == 0) {
            result.setMsg(CodeMsg.getMsg(code));
        } else {
            result.setMsg(errmsg);
        }
        return result;
    }

    public static Result build(int code) {
        return commonBuild(code, CodeMsg.getMsg(code));
    }

    public static Result build() {
        return commonBuild(CodeMsg.SUCCESS, null);
    }

    public static Result build(Object data) {
        Result json = commonBuild(CodeMsg.SUCCESS, null);
        json.setRetData(data);
        return json;
    }

}
