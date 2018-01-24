package com.apress.wavplayer;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import wavplayer.apress.com.wavplayer.R;

/**
 * Created by liangjiangli on 2018/1/10.
 */

public class NativeWindowPlayerActivity extends AbstractPlayerActivity {
    private final AtomicBoolean isPlaying = new AtomicBoolean();
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_window_player);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
    }

    private final Callback callback = new Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isPlaying.set(true);
            new Thread(player).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isPlaying.set(false);
        }
    };

    private final Runnable player = new Runnable() {
        @Override
        public void run() {
            Surface surface = surfaceHolder.getSurface();
            //初始化原生window
            init(avi, surface);
            long frameDelay = (long) (1000/getFrameRate(avi));
            while (isPlaying.get()) {
                render(avi, surface);
                try {
                    Thread.sleep(frameDelay);
                } catch (Exception e) {
                    break;
                }
            }
        }
    };

    private native static void init(long avi, Surface surface);
    private native static boolean render(long avi, Surface surface);

}
