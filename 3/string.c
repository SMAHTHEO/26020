

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>


int main(int argc, char **argv) { 

    char s1[128];
    char s2[128];

    // 获得字符串
    printf("input string1:\n");
    fgets(s1, 128, stdin);

    printf("input string2:\n");
    fgets(s2, 128, stdin);

    // 比较字符串
    int i = 0;
    bool flag = true;
    while (s1[i] == s2[i]) {
        if (s1[i] == '\0') {
            printf("they are same\n");
            return 0;
        }
        i++;
    }
    printf("they are different\n");
    return 0;

}