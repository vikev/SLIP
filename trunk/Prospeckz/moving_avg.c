/*
#include <stdio.h>


int bufferSize = 16;
int values[16] = {3};
int counter;

int average(int num){
    counter++;
    values[counter%bufferSize] = num;
    int sum = 0;
    for (int i=0; i<bufferSize; i++) {
        sum += values[i];
    }
    int avg = sum>>4;
    return avg;
}


int main ( void )
{
    counter = 0;
    int i;
    for ( i = 0; i < 100; i++ )
        printf ("avg is %d\n", average(50));
    return 0;
}
*/
