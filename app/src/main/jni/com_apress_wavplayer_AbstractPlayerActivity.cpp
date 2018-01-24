//
// Created by liangjiangli on 2017/12/29.
//
#include "AbstractPlayer.h"
#include "Common.h"
#include "android/bitmap.h"

extern "C" {
#include "avilib.h"
}

JNIEXPORT jlong JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_open(JNIEnv *env, jobject object, jstring fileName_) {
    avi_t* avi = 0;
    const char *cFileName = env->GetStringUTFChars(fileName_, 0);
    if (cFileName == 0) {
        goto exit;
    }
    avi = AVI_open_input_file(cFileName, 1);
    env->ReleaseStringUTFChars(fileName_, cFileName);
    if (avi == 0) {
        ThrowException(env, "java/io/IOException", AVI_strerror());
    }
    exit:
    return (jlong) avi;
}

JNIEXPORT jdouble JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_getFrameRate(JNIEnv *env, jobject object,
                                                              jlong avi) {
    return AVI_frame_rate((avi_t *) avi);
}

JNIEXPORT jint JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_getHeight(JNIEnv *env, jobject object,
                                                           jlong avi) {
    return AVI_video_height((avi_t *) avi);

}

JNIEXPORT jint JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_getWidth(JNIEnv *env, jobject object,
                                                          jlong avi) {
    return AVI_video_width((avi_t *) avi);

}

JNIEXPORT void JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_close(JNIEnv *env, jobject object, jlong avi) {
    AVI_close((avi_t *) avi);
}

JNIEXPORT jboolean JNICALL
Java_com_apress_wavplayer_AbstractPlayerActivity_render(JNIEnv *env, jobject object,
                                                        jlong avi, jobject bitmap) {

    jboolean isFrameRead = JNI_FALSE;
    char* frameBuffer = 0;
    long frameSize = 0;
    int keyFrame = 0;
    //锁定bitmap并得到raw byte
    if (AndroidBitmap_lockPixels(env, bitmap, (void**) & frameBuffer) < 0) {
        ThrowException(env, "java/io/IOException", "Unable to lock pixels.");
        goto exit;
    }
    //将AVI帧byte读到bitmap中
    frameSize = AVI_read_frame((avi_t*)avi, frameBuffer, &keyFrame);
    //解锁bitmap
    if (AndroidBitmap_unlockPixels(env, bitmap) < 0) {
        ThrowException(env, "java/io/IOException", "Unable to unlock pixels.");
        goto exit;
    }
    if (frameSize > 0) {
        isFrameRead = JNI_TRUE;
    }
    exit :
    return isFrameRead;
}