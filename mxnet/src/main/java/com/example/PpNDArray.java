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
import ai.djl.mxnet.engine.MxNDManager;
import ai.djl.mxnet.jna.NativeResource;
import ai.djl.ndarray.LazyNDArray;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.index.NDIndex;
import ai.djl.ndarray.internal.NDArrayEx;
import ai.djl.ndarray.types.DataType;
import ai.djl.ndarray.types.Shape;
import ai.djl.ndarray.types.SparseFormat;
import com.sun.jna.Pointer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * ClassName: PpNDArray
 * Description: TODD
 * Author: James Zow
 * Date: 2020/11/11 0011 21:26
 * Version:
 **/
public class PpNDArray extends NativeResource implements LazyNDArray {

    private static final int MAX_SIZE = 100;
    private static final int MAX_DEPTH = 10;
    private static final int MAX_ROWS = 10;
    private static final int MAX_COLUMNS = 20;
    private String name;
    private Device device;
    private SparseFormat sparseFormat;
    private DataType dataType;
    private Shape shape;
    private Boolean hasGradient;
    protected MxNDManager manager;

    protected PpNDArray(Pointer pointer) {
        super(pointer);
    }

    @Override
    public void waitToRead() {

    }

    @Override
    public void waitToWrite() {

    }

    @Override
    public void waitAll() {

    }

