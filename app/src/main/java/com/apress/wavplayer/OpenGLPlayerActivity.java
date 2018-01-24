package com.apress.wavplayer;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wavplayer.apress.com.wavplayer.R;

/**
 * Created by liangjiangli on 2018/1/10.
 */

public class OpenGLPlayerActivity extends AbstractPlayerActivity {
    private final AtomicBoolean isPlaying = new AtomicBoolean();
    private long instance;
    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_open_gl_player);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        glSurfaceView.setRenderer(renderer);
        //请求是渲染帧
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance = init(avi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        free(instance);
        instance = 0;
    }

    private final Runnable player = new Runnable() {
        @Override
        public void run() {
            long frameDelay = (long) (1000 / getFrameRate(avi));
            while (isPlaying.get()) {
                glSurfaceView.requestRender();
                try {
                    Thread.sleep(frameDelay);
                } catch (Exception e) {
                    break;
                }
            }
        }
    };
    private final Renderer renderer = new Renderer() {

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            initSurface(instance, avi);
            isPlaying.set(true);
            new Thread(player).start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            //渲染下一帧
            boolean isRender = render(instance, avi);
            Log.e("testtt", "isRender=" + isRender);
            if (!isRender) {
                isPlaying.set(false);
            }
        }
    };

    private native static long init(long avi);

    private native static void initSurface(long instance, long avi);

    private native static boolean render(long instance, long avi);

    private native static void free(long instance);
}
