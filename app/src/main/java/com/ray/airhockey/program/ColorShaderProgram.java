package com.ray.airhockey.program;

import android.content.Context;

import com.ray.airhockey.R;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:49
 *  description : 
 */
public class ColorShaderProgram extends ShaderProgram {
    //uniform locations
    private final int mUMatrixLocation;
    private final int mUColorLocation;
    //attribute locations
    private final int mAPositionLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        mUMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        mUColorLocation = glGetUniformLocation(mProgram, U_COLOR);
        mAPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
    }

    public void setUniforms(float[] matrix, float r, float g, float b) {
        glUniformMatrix4fv(mUMatrixLocation, 1, false, matrix, 0);
        glUniform4f(mUColorLocation, r, g, b, 1f);
    }

    public int getPositionAttributeLocation() {
        return mAPositionLocation;
    }

}
