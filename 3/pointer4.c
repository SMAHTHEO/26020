#include <stdio.h>

int main(int argc, char **argv) {
    char *string = "hello, world!\n";

    int i = 0;
    while(*(string + i) != '\0') {
        printf("%c", *(string + i));
        i++;
    }

    return 0;
}