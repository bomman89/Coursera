#include "stdio.h"
#include "stdlib.h"
#include "string.h"

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
    unsigned long long int m, n;

    scanf("%lld %lld",&m, &n);

    unsigned int pisanoPeriod = pisano_period_get(10);
    // printf("p %d\n",pisanoPeriod);
    unsigned int* pisanoMods;
    pisanoMods = (unsigned int* )malloc(sizeof(unsigned int)*pisanoPeriod);
    memset(pisanoMods, 0, (sizeof(unsigned int)*pisanoPeriod));

    pisanoMods[0] = 0;
    pisanoMods[1] = 1;
    unsigned int pisanoPeriodMod=1;
    unsigned int j;
    for (j=2; j<pisanoPeriod; j++)
    {
        pisanoMods[j] = (pisanoMods[j-1]%10 + pisanoMods[j-2]%10)%10;
        pisanoPeriodMod = (pisanoPeriodMod + pisanoMods[j])%10;
    }

    unsigned long long int mod=0;
    unsigned long long int numPisanoPeriodsBetween = 0;
    if ((m/pisanoPeriod) == (n/pisanoPeriod)) {
        mod = 0;
        for (j=(m%pisanoPeriod);  j<=(n%pisanoPeriod); j++) {
                mod = (mod + pisanoMods[j])%10;
        }
    } else {
        mod=0;
        for (j=(m%pisanoPeriod);  j<pisanoPeriod; j++) {
            mod = (mod + pisanoMods[j])%10;
        }

        for (j=0;  j<=(n%pisanoPeriod); j++) {
            mod = (mod + pisanoMods[j])%10;
        }

        unsigned long long int mPisanoPeriodEnd = ((m + pisanoPeriod)/pisanoPeriod)*pisanoPeriod;
        unsigned long long int nPisanoPeriodStart = (n/pisanoPeriod)*pisanoPeriod;
        numPisanoPeriodsBetween = ((nPisanoPeriodStart - mPisanoPeriodEnd)/pisanoPeriod);
        mod = (mod + ((pisanoPeriodMod*numPisanoPeriodsBetween)%10))%10;
    }

    printf("%d\n",mod);

    free(pisanoMods);
    return 0;
}


