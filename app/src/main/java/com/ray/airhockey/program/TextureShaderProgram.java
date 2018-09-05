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
public class TextureShaderProgram extends ShaderProgram {
    //uniform locations
    private final int mUMatrixLocation;
    private final int mUTextureUnitLocation;
    //attribute locations
    private final int mAPositionLocation;
    private final int mATextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);
        mUMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        mUTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);
        mAPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        mATextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId) {
        glUniformMatrix4fv(mUMatrixLocation, 1, false, matrix, 0);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureId);
        glUniform1i(mUTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return mAPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return mATextureCoordinatesLocation;
    }
}
