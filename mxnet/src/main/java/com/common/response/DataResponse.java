/*
 * Copyright 2021 Apache All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.common.response;


import com.common.enums.CodeEnum;

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
