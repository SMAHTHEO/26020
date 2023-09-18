
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {

    int temp;

    for (int i = 1; i < argc; i++) {
        temp = atoi(argv[i]);

        if (temp % 2 == 0) {
            printf("%d is even\n", temp);
        } else {
            printf("%d is odd\n", temp);
        }

    }
    return 0;
}