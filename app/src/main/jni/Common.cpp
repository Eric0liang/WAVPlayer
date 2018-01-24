//
// Created by liangjiangli on 2018/1/4.
//
#include "Common.h"

void ThrowException(JNIEnv* env, const char* className,
                    const char* message) {
    jclass clazz = env->FindClass(className);
    if (clazz != 0) {
        env->ThrowNew(clazz, message);
        env->DeleteLocalRef(clazz);
    }
}

