package com.fastdjl.common.response;


import com.fastdjl.common.enums.CodeEnum;

public class DataResponse<T> extends BaseResponse {

    private T data;

    private DataResponse() {}

    private DataResponse(CodeEnum code, T data) {
        super(code);
        this.data = data;
    }

    public static <T> DataResponse<T> response(CodeEnum code, T data) {
        return new DataResponse<T>(code, data);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
