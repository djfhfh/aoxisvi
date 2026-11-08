#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <fcntl.h>

#define LOG_TAG "ProxyMenu"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT void JNICALL
Java_com_g45_ffnativo_OverlayService_toggleEsp(JNIEnv *env, jobject thiz, jboolean enable) {
    if (enable) {
        LOGI("ESP ativado via ADB Pairing (sem root).");
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_g45_ffnativo_OverlayService_setAimbot(JNIEnv *env, jobject thiz, jboolean enable) {
    if (enable) {
        LOGI("Aimbot ativado.");
    }
}
