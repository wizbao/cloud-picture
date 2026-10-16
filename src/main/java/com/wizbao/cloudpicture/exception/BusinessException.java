package com.wizbao.cloudpicture.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    public final int code;

    /**
     * 根据错误码和错误信息创建业务异常
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 根据错误码枚举创建业务异常
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    /**
     * 根据错误码枚举和自定义信息创建业务异常
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public BusinessException(ErrorCode errorCode,String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
