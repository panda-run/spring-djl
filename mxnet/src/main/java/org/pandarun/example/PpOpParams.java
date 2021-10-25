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
package org.pandarun.example;

import ai.djl.Device;
import ai.djl.mxnet.engine.MxDataType;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.ndarray.types.SparseFormat;
import ai.djl.util.PairList;

/**
 * ClassName: PpOpParams
 * Description: TODD
 * Author: James Zow
 * Date: 2020/11/11 21:37
 * Version:
 **/
public class PpOpParams extends PairList<String, Object> {

    private static final String PaddlePaddle_CPU = "cpu(0)";

    public PpOpParams() {
    }

    public void setShape(Shape shape) {
        this.addParam("shape", shape);
    }

    public void setDevice(Device device) {
        this.setParam("ctx", "cpu".equals(device.getDeviceType()) ? "cpu(0)" : device.toString());
    }

    public void setDataType(DataType dataType) {
        if (dataType != null) {
            this.setParam("dtype", MxDataType.toMx(dataType));
        }

    }

    public void setSparseFormat(SparseFormat sparseFormat) {
        if (sparseFormat != null) {
            this.setParam("stype", String.valueOf(sparseFormat.getValue()));
        }

    }

    public void setParam(String paramName, String value) {
        this.remove(paramName);
        this.add(paramName, value);
    }

    public void addParam(String paramName, Shape shape) {
        if (shape != null) {
            this.add(paramName, shape.toString());
        }

    }

    public void addParam(String paramName, String value) {
        this.add(paramName, value);
    }

    public void addParam(String paramName, int value) {
        this.add(paramName, String.valueOf(value));
    }

    public void addParam(String paramName, long value) {
        this.add(paramName, String.valueOf(value));
    }

    public void addParam(String paramName, double value) {
        this.add(paramName, String.valueOf(value));
    }

    public void addParam(String paramName, float value) {
        this.add(paramName, String.valueOf(value));
    }

    public void addParam(String paramName, boolean value) {
        this.add(paramName, value ? "True" : "False");
    }

    public void addParam(String paramName, Number value) {
        this.add(paramName, String.valueOf(value));
    }

    public void addTupleParam(String paramName, int... tuple) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        for(int i = 0; i < tuple.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(tuple[i]);
        }

        sb.append(')');
        this.add(paramName, sb.toString());
    }

    public void addTupleParam(String paramName, long... tuple) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        for(int i = 0; i < tuple.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(tuple[i]);
        }

        sb.append(')');
        this.add(paramName, sb.toString());
    }

    public void addTupleParam(String paramName, float... tuple) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');

        for(int i = 0; i < tuple.length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(tuple[i]);
        }

        sb.append(')');
        this.add(paramName, sb.toString());
    }
}
