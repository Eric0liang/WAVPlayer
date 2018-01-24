package com.apress.wavplayer;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicBoolean;

import wavplayer.apress.com.wavplayer.R;

/**
 * Created by liangjiangli on 2018/1/4.
 */

public class BitmapPlayerActivity extends AbstractPlayerActivity {
    private final AtomicBoolean isPlaying = new AtomicBoolean();
    private SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.act_bitmap_player);
        SurfaceView surfaceView = (SurfaceView)findViewById(R.id.surface_view);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                isPlaying.set(true);
                new Thread(renderer).start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                isPlaying.set(false);
            }
        });
    }


    private final Runnable renderer = new Runnable() {
        @Override
        public void run() {
            //创建一个新的bitmap保存所有的帧
            Bitmap bitmap = Bitmap.createBitmap(getWidth(avi), getHeight(avi), Bitmap.Config.RGB_565);
            //帧数计算延迟
            long frameDelay = (long) (1000 / getFrameRate(avi));
            //开始渲染
            while (isPlaying.get()) {
                //将帧渲染到bitmap
                boolean isComplete = !render(avi, bitmap);
                if (isComplete) {
                    break;
                }
                Canvas canvas = surfaceHolder.lockCanvas();
                canvas.drawBitmap(bitmap, 0, 0, null);
                surfaceHolder.unlockCanvasAndPost(canvas);
                //等待下一帧
                try {
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };
}
