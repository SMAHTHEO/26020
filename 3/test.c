


#include <stdio.h>
#include <stdlib.h>

typedef struct {
    // lots of large (8 bytes) fields:
    double a; double b; double c; double d; double e; double f;
} large_struct;
large_struct f(large_struct s) { // very inefficient in terms of performance and memory usage!
    s.a += 42.0;
    return s;
}
int main(int argc, char **argv) {
    large_struct x = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
    large_struct y = f(x);
    printf("y.a: %f\n", y.a);
}