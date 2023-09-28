#include <stdio.h>
#include <stdlib.h>

void *my_realloc(void *ptr, size_t old_size, size_t new_size) {
    // 如果 ptr 为空，那么就直接 malloc
    if(!ptr) return malloc(new_size);
    // 如果 new_size 为 0，那么就直接 free
    if(new_size == 0) {
        free(ptr);
        return NULL;
    }

    // 如果 new_size 大于 old_size，那么就直接 malloc
    void *new_ptr = malloc(new_size);
    if(!new_ptr) return NULL;

    size_t copy_size = old_size < new_size ? old_size : new_size;
    for(size_t i=0; i<copy_size; i++)
        *((char *)new_ptr + i) = *((char *)ptr + i);

    free(ptr);
    return new_ptr;
}

int main(int argc, char **argv) {
    /* first malloc space for 5 int */
    /* 为 5 int 分配第一个 malloc 空间 */
    int *array = malloc(5 * sizeof(int));
    if(!array) return -1;

    for(int i=0; i<5; i++) {
        array[i] = i*10;
        printf("before realloc, array[%d] = %d\n", i, array[i]);
    }

    /* expand the size to 10 int */
    /* 将大小扩展为 10 int */
    array = my_realloc(array, 5*sizeof(int), 10*sizeof(int));
    if(!array) return -1;

    for(int i=5; i<10; i++)
        array[i] = i*10;

    for(int i=0; i<10; i++)
        printf("after realloc, array[%d] = %d\n", i, array[i]);

    free(array);
    return 0;
}