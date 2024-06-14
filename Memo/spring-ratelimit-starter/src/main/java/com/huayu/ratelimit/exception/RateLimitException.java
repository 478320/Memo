package com.huayu.ratelimit.exception;

/**
 * 限流注解自定义异常
 */
public class RateLimitException extends RuntimeException{

    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public RateLimitException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public RateLimitException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
