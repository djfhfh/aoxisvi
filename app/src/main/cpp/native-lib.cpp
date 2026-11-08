#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <fcntl.h>
#include <string>
#include <dirent.h>

#define LOG_TAG "G45Hack"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

int mem_fd = -1;

// Função para achar o PID do Free Fire
int get_pid(const char* pkg) {
    DIR* d = opendir("/proc"); struct dirent* e;
    if (d) {
        while ((e = readdir(d))) {
            int pid = atoi(e->d_name);
            if (pid > 0) {
                char path[64]; snprintf(path, 64, "/proc/%d/cmdline", pid);
                FILE* f = fopen(path, "r");
                if (f) {
                    char cmd[256] = {0}; fgets(cmd, 256, f); fclose(f);
                    if (strstr(cmd, pkg)) { closedir(d); return pid; }
                }
            }
        }
        closedir(d);
    }
    return -1;
}

extern "C" JNIEXPORT void JNICALL
Java_com_g45_ffnativo_OverlayService_toggleEsp(JNIEnv *env, jobject thiz, jboolean enable) {
    if (enable) {
        int pid = get_pid("com.dts.freefireth");
        if (pid == -1) {
            LOGI("Free Fire nao encontrado!");
            return;
        }
        
        char path[64]; snprintf(path, 64, "/proc/%d/mem", pid);
        mem_fd = open(path, O_RDWR);
        
        if (mem_fd == -1) {
            LOGI("Acesso NEGADO. Use o ADB/Shizuku para liberar a memoria.");
        } else {
            LOGI("Conexao com a memoria do Free Fire FEITA (PID: %d)", pid);
            
            // AQUI ENTRAM OS OFFSETS (Precisa do Il2CppDumper no PC)
            // Exemplo: uintptr_t LocalPlayer = ReadMemory(base + 0x123456);
            // float vida = ReadMemory(LocalPlayer + 0x40);
        }
    } else {
        if (mem_fd != -1) { close(mem_fd); mem_fd = -1; }
        LOGI("ESP desligado.");
    }
}

extern "C" JNIEXPORT void JNICALL
Java_com_g45_ffnativo_OverlayService_setAimbot(JNIEnv *env, jobject thiz, jboolean enable) {
    if (enable) LOGI("Aimbot armado. Aguardando alvo...");
}
