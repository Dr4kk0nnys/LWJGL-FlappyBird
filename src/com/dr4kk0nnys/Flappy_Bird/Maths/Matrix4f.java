package com.dr4kk0nnys.Flappy_Bird.Maths;

import com.dr4kk0nnys.Flappy_Bird.Utils.BufferUtils;

import java.nio.FloatBuffer;

public class Matrix4f {
    private static final int SIZE = 4 * 4;

    public float[] elements = new float[SIZE];

    public Matrix4f() {

    }

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < SIZE; i++) {
            result.elements[i] = 0.0f;
        }

        result.elements[0] = 1f;
        result.elements[1 + 4] = 1f;
        result.elements[2 + 2 * 4] = 1f;
        result.elements[3 + 3 * 4] = 1f;

        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = identity();

        result.elements[0] = 2f / (right - left);
        result.elements[1 + 4] = 2f / (top - bottom);
        result.elements[2 + 2 * 4] = 2f / (near - far);

        result.elements[3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);

        return result;
    }

    public static Matrix4f translate(Vector3f vector) {
        Matrix4f result = identity();

        result.elements[3 * 4] = vector.x;
        result.elements[1 + 3 * 4] = vector.y;
        result.elements[2 + 3 * 4] = vector.z;

        return result;
    }

    public static Matrix4f rotate(float angle) {
        Matrix4f result = identity();

        float r = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(r);
        float sin = (float) Math.sin(r);

        result.elements[0] = cos;
        result.elements[1] = sin;

        result.elements[4] = -sin;
        result.elements[1 + 4] = cos;

        return result;
    }

    public Matrix4f multiply(Matrix4f matrix) {
        Matrix4f result = new Matrix4f();

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                float sum = 0f;
                for(int k = 0; k < 4; k++) {
                    // i == y   j == x   k == e
                    sum += this.elements[j + k * 4] * matrix.elements[k + i * 4];
                }
                result.elements[j + i * 4] = sum;
            }
        }

        return result;
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }
}
