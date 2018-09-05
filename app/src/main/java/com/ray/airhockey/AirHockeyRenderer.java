package com.ray.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ray.airhockey.object.Mallet;
import com.ray.airhockey.object.Table;
import com.ray.airhockey.program.ColorShaderProgram;
import com.ray.airhockey.program.TextureShaderProgram;
import com.ray.airhockey.util.MatrixHelper;
import com.ray.airhockey.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/31 下午7:32
 * Description :
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private final Context mContext;
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private Table mTable;
    private Mallet mMallet;
    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;
    private int mTexture;

    public AirHockeyRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 1f);
        mTable = new Table();
        mMallet = new Mallet();
        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);
        mTexture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(mProjectionMatrix, 45f, width * 1.f / height, 1f, 10f);
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(mModelMatrix, 0, -60f, 1f, 0f, 0f);
        final float[] temp = new float[16];
        multiplyMM(temp, 0, mProjectionMatrix, 0, mModelMatrix, 0);
        System.arraycopy(temp, 0, mProjectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //draw the table
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(mProjectionMatrix, mTexture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //draw the mallets
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(mProjectionMatrix);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();

    }

}
