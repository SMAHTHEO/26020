
#include <stdio.h>
#include <stdlib.h>  // for atoi
#include <unistd.h>  // for sleep
#include <sys/time.h> // needed for gettimeofday

int main(int argc, char **argv) {  

    int i = atoi(argv[1]);

    struct timeval start, stop, elapsed;

    gettimeofday(&start, NULL);

    sleep(i);

    gettimeofday(&stop, NULL);

    timersub(&stop, &start, &elapsed);

    printf("Time elapsed: %ld.%06ld\n", elapsed.tv_sec, elapsed.tv_usec);

    return 0;

}



    // struct timeval start, stop, elapsed;
    // gettimeofday(&start, NULL);
    // xxx
    // gettimeofday(&stop, NULL);
    // timersub(&stop, &start, &elapsed);