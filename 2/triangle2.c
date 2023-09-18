
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    int a = atoi(argv[1]);

    for (int i = 1;i <= a;i++) {
        for (int j = 1;j <= i;j++) {
            printf("*");
        }
        printf("\n");
    }
    for (int i = a-1;i >= 1;i--) {
        for (int j = 1;j <= i;j++) {
            printf("*");
        }
        printf("\n");
    }
    return 0;
}