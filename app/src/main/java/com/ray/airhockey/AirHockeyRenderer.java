package com.ray.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.ray.airhockey.object.Mallet;
import com.ray.airhockey.object.Puck;
import com.ray.airhockey.object.Table;
import com.ray.airhockey.program.ColorShaderProgram;
import com.ray.airhockey.program.TextureShaderProgram;
import com.ray.airhockey.util.Geometry;
import com.ray.airhockey.util.MatrixHelper;
import com.ray.airhockey.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static com.ray.airhockey.util.Geometry.Point;
import static com.ray.airhockey.util.Geometry.Ray;
import static com.ray.airhockey.util.Geometry.Sphere;
import static com.ray.airhockey.util.Geometry.vectorBetween;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/31 下午7:32
 * Description :
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    public static final String TAG = "AirHockeyRenderer";

    private final Context mContext;
    private final float[] mViewMatrix = new float[16];
    private final float[] mViewProjectionMatrix = new float[16];
    private final float[] mModelViewProjectionMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];
    private final float[] mInvertedViewProjectionMatrix = new float[16];

    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;
    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;
    private int mTexture;
    private boolean mMalletPressed;
    private Point mBlueMalletPosition;

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
        mBlueMalletPosition = new Point(0f, mMallet.mHeight / 2f, 0.4f);
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
        multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        invertM(mInvertedViewProjectionMatrix, 0, mViewProjectionMatrix, 0);
        //draw the table
        positionTableInScene();
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(mModelViewProjectionMatrix, mTexture);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //draw the mallets
        positionObjectInScene(0f, mMallet.mHeight / 2f, -0.4f);
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 1f, 0f, 0f);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();
        positionObjectInScene(mBlueMalletPosition.x, mBlueMalletPosition.y, mBlueMalletPosition.z);
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 0f, 0f, 1f);
        mMallet.draw();

        //draw the puck
        positionObjectInScene(0f, mPuck.mHeight / 2f, 0f);
        mColorShaderProgram.setUniforms(mModelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorShaderProgram);
        mPuck.draw();
    }

    private void positionTableInScene() {
        setIdentityM(mModelMatrix, 0);
        rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f);
        multiplyMM(mModelViewProjectionMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        setIdentityM(mModelMatrix, 0);
        translateM(mModelMatrix, 0, x, y, z);
        multiplyMM(mModelViewProjectionMatrix, 0, mViewProjectionMatrix, 0, mModelMatrix, 0);
    }

    public void handleTouchPress(float normalizeX, float normalizeY) {

        Ray ray = convertNormalized2DPointToRay(normalizeX, normalizeY);
        Sphere malletBoundingSphere = new Sphere(new Point(
                mBlueMalletPosition.x,
                mBlueMalletPosition.y,
                mBlueMalletPosition.z), mMallet.mHeight / 2f);
        mMalletPressed = Geometry.intersects(malletBoundingSphere, ray);
        if (mMalletPressed) {
            Log.v(TAG, "mallet pressed");
        }
    }

    public void handleTouchDrag(float normalizeX, float normalizeY) {
    }

    private Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY) {

        final float[] nearPointNdc = {normalizedX, normalizedY, -1, 1};
        final float[] farPointNdc =  {normalizedX, normalizedY,  1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        multiplyMV(nearPointWorld, 0, mInvertedViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, mInvertedViewProjectionMatrix, 0, farPointNdc, 0);
        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Point nearPointRay = new Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Point farPointRay = new Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Ray(nearPointRay, vectorBetween(nearPointRay, farPointRay));

    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

}
