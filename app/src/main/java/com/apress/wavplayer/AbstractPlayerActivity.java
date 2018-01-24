package com.apress.wavplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;

import java.io.IOException;

public abstract class AbstractPlayerActivity extends Activity {
    public static final String EXTRA_FILE_NAME = "file_name";
    protected long avi = 0;
    protected void onStart() {
        super.onStart();
        try {
            avi = open(getFileName());
        }catch (IOException e) {
            new AlertDialog.Builder(this)
                    .setTitle("错误提示")
                    .setMessage(e.getMessage())
                    .show();
        }
    }
    protected void onStop() {
        super.onStop();
        if (avi != 0) {
            close(avi);
            avi = 0;
        }
    }
    protected  String getFileName() {
        return getIntent().getStringExtra(EXTRA_FILE_NAME);
    }

    protected native static long open(String fileName) throws  IOException;
    protected native static double getFrameRate(long avi);
    protected native static int getHeight(long avi);
    protected native static int getWidth(long avi);
    protected native static void close(long avi);
    protected native static boolean render(long avi, Bitmap bitmap);

    static {
        System.loadLibrary("app");
    }


}
