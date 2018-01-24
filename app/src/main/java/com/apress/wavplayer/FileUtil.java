package com.apress.wavplayer;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liangjiangli on 2018/1/2.
 */

public class FileUtil {

    public static String filePath = Environment.getExternalStorageDirectory().getPath();
    public static void writeToSD(Context context){
        if(!isExist()){
            write(context);
        }
    }
    private static void write(Context context){
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("8k16bitpcm.wav");
            File file = new File(filePath, "8k16bitpcm.wav");
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/8k16bitpcm.wav");
            byte[] buffer = new byte[512];
            int count = 0;
            while((count = inputStream.read(buffer)) > 0){
                fileOutputStream.write(buffer, 0 ,count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static boolean isExist(){
        File file = new File(filePath, "8k16bitpcm.wav");
        if(file.exists()){
            return true;
        }else{
            return false;
        }
    }
}
