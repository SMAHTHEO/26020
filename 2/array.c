
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

    // 创建一个数组，用来存储输入的数字
    int a[argc - 1];
    // 从小到大排序
    int temp;
    for (int i = 1; i < argc; i++) {
        a[i - 1] = atoi(argv[i]);
        for (int j = 0; j < i; j++) {
            if (a[i - 1] < a[j]) {
                temp = a[i - 1];
                a[i - 1] = a[j];
                a[j] = temp;
            }
        }
    }

}