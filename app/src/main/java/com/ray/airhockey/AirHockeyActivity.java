package com.ray.airhockey;

import android.annotation.SuppressLint;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class AirHockeyActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private AirHockeyRenderer mAirHockeyRenderer;
    private boolean mRendererSet;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mAirHockeyRenderer = new AirHockeyRenderer(this);
        mGLSurfaceView.setRenderer(mAirHockeyRenderer);
        mGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    final float normalizeX = event.getX() * 1f / v.getWidth() * 2 - 1;
                    final float normalizeY = -(event.getY() * 1f / v.getHeight() * 2 - 1);

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRenderer.handleTouchPress(normalizeX, normalizeY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        mGLSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mAirHockeyRenderer.handleTouchDrag(normalizeX, normalizeY);
                            }
                        });
                    }

                    return true;
                }
                return false;
            }
        });
        mRendererSet = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }
}
