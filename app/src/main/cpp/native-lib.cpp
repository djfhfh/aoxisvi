#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <fcntl.h>

#define LOG_TAG "FFHack"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT void JNICALL
Java_com_g45_ffnativo_MainActivity_toggleEsp(JNIEnv *env, jobject thiz, jboolean enable) {
    if (enable) {
        LOGI("ESP ATIVADO (ARMv7)");
        int fd = open("/proc/self/mem", O_RDWR);
        if (fd != -1) {
            LOGI("Leitura de memoria habilitada!");
            close(fd);
        } else {
            LOGI("Acesso negado. Precisa de Root.");
        }
    } else {
        LOGI("ESP DESATIVADO");
    }
}
