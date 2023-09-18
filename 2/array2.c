
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

    int a[argc - 1];

    for (int i = 1; i < argc; i++) {
        a[i - 1] = atoi(argv[i]);
    }

    for (int i = 0; i < argc - 1; i++) {
        if (a[i] % 2 == 0) {
            printf("%d is even", a[i]);
        } else {
            printf("%d is odd", a[i]);
        }
    }
}