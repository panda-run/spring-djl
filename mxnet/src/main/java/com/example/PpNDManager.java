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
package com.example;

import ai.djl.Device;
import ai.djl.engine.Engine;
import ai.djl.mxnet.jna.JnaUtils;
import ai.djl.ndarray.BaseNDManager;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.util.PairList;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * ClassName: PpNDManager
 * Description: TODD
 * Author: James Zow
 * Date: 2020/11/11 0011 21:29
 * Version:
 **/
public class PpNDManager extends BaseNDManager {



    private static final PpNDManager SYSTEM_MANAGER = new SystemManager();
    private static final NDArray[] EMPTY = new NDArray[0];
    private int version;

    protected PpNDManager(NDManager parent, Device defaultDevice, int version, Device device) {
        super(parent, device);
    }

    @Override
    public ByteBuffer allocateDirect(int i) {
        return null;
    }

    @Override
    public NDArray create(String s) {
        return null;
    }

    @Override
    public NDArray create(Shape shape, DataType dataType) {
        return null;
    }

    @Override
    public NDArray createCSR(Buffer buffer, long[] longs, long[] longs1, Shape shape) {
        return null;
    }

    @Override
    public NDArray createRowSparse(Buffer buffer, Shape shape, long[] longs, Shape shape1) {
        return null;
    }

    @Override
    public NDList load(Path path) {
        return null;
    }

    @Override
    public NDArray zeros(Shape shape, DataType dataType) {
        return null;
    }

    @Override
    public NDArray ones(Shape shape, DataType dataType) {
        return null;
    }

    @Override
    public NDArray full(Shape shape, float v, DataType dataType) {
        return null;
    }

    @Override
    public NDArray arange(float v, float v1, float v2, DataType dataType) {
        return null;
    }

    @Override
    public NDArray eye(int i, int i1, int i2, DataType dataType) {
        return null;
    }

    @Override
    public NDArray linspace(float v, float v1, int i, boolean b) {
        return null;
    }

    @Override
    public NDArray randomUniform(float v, float v1, Shape shape, DataType dataType) {
        return null;
    }

    @Override
    public NDArray randomNormal(float loc, float scale, Shape shape, DataType dataType) {
        PpOpParams params = new PpOpParams();
        params.addParam("loc", loc);
        params.addParam("scale", scale);
        params.addParam("size", shape);
        params.setDevice(this.device);
        params.setDataType(dataType);
        return this.invoke("_npi_normal", params);

    }

    @Override
    public NDArray randomMultinomial(int i, NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray randomMultinomial(int i, NDArray ndArray, Shape shape) {
        return null;
    }

    @Override
    public NDManager newSubManager() {
        return null;
    }

    @Override
    public NDManager newSubManager(Device device) {
        return null;
    }

    @Override
    public void invoke(String s, NDArray[] ndArrays, NDArray[] ndArrays1, PairList<String, ?> pairList) {
    }

    @Override
    public NDList invoke(String operation, NDList src, PairList<String, ?> params) {
        return new NDList(JnaUtils.op(operation).invoke(this, (NDArray[])src.toArray(EMPTY), params));
    }

    public NDArray invoke(String operation, NDArray src, PairList<String, ?> params) {
        return this.invoke(operation, new NDArray[]{src}, params);
    }

    private NDArray invoke(String operation, NDArray[] ndArrays, PairList<String,?> params) {
        return null;
    }

    public NDArray invoke(String operation, PairList<String, ?> params) {
        return this.invoke(operation, EMPTY, params);
    }

    @Override
    public Engine getEngine() {
        return null;
    }


    private static final class SystemManager extends PpNDManager {
        SystemManager() {
            super((NDManager)null, Device.defaultDevice(), JnaUtils.getVersion(), null);
        }

        public void attach(String resourceId, AutoCloseable resource) {
        }

        public void detach(String resourceId) {
        }

        public void close() {
        }
    }
}
