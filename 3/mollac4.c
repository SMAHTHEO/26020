#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

    // 创建内存
    int *a = malloc(10 * sizeof(int));
    if(!a) return -1;

    // 填数
    for(int i=0; i<10; i++) {
        a[i] = i;
        printf("%d\n", a[i]);
    }

    free(a);

    return 0;

}