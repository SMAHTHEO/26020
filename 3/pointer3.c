

#include <stdio.h>

int main(int argc, char **argv) {
    int i = atoi(argv[1]);
    int *p;
    p = &i;

    printf("Variable contains %d and is located @%p\n", i, p);

    return 0;
}