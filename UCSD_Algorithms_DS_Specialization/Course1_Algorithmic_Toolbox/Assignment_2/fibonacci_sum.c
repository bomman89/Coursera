#include "stdio.h"
#include "stdlib.h"

unsigned int pisano_period_get(unsigned int m)
{
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

    return pisanoPeriod;

}

int main(int argc, char** argv)
{
    unsigned long long int n;
    unsigned int m = 10;
    scanf("%lld",&n);

    if (n <= 1) {
        printf("%d\n",n);
        return 0;
    }

    unsigned int pisanoPeriod = pisano_period_get(10);
    // printf("p %d\n",pisanoPeriod);
    unsigned int fib[2] = {0,1};
    int i=0;
    unsigned int mod=1;
    unsigned int j;

    for (j=2; j<pisanoPeriod; j++)
    {
        fib[i] = (fib[0]%m + fib[1]%m)%m;
        mod = (mod + fib[i])%m;
        i = (i+1)%2;
    }

    mod = (mod*(n/pisanoPeriod))%m;

    fib[0] = 0;
    fib[1] = 1;
    i=0;
    mod=0;
    for (j=0; j<=(n%pisanoPeriod); j++)
    {
        if (j < 2) {
            mod = (mod + fib[j])%m;
        } else {
            fib[i] = (fib[0]%m + fib[1]%m)%m;
            mod = (mod + fib[i])%m;
            i = (i+1)%2;
        }
    }

    printf("%d\n",mod);

    return 0;
}

