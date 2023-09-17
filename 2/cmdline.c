#include <stdio.h>

int main(int argc, char *argv[])
{
    if (argc != 4)
    {
        printf("Usage: %s <a> <b> <c>\n", argv[0]);
        return 1;
    }

    float a, b, c;

    a = atof(argv[1]);
    b = atof(argv[2]);
    c = atof(argv[3]);

    float d = a * b * c;

    printf("%f\n", d);
}