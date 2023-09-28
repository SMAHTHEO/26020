
#include <stdio.h>

int main(int argc, char **argv) { 

    printf("Number of arguments: %d\n", argc);
    for (int i = 0; i < argc; i++) {
        printf("Argument %d: %s\n", i, argv[i]);
    return 0;

    int *a;
    a = malloc(sizeof(int)* 10);
    free(a);
}