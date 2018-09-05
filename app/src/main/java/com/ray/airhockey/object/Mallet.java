package com.ray.airhockey.object;

import com.ray.airhockey.data.VertexArray;
import com.ray.airhockey.program.ColorShaderProgram;
import com.ray.airhockey.program.TextureShaderProgram;

import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glDrawArrays;
import static com.ray.airhockey.Constants.BYTES_PER_FLOAT;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:18
 *  description : 
 */
public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    private final VertexArray mVertexArray;

    private final float[] VERTEX_DATA = {
            //mallets Order of coordinate : X, Y, R, G, B
            0f, -0.40f, 0f, 0f, 1f,
            0f,  0.40f, 0f, 1f, 0f
    };

    public Mallet() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram textureShaderProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 2);
    }

}
