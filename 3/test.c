
#include <stdio.h>

int main() {
    char *a = "Hello World";
    a[sizeof(a) - 2] = 'a'; 
    printf("%s\n", a);  
    return 0;
}

