
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

    // 创建内存
    int *a = malloc(10 * sizeof(int));
    if(!a) return -1;

    // 赋值 2N
    for(int i=0; i<10; i++)
        a[i] = i*2;

    int *b = *a;

    // 打印
    for(int i=0; i<10; i++)
        printf("%d ", b[i]);
    printf("\n");

    return 0;
}