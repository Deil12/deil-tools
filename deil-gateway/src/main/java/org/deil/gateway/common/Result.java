package org.deil.gateway.common;

import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

public class Result<T> extends ServiceRespBody {
    private final T data;

    private Result(String logId, Integer code, String message) {
        super(logId, code, message);
        this.data = null;
    }

    private Result(String logId, Integer code, String message, T data) {
        super(logId, code, message);
        this.data = data;
    }

    public static Result<Object> OK(String logId) {
        return new Result(logId, ResultType.SUCCESS.getCode(), ResultType.SUCCESS.getMessage());
    }

    public static Result<Object> OK(String logId, @Nullable Object data) {
        return new Result(logId, ResultType.SUCCESS.getCode(), ResultType.SUCCESS.getMessage(), data);
    }

    public static Result<Object> FAIL(String logId) {
        return new Result(logId, ResultType.FAIL.getCode(), ResultType.FAIL.getMessage());
    }

    public static Result<Object> FAIL(String logId, @Nullable String msg) {
        return new Result(logId, ResultType.FAIL.getCode(), msg);
    }

    public static Result<Object> FAIL(String logId, @Nullable Object data) {
        return new Result(logId, ResultType.FAIL.getCode(), ResultType.FAIL.getMessage(), data);
    }

    public T getData() {
        return this.data;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Result)) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            Result<?> that = (Result)o;
            return Objects.equals(this.data, that.data);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{super.hashCode(), this.data});
    }

    public String toString() {
        return (new StringJoiner(", ", Result.class.getSimpleName() + "[", "]")).add("data=" + this.data).toString();
    }
}
