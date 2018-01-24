LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := app

LOCAL_SRC_FILES := wavplayer_apress_com_wavplayer_JniTest.cpp

LOCAL_STATIC_LIBRARIES += avilib_shared
#动态链接日志库
LOCAL_LDLIBS :=  -L$(SYSROOT)/usr/lib -llog
LOCAL_LDLIBS += -lOpenSLES
include $(BUILD_SHARED_LIBRARY)
$(call import-module, transcode-1.1.7/avilib)

