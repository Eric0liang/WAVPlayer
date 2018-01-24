package com.apress.wavplayer.jni;

import java.io.IOException;

public class JniTest {
    static {
        System.loadLibrary("app"); //jni模块加载名称  
    }

    public native String getJniString(); //该方法是红色的，暂时不用理会

    /**
     * Plays the given WAVE file using native sound API.
     *
     * @param fileName file name.
     * @throws java.io.IOException
     */
    public native void play(String fileName) throws IOException;
    
}  