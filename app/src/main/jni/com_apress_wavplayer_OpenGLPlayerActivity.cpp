//
// Created by liangjiangli on 2017/12/29.
//
#include "AbstractPlayer.h"
#include "Common.h"
#include "GLES/gl.h"
#include "GLES/glext.h"
#include "malloc.h"

extern "C" {
#include "avilib.h"
}

struct Instance {
    char* buffer;
    GLuint texture;

    Instance() :
            buffer(0),
            texture(0) {

    }
};

JNIEXPORT jlong JNICALL
Java_com_apress_wavplayer_OpenGLPlayerActivity_init(JNIEnv *env, jclass type, jlong avi) {
    Instance* instance = 0;
    long frameSize = AVI_frame_size((avi_t *) avi, 0);
    if (frameSize <= 0) {
        ThrowException(env, "java/io/RuntimeException",
                       "Unable to get the frame size");
        goto exit;
    }
    instance = new Instance();
    instance->buffer = (char*) malloc(frameSize);
    if (0 == instance->buffer ) {
        ThrowException(env, "java/io/RuntimeException",
                       "Unable to allocate buffer");
        delete instance;
        instance = 0;
    }
    exit :
    return (jlong) instance;

}

JNIEXPORT void JNICALL
Java_com_apress_wavplayer_OpenGLPlayerActivity_initSurface(JNIEnv *env, jclass type, jlong instance,
                                                           jlong avi) {
    Instance* inst = (Instance*) instance;
    //启用纹理
    glEnable(GL_TEXTURE_2D);
    //生成一个纹理对象
    glGenTextures(1, &inst->texture);
    //绑定到生成的纹理上
    glBindTexture(GL_TEXTURE_2D, inst->texture);
    int frameWidth = AVI_video_width((avi_t*) avi);
    int frameHeight = AVI_video_height((avi_t*) avi);
    //剪切纹理矩形
    GLint rect[] = {0, frameHeight, frameWidth, -frameHeight};
    glTexParameteriv(GL_TEXTURE_2D, GL_TEXTURE_CROP_RECT_OES, rect);
    //填充颜色
    glColor4f(1.0,1.0,1.0,1.0);
    //生成一个空的纹理
    glTexImage2D(GL_TEXTURE_2D,
                 0,
                 GL_RGB,
                 frameWidth,
                 frameHeight,
                 0,
                 GL_RGB,
                 GL_UNSIGNED_SHORT_5_6_5,
                 0);

}

JNIEXPORT jboolean JNICALL
Java_com_apress_wavplayer_OpenGLPlayerActivity_render(JNIEnv *env, jclass type, jlong instance,
                                                      jlong avi) {
    Instance* inst = (Instance*) instance;
    jboolean isFrameRead = JNI_FALSE;
    int keyFrame = 0;
    //将AVI帧字节读到bitmap
    long frameSize = AVI_read_frame((avi_t *) avi, inst->buffer, &keyFrame);
    //检查帧是否读了
    if (frameSize <= 0) {
        goto exit;
    }
    //读帧
    isFrameRead = JNI_TRUE;
    //使用新帧更新新纹理
    glTexSubImage2D(GL_TEXTURE_2D,
                    0,
                    0,
                    0,
                    AVI_video_width((avi_t*) avi),
                    AVI_video_height((avi_t*) avi),
                    GL_RGB,
                    GL_UNSIGNED_SHORT_5_6_5,
                    inst->buffer);
    //绘制纹理
    glDrawTexiOES(0,
                  0,
                  0,
                  AVI_video_width((avi_t *) avi),
                  AVI_video_height((avi_t *) avi));

    exit:
    return isFrameRead;
}

JNIEXPORT void JNICALL
Java_com_apress_wavplayer_OpenGLPlayerActivity_free(JNIEnv *env, jclass type, jlong instance) {
    Instance* inst = (Instance*) instance;
    if (inst != 0) {
        free(inst->buffer);
        delete inst;
    }
}