    @Override
    public NDManager getManager() {
        return this.manager;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public Device getDevice() {
        return null;
    }

    @Override
    public Shape getShape() {
        return null;
    }

    @Override
    public SparseFormat getSparseFormat() {
        return null;
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public boolean isScalar() {
        return false;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public NDManager attach(NDManager ndManager) {
        return null;
    }

    @Override
    public void detach() {

    }

    @Override
    public NDArray toDevice(Device device, boolean b) {
        return null;
    }

    @Override
    public NDArray toType(DataType dataType, boolean b) {
        return null;
    }

    @Override
    public void attachGradient() {

    }

    @Override
    public void attachGradient(SparseFormat sparseFormat) {

    }

    @Override
    public NDArray getGradient() {
        return null;
    }

    @Override
    public boolean hasGradient() {
        return false;
    }

    @Override
    public long size(int axis) {
        return 0;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public double[] toDoubleArray() {
        return new double[0];
    }

    @Override
    public float[] toFloatArray() {
        return new float[0];
    }

    @Override
    public int[] toIntArray() {
        return new int[0];
    }

    @Override
    public long[] toLongArray() {
        return new long[0];
    }

    @Override
    public byte[] toByteArray() {
        return new byte[0];
    }

    @Override
    public int[] toUint8Array() {
        return new int[0];
    }

    @Override
    public boolean[] toBooleanArray() {
        return new boolean[0];
    }

    @Override
    public Number[] toArray() {
        return new Number[0];
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return null;
    }

    @Override
    public void set(Buffer buffer) {

    }

    @Override
    public void set(float[] data) {

    }

    @Override
    public void set(int[] data) {

    }

    @Override
    public void set(double[] data) {

    }

    @Override
    public void set(long[] data) {

    }

    @Override
    public void set(byte[] data) {

    }

    @Override
    public void set(NDIndex index, NDArray value) {

    }

    @Override
    public void set(NDIndex index, Number value) {

    }

    @Override
    public void set(NDIndex index, Function<NDArray, NDArray> function) {

    }

    @Override
    public void setScalar(NDIndex index, Number value) {

    }

    @Override
    public NDArray get(NDIndex index) {
        return null;
    }

    @Override
    public NDArray get(String indices, Object... args) {
        return null;
    }

    @Override
    public NDArray get(long... indices) {
        return null;
    }

    @Override
    public NDArray get(NDArray index) {
        return null;
    }

    @Override
    public NDArray getScalar(long... indices) {
        return null;
    }

    @Override
    public long getLong(long... indices) {
        return 0;
    }

    @Override
    public double getDouble(long... indices) {
        return 0;
    }

    @Override
    public float getFloat(long... indices) {
        return 0;
    }

    @Override
    public int getInt(long... indices) {
        return 0;
    }

    @Override
    public byte getByte(long... indices) {
        return 0;
    }

    @Override
    public int getUint8(long... indices) {
        return 0;
    }

    @Override
    public boolean getBoolean(long... indices) {
        return false;
    }

    @Override
    public void copyTo(NDArray ndArray) {

    }

    @Override
    public NDArray duplicate() {
        return null;
    }

    @Override
    public NDArray booleanMask(NDArray index) {
        return null;
    }

    @Override
    public NDArray booleanMask(NDArray ndArray, int i) {
        return null;
    }

    @Override
    public NDArray sequenceMask(NDArray ndArray, float v) {
        return null;
    }

    @Override
    public NDArray sequenceMask(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray zerosLike() {
        return null;
    }

    @Override
    public NDArray onesLike() {
        return null;
    }

    @Override
    public NDArray like() {
        return null;
    }

    @Override
    public boolean contentEquals(Number number) {
        return false;
    }

    @Override
    public boolean contentEquals(NDArray ndArray) {
        return false;
    }

    @Override
    public boolean shapeEquals(NDArray other) {
        return false;
    }

    @Override
    public boolean allClose(NDArray other) {
        return false;
    }

    @Override
    public boolean allClose(NDArray other, double rtol, double atol, boolean equalNan) {
        return false;
    }

    @Override
    public NDArray eq(Number number) {
        return null;
    }

    @Override
    public NDArray eq(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray neq(Number number) {
        return null;
    }

    @Override
    public NDArray neq(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray gt(Number number) {
        return null;
    }

    @Override
    public NDArray gt(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray gte(Number number) {
        return null;
    }

    @Override
    public NDArray gte(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray lt(Number number) {
        return null;
    }

    @Override
    public NDArray lt(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray lte(Number number) {
        return null;
    }

    @Override
    public NDArray lte(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray add(Number number) {
        return null;
    }

    @Override
    public NDArray add(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray sub(Number number) {
        return null;
    }

    @Override
    public NDArray sub(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray mul(Number number) {
        return null;
    }

    @Override
    public NDArray mul(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray div(Number number) {
        return null;
    }

    @Override
    public NDArray div(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray mod(Number number) {
        return null;
    }

    @Override
    public NDArray mod(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray pow(Number number) {
        return null;
    }

    @Override
    public NDArray pow(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray addi(Number number) {
        return null;
    }

    @Override
    public NDArray addi(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray subi(Number number) {
        return null;
    }

    @Override
    public NDArray subi(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray muli(Number number) {
        return null;
    }

    @Override
    public NDArray muli(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray divi(Number number) {
        return null;
    }

    @Override
    public NDArray divi(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray modi(Number number) {
        return null;
    }

    @Override
    public NDArray modi(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray powi(Number number) {
        return null;
    }

    @Override
    public NDArray powi(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray sign() {
        return null;
    }

    @Override
    public NDArray signi() {
        return null;
    }

    @Override
    public NDArray maximum(Number number) {
        return null;
    }

    @Override
    public NDArray maximum(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray minimum(Number number) {
        return null;
    }

    @Override
    public NDArray minimum(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray neg() {
        return null;
    }

    @Override
    public NDArray negi() {
        return null;
    }

    @Override
    public NDArray abs() {
        return null;
    }

    @Override
    public NDArray square() {
        return null;
    }

    @Override
    public NDArray sqrt() {
        return null;
    }

    @Override
    public NDArray cbrt() {
        return null;
    }

    @Override
    public NDArray floor() {
        return null;
    }

    @Override
    public NDArray ceil() {
        return null;
    }

    @Override
    public NDArray round() {
        return null;
    }

    @Override
    public NDArray trunc() {
        return null;
    }

    @Override
    public NDArray exp() {
        return null;
    }

    @Override
    public NDArray log() {
        return null;
    }

    @Override
    public NDArray log10() {
        return null;
    }

    @Override
    public NDArray log2() {
        return null;
    }

    @Override
    public NDArray sin() {
        return null;
    }

    @Override
    public NDArray cos() {
        return null;
    }

    @Override
    public NDArray tan() {
        return null;
    }

    @Override
    public NDArray asin() {
        return null;
    }

    @Override
    public NDArray acos() {
        return null;
    }

    @Override
    public NDArray atan() {
        return null;
    }

    @Override
    public NDArray sinh() {
        return null;
    }

    @Override
    public NDArray cosh() {
        return null;
    }

    @Override
    public NDArray tanh() {
        return null;
    }

    @Override
    public NDArray asinh() {
        return null;
    }

    @Override
    public NDArray acosh() {
        return null;
    }

    @Override
    public NDArray atanh() {
        return null;
    }

    @Override
    public NDArray toDegrees() {
        return null;
    }

    @Override
    public NDArray toRadians() {
        return null;
    }

    @Override
    public NDArray max() {
        return null;
    }

    @Override
    public NDArray max(int[] axes) {
        return null;
    }

    @Override
    public NDArray max(int[] ints, boolean b) {
        return null;
    }

    @Override
    public NDArray min() {
        return null;
    }

    @Override
    public NDArray min(int[] axes) {
        return null;
    }

    @Override
    public NDArray min(int[] ints, boolean b) {
        return null;
    }

    @Override
    public NDArray sum() {
        return null;
    }

    @Override
    public NDArray sum(int[] axes) {
        return null;
    }

    @Override
    public NDArray sum(int[] ints, boolean b) {
        return null;
    }

    @Override
    public NDArray prod() {
        return null;
    }

    @Override
    public NDArray prod(int[] axes) {
        return null;
    }

    @Override
    public NDArray prod(int[] ints, boolean b) {
        return null;
    }

    @Override
    public NDArray mean() {
        return null;
    }

    @Override
    public NDArray mean(int[] axes) {
        return null;
    }

    @Override
    public NDArray mean(int[] ints, boolean b) {
        return null;
    }

    @Override
    public NDArray trace() {
        return null;
    }

    @Override
    public NDArray trace(int offset) {
        return null;
    }

    @Override
    public NDArray trace(int i, int i1, int i2) {
        return null;
    }

    @Override
    public NDList split(long sections) {
        return null;
    }

    @Override
    public NDList split(long[] indices) {
        return null;
    }

    @Override
    public NDList split(long sections, int axis) {
        return null;
    }

    @Override
    public NDList split(long[] longs, int i) {
        return null;
    }

    @Override
    public NDArray flatten() {
        return null;
    }

    @Override
    public NDArray reshape(long... newShape) {
        return null;
    }

    @Override
    public NDArray reshape(Shape shape) {
        return null;
    }

    @Override
    public NDArray expandDims(int i) {
        return null;
    }

    @Override
    public NDArray squeeze() {
        return null;
    }

    @Override
    public NDArray squeeze(int axis) {
        return null;
    }

    @Override
    public NDArray squeeze(int[] ints) {
        return null;
    }

    @Override
    public NDArray stack(NDArray array) {
        return null;
    }

    @Override
    public NDArray stack(NDArray array, int axis) {
        return null;
    }

    @Override
    public NDArray concat(NDArray array) {
        return null;
    }

    @Override
    public NDArray concat(NDArray array, int axis) {
        return null;
    }

    @Override
    public NDArray logicalAnd(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray logicalOr(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray logicalXor(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray logicalNot() {
        return null;
    }

    @Override
    public NDArray argSort() {
        return null;
    }

    @Override
    public NDArray argSort(int axis) {
        return null;
    }

    @Override
    public NDArray argSort(int i, boolean b) {
        return null;
    }

    @Override
    public NDArray sort() {
        return null;
    }

    @Override
    public NDArray sort(int i) {
        return null;
    }

    @Override
    public NDArray softmax(int i) {
        return null;
    }

    @Override
    public NDArray logSoftmax(int i) {
        return null;
    }

    @Override
    public NDArray cumSum() {
        return null;
    }

    @Override
    public NDArray cumSum(int i) {
        return null;
    }

    @Override
    public NDArray isInfinite() {
        return null;
    }

    @Override
    public NDArray isNaN() {
        return null;
    }

    @Override
    public NDArray tile(long l) {
        return null;
    }

    @Override
    public NDArray tile(int i, long l) {
        return null;
    }

    @Override
    public NDArray tile(long[] longs) {
        return null;
    }

    @Override
    public NDArray tile(Shape shape) {
        return null;
    }

    @Override
    public NDArray repeat(long l) {
        return null;
    }

    @Override
    public NDArray repeat(int i, long l) {
        return null;
    }

    @Override
    public NDArray repeat(long[] longs) {
        return null;
    }

    @Override
    public NDArray repeat(Shape shape) {
        return null;
    }

    @Override
    public NDArray dot(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray matMul(NDArray ndArray) {
        return null;
    }

    @Override
    public NDArray clip(Number number, Number number1) {
        return null;
    }

    @Override
    public NDArray swapAxes(int axis1, int axis2) {
        return null;
    }

    @Override
    public NDArray flip(int... ints) {
        return null;
    }

    @Override
    public NDArray transpose() {
        return null;
    }

    @Override
    public NDArray transpose(int... ints) {
        return null;
    }

    @Override
    public NDArray broadcast(Shape shape) {
        return null;
    }

    @Override
    public NDArray broadcast(long... shape) {
        return null;
    }

    @Override
    public NDArray argMax() {
        return null;
    }

    @Override
    public NDArray argMax(int i) {
        return null;
    }

    @Override
    public NDArray argMin() {
        return null;
    }

    @Override
    public NDArray argMin(int i) {
        return null;
    }

    @Override
    public NDArray percentile(Number number) {
        return null;
    }

    @Override
    public NDArray percentile(Number number, int[] ints) {
        return null;
    }

    @Override
    public NDArray median() {
        return null;
    }

    @Override
    public NDArray median(int[] ints) {
        return null;
    }

    @Override
    public NDArray toDense() {
        return null;
    }

    @Override
    public NDArray toSparse(SparseFormat sparseFormat) {
        return null;
    }

    @Override
    public NDArray nonzero() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public NDArray all() {
        return null;
    }

    @Override
    public NDArray any() {
        return null;
    }

    @Override
    public NDArray none() {
        return null;
    }

    @Override
    public NDArray countNonzero() {
        return null;
    }

    @Override
    public NDArray countNonzero(int axis) {
        return null;
    }

    @Override
    public NDArray erfinv() {
        return null;
    }

    @Override
    public NDArrayEx getNDArrayInternal() {
        return null;
    }

    @Override
    public String toDebugString(int maxSize, int maxDepth, int maxRows, int maxColumns) {
        return null;
    }
}
