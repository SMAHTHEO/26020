#include <stdio.h>

int main() {
    int a = sizeof(int);
    int b = sizeof(double);
    int c = sizeof(unsigned long long int);
    int d = a * b * c;

    printf("%d\n",a);
    printf("%d\n",b);
    printf("%d\n",c);
    printf("%d\n",d);
    
}