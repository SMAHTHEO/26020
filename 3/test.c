int glob = 12;
typedef struct {
    int member1;
    float member2;
} mystruct;
int main(int argc, char **argv) {
    mystruct ms = {1, 2.0};
    // With modern processors an address is a 64 bits value so we need the
    // right format specifier: `%lu` or `%lx` if we want to print in hexadecimal
    printf("ms address is: 0x%lx, glob address is 0x%lx\n", &ms, &glob);
    return 0;
}