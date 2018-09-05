package com.ray.airhockey.data;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;
import static com.ray.airhockey.Constants.BYTES_PER_FLOAT;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:01
 *  description : 
 */
public class VertexArray {

    private final FloatBuffer mFloatBuffer;

    public VertexArray(float[] floatBuffer) {
        mFloatBuffer = ByteBuffer
                .allocateDirect(floatBuffer.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(floatBuffer);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        mFloatBuffer.position(dataOffset);
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, mFloatBuffer);
        glEnableVertexAttribArray(attributeLocation);
        mFloatBuffer.position(0);
    }

}
