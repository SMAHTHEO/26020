
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv)
{
    int a = atoi(argv[1]);

    if (a % 4 != 0)
    {
        printf("%d is a common year\n", a);
    }
    else if (a % 100 != 0)
    {
        printf("%d is a leap year\n", a);
    }
    else if (a % 400 != 0)
    {
        printf("%d is a common year\n", a);
    }
    else
    {
        printf("%d is a leap year\n", a);
    }

    return 0;
}