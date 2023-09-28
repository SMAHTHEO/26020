
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) { 

    // 创建行和列

    int row = atoi(argv[1]);
    int col = atoi(argv[2]);

    int num = 0;

    // 打印
    for (int i = 0; i < row; i++) {
        for (int j = 0; j < col; j++, num++) {
            printf("%d ", num);
        }
        printf("\n");
    }

    return 0;

}