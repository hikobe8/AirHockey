package com.ray.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.ray.airhockey.util.LoggerConfig;
import com.ray.airhockey.util.ShaderHelper;
import com.ray.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Author : hikobe8@github.com
 * Time : 2018/8/31 下午7:32
 * Description :
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT)*BYTES_PER_FLOAT;
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";


    private int mAColorLocation;
    private int mAPositionLocation;
    private FloatBuffer mVertexData;

    private static final float[] TABLE_VERTICES = {
            //border fan
            0f, 0f, 1f, 1f, 1f,
            -0.52f, -0.52f, 0f, 0f, 0.8f,
            0.52f, -0.52f, 0f, 0f, 0.8f,
            0.52f, 0.52f, 0f, 0f, 0.8f,
            -0.52f, 0.52f, 0f, 0f, 0.8f,
            -0.52f, -0.52f, 0f, 0f, 0.8f,
            //Triangle fan
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0.7f, 0.7f, 0.7f,
            //line
            -0.5f, 0f, 1.0f, 0f, 0f,
            0.5f, 0f, 1.0f, 0f, 0f,
            //mallets
            0f, 0.25f, 0f, 0f, 1f,
            0f, -0.25f, 0f, 1f, 0f
    };

    private Context mContext;
    private int mProgram;

    public AirHockeyRenderer(Context context) {
        mContext = context;
        mVertexData = ByteBuffer
                .allocateDirect(TABLE_VERTICES.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertexData.put(TABLE_VERTICES);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0f, 0f, 1f);
        String vertexShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderResource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderResource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderResource);
        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(mProgram);
        }
        glUseProgram(mProgram);
        mAColorLocation = glGetAttribLocation(mProgram, A_COLOR);
        mAPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        mVertexData.position(0);
        glVertexAttribPointer(mAPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, mVertexData);
        glEnableVertexAttribArray(mAPositionLocation);
        mVertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(mAColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, mVertexData);
        glEnableVertexAttribArray(mAColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //draw border of table
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
        glDrawArrays(GL_TRIANGLE_FAN, 6, 6);
        glDrawArrays(GL_LINES, 12, 2);
        glDrawArrays(GL_POINTS, 14, 1);
        glDrawArrays(GL_POINTS, 15, 1);
    }

}
