package com.ray.airhockey.program;

import android.content.Context;

import com.ray.airhockey.util.ShaderHelper;
import com.ray.airhockey.util.TextResourceReader;

import static android.opengl.GLES20.glUseProgram;

/***
 *  Author : ryu18356@gmail.com
 *  Create at 2018-09-05 15:40
 *  description : 
 */
public class ShaderProgram {
    //uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    //program
    protected int mProgram;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        mProgram = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(
                        context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(
                        context, fragmentShaderResourceId));
    }

    public void useProgram(){
        glUseProgram(mProgram);
    }

}
