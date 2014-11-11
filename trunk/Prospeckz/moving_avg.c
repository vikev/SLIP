#include <stdio.h>

static int average = 3;

int avg(int num){
    int temp_avg;
    int num_of_terms = 4;
    if (num) {
        temp_avg = average + num - average/num_of_terms;
        average = temp_avg/num_of_terms;
    }
    return average;
}

int main(int argc, const char * argv[]) {
    for (int i=0; i<20; i ++) {
        printf("Avg %d \n",avg(i));

    }
        printf("\n");
    printf("flunctuations \n");
    printf("avg %d \n", avg(100));
    printf("avg %d\n", avg(101));
    printf("avg %d\n", avg(101));
    printf("avg %d\n", avg(101));
    printf("avg %d\n", avg(100));
    printf("avg %d\n", avg(101));
    printf("avg %d\n", avg(102));
    printf("avg %d\n", avg(103));
    printf("avg %d\n", avg(105));
    printf("avg %d\n", avg(102));
    printf("avg %d\n", avg(103));
     printf("\n");
    printf("steady high \n");
    for (int i=0; i<10; i++)
        printf("avg %d\n", avg(101));
        printf("\n");
    printf("steady low \n");
    for (int i=0; i<10; i++)
        printf("avg %d\n", avg(30));
    printf("\n");
    printf("zeros\n");
    for (int i=0; i<10; i++)
        printf("avg %d\n", avg(0));


    return 0;
}
