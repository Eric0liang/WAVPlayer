package com.apress.wavplayer.jni;
public class FmodJni {
    static {
        System.loadLibrary("app"); //jni模块加载名称
    }

    public native void fix(String fileName, int type);
    
}  