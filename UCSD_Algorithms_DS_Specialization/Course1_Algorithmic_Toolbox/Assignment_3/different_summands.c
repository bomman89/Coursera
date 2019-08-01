#include "stdio.h"
#include "stdlib.h"
#include "memory.h"

int main(int argc, char** argv)
{
    unsigned long long int sumValue;
    scanf("%lld",&sumValue);

    unsigned long long int currSum = 0;
    unsigned long int lastDistinctValue = 1;
    for (int i=1; ;i++)
    {
        currSum += i;
        if (currSum > sumValue) {
            lastDistinctValue = i-1;
            currSum -= i;
            break;
        }
        else if (currSum == sumValue)
        {
            lastDistinctValue = i;
            break;
        }
    }

    printf("%d\n",lastDistinctValue);

    for (int i=1; i<lastDistinctValue; i++)
    {
        printf("%d ",i);
    }

    printf("%d\n",(sumValue-currSum)+lastDistinctValue);

    return 0;
}


