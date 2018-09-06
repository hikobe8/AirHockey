package com.ray.airhockey.object;

import com.ray.airhockey.data.VertexArray;
import com.ray.airhockey.program.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.ray.airhockey.Constants.BYTES_PER_FLOAT;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:18
 *  description : 
 */
public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray mVertexArray;

    private final float[] VERTEX_DATA = {
            //Triangle fan. Order of coordinates : X, Y, S, T
            0f,       0f, 0.5f, 0.5f,
            -0.5f, -0.8f,   0f, 0.9f,
            0.5f,  -0.8f,   1f, 0.9f,
            0.5f,   0.8f,   1f, 0.1f,
            -0.5f,  0.8f,   0f, 0.1f,
            -0.5f, -0.8f,   0f, 0.9f,
    };

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureShaderProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureShaderProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }

}
