package org.deil.utils.exception;

import org.slf4j.helpers.MessageFormatter;

/**
 * 
 *
 * @DATE 2023/09/23
 * @CODE Deil
 */
public class CustomException extends RuntimeException {

    private final Integer code;

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(Integer code, String message, Object arg) {
        super(MessageFormatter.arrayFormat(message, new Object[]{arg}).getMessage());
        this.code = code;
    }

    public CustomException(Integer code, String message, Object arg1, Object arg2) {
        super(MessageFormatter.arrayFormat(message, new Object[]{arg1, arg2}).getMessage());
        this.code = code;
    }

    public CustomException(Integer code, String message, Object... arg) {
        super(MessageFormatter.arrayFormat(message, arg).getMessage());
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
