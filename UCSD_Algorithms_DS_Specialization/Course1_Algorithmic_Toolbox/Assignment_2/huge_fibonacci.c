#include "stdio.h"
#include "stdlib.h"

int main(int argc, char** argv)
{
    unsigned long long int n;
    unsigned int m;
    scanf("%lld %d",&n, &m);

    int prevMod=-1;
    int currMod=-1;
    int pisanoPeriod=2;

    unsigned long long int fib[2] = {0,1};
    int i=0;
    while (1)
    {
        prevMod = currMod;
        fib[i] = (fib[0]%m + fib[1]%m)%m;
        currMod = fib[i];
        i = (i+1)%2;
        if ((prevMod == 0) && (currMod == 1))
        {
            pisanoPeriod--;
            break;
        }
        pisanoPeriod++;
    }

    // printf("Pisano Period %d\n",pisanoPeriod);
    if ((n%pisanoPeriod) == 0) {
        printf("0\n");
        return 0;
    } else if ((n%pisanoPeriod) == 1) {
        printf("1\n");
        return 0;
    }

    i=0;
    fib[0] = 0;
    fib[1] = 1;
    unsigned int mod=0;
    for (int j=2; j<=(n%pisanoPeriod); j++)
    {
        fib[i] = (fib[0]%m + fib[1]%m)%m;
        mod = fib[i];
        i = (i+1)%2;
    }

    printf("%d\n",mod);

    return 0;
}
