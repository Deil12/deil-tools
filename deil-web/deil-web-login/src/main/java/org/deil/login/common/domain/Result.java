package org.deil.login.common.domain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class Result  extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_ERROR = 500;

    public Result() {
    }

    public Result(int code, String msg, Object data) {
        this.setCode(code);
        this.setMsg(msg);
        this.setData(data);
    }

    public Result(Map<String, ?> map) {
        this.setMap(map);
    }

    public Integer getCode() {
        return (Integer)this.get("code");
    }

    public String getMsg() {
        return (String)this.get("msg");
    }

    public Object getData() {
        return this.get("data");
    }

    public Result setCode(int code) {
        this.put("code", code);
        return this;
    }

    public Result setMsg(String msg) {
        this.put("msg", msg);
        return this;
    }

    public Result setData(Object data) {
        this.put("data", data);
        return this;
    }

    public Result set(String key, Object data) {
        this.put(key, data);
        return this;
    }

    public <T> T get(String key, Class<T> cs) {
        return getValueByType(this.get(key), cs);
    }

    public Result setMap(Map<String, ?> map) {
        Iterator var2 = map.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();
            this.put(key, map.get(key));
        }

        return this;
    }

    public static Result ok() {
        return new Result(200, "ok", (Object)null);
    }

    public static Result ok(String msg) {
        return new Result(200, msg, (Object)null);
    }

    public static Result code(int code) {
        return new Result(code, (String)null, (Object)null);
    }

    public static Result data(Object data) {
        return new Result(200, "ok", data);
    }

    public static Result error() {
        return new Result(500, "error", (Object)null);
    }

    public static Result error(String msg) {
        return new Result(500, msg, (Object)null);
    }

    public static Result get(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public String toString() {
        return "{\"code\": " + this.getCode() + ", \"msg\": " + this.transValue(this.getMsg()) + ", \"data\": " + this.transValue(this.getData()) + "}";
    }

    private String transValue(Object value) {
        if (value == null) {
            return null;
        } else {
            return value instanceof String ? "\"" + value + "\"" : String.valueOf(value);
        }
    }

    public static <T> T getValueByType(Object obj, Class<T> cs) {
        if (obj != null && !obj.getClass().equals(cs)) {
            String obj2 = String.valueOf(obj);
            Object obj3;
            if (cs.equals(String.class)) {
                obj3 = obj2;
            } else if (!cs.equals(Integer.TYPE) && !cs.equals(Integer.class)) {
                if (!cs.equals(Long.TYPE) && !cs.equals(Long.class)) {
                    if (!cs.equals(Short.TYPE) && !cs.equals(Short.class)) {
                        if (!cs.equals(Byte.TYPE) && !cs.equals(Byte.class)) {
                            if (!cs.equals(Float.TYPE) && !cs.equals(Float.class)) {
                                if (!cs.equals(Double.TYPE) && !cs.equals(Double.class)) {
                                    if (!cs.equals(Boolean.TYPE) && !cs.equals(Boolean.class)) {
                                        if (!cs.equals(Character.TYPE) && !cs.equals(Character.class)) {
                                            obj3 = obj;
                                        } else {
                                            obj3 = obj2.charAt(0);
                                        }
                                    } else {
                                        obj3 = Boolean.valueOf(obj2);
                                    }
                                } else {
                                    obj3 = Double.valueOf(obj2);
                                }
                            } else {
                                obj3 = Float.valueOf(obj2);
                            }
                        } else {
                            obj3 = Byte.valueOf(obj2);
                        }
                    } else {
                        obj3 = Short.valueOf(obj2);
                    }
                } else {
                    obj3 = Long.valueOf(obj2);
                }
            } else {
                obj3 = Integer.valueOf(obj2);
            }

            return (T) obj3;
        } else {
            return (T) obj;
        }
    }

}
