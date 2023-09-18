#include <stdio.h>

/* Typedef struct forward declaration for the pointer member */
/* 指针成员的 Typedef 结构正向声明 */
typedef struct s_list_member list_member;

typedef struct s_list_member {
    int value;
    list_member *next;
} list_member;

int main(int argc, char **argv) {
    list_member lm1, lm2, lm3;

    lm1.value = 1; lm1.next = &lm2;
    lm2.value = 2; lm2.next = &lm3;
    lm3.value = 3; lm3.next = 0x0;

    printf("third member value is: %d\n", lm1.next->next->value);

    return 0;
}