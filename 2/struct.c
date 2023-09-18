#include <stdio.h>
#include <stdlib.h>

typedef struct {
    unsigned int hour;
    unsigned int minute;
    unsigned int second;
}timestamp;


timestamp add_timestamps(timestamp a, timestamp b) {
    timestamp c;
    int hasnext = 0;
    c.second = a.second + b.second;
    if (c.second >= 60) {
        c.second -= 60;
        hasnext = 1;
    }
    c.minute = a.minute + b.minute + hasnext;
    if (c.minute >= 60) {
        c.minute -= 60;
        hasnext = 1;
    }
    c.hour = a.hour + b.hour + hasnext;

    return c;
}

int main(int argc, char **argv) {
    timestamp a, b, c;
    a.hour = atoi(argv[1]);
    a.minute = atoi(argv[2]);
    a.second = atoi(argv[3]);
    b.hour = atoi(argv[4]);
    b.minute = atoi(argv[5]);
    b.second = atoi(argv[6]);
    c = add_timestamps(a, b);
    printf("%d:%d:%d\n", c.hour, c.minute, c.second);
    return 0;
}
