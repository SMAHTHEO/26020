
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) { 

    // 放入所有数字

    int *is = malloc(sizeof(int) * (argc-1));

    for (int i = 1; i < argc; i++) {
        is[i-1] = atoi(argv[i]);
    }

    // 从小到大排序

    for (int i = 0; i < argc-1; i++) {
        for (int j = i+1; j < argc-1; j++) {
            if (is[i] > is[j]) {
                int tmp = is[i];
                is[i] = is[j];
                is[j] = tmp;
            }
        }
    }

    // 打印

    for (int i = 0; i < argc-1; i++) {
        printf("%d ", is[i]);
    }

    // 释放内存

    free(is);

    return 0;

}