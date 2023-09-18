#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
    int a = atoi(argv[1]);

    long long int result = 1;

    for (int i = 1; i<= a; i++ ) {
        result *= i;
    }

    printf("%lld\n", result);
}