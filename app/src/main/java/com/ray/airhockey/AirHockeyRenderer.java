package com.ray.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ray.airhockey.object.Mallet;
import com.ray.airhockey.object.Puck;
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
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/31 下午7:32
 * Description :
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private final Context mContext;
    private final float[] mViewMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];
    private final float[] mModelViewProjectionMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;
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
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);
        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);
        mTexture = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(mProjectionMatrix, 45f, width * 1.f / height, 1f, 10f);
        setLookAtM(mViewMatrix, 0, 0f, 1.2f, 2.2f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0 );

        //draw the table
        positionTableInScene();
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(mModelViewProjectionMatrix, mTexture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //draw the mallets
        positionObjectInScene(0f, mMallet.mHeight/2f, -0.4f);
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 1f, 0f, 0f);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();
        positionObjectInScene(0f, mMallet.mHeight/2f, 0.4f);
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 0f, 0f, 1f);
        mMallet.draw();

        //draw the puck
        positionObjectInScene(0f, mPuck.mHeight/2f, 0f);
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 0.8f,0.8f, 1f);
        mPuck.bindData(mColorShaderProgram);
        mPuck.draw();
    }

    private void positionTableInScene(){
        setIdentityM(mModelMatrix, 0);
        rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(mModelViewProjectionMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z){
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, x, y, z);
        multiplyMM(mModelViewProjectionMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }

}
