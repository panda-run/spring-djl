package com.fastdjl.common.enums;

/**
 * James Zow
 * 响应状态码枚举说明
 */
public enum CodeEnum {

    // 基本状态码
    SUCCESS(0, "成功！"),
    ERROR(1, "失败，未知错误！"),;

    /**
     * 响应状态码
     */
    private final int code;

    /**
     * 响应提示
     */
    private final String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
