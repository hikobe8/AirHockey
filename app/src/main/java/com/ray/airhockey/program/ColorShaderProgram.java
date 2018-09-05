package com.ray.airhockey.program;

import android.content.Context;

import com.ray.airhockey.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:49
 *  description : 
 */
public class ColorShaderProgram extends ShaderProgram {
    //uniform locations
    private final int mUMatrixLocation;
    //attribute locations
    private final int mAPositionLocation;
    private final int mAColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        mUMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        mAPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        mAColorLocation = glGetAttribLocation(mProgram, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        glUniformMatrix4fv(mUMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionAttributeLocation() {
        return mAPositionLocation;
    }

    public int getColorAttributeLocation() {
        return mAColorLocation;
    }
}
