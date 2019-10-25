#include "stdio.h"
#include "stdlib.h"
#include "string.h"


#define MIN(a,b) ((a <= b) ? a : b)
#define MAX(a,b) ((a >= b) ? a : b)
#define MAX_NUM_SYMBOLS 30

long long int op_apply(char op, long long int val1, long long int val2)
{
    // printf("%c %lld %lld\n",op, val1, val2);

    if (op == '+')
        return (val1 + val2);
    else if (op == '-')
        return (val1 - val2);
    else if (op == '*')
        return (val1*val2);

    return -1;
}

#define LL_INT_MAX (0x7FFFFFFFFFFFFFFF)
#define LL_INT_MIN (0x8000000000000000)

void min_max(int i, int j,
    long long int minMatrix[MAX_NUM_SYMBOLS][MAX_NUM_SYMBOLS], long long int maxMatrix[MAX_NUM_SYMBOLS][MAX_NUM_SYMBOLS],
    char* opAr)
{
    long long int currMin = LL_INT_MAX;
    long long int currMax = LL_INT_MIN;

    for (int k=i; k<j; k++)
    {
        long long int a = op_apply(opAr[k], minMatrix[i][k], minMatrix[k+1][j]);
        long long int b = op_apply(opAr[k], minMatrix[i][k], maxMatrix[k+1][j]);
        long long int c = op_apply(opAr[k], maxMatrix[i][k], minMatrix[k+1][j]);
        long long int d = op_apply(opAr[k], maxMatrix[i][k], maxMatrix[k+1][j]);

        currMin = MIN(currMin, a);
        currMin = MIN(currMin, b);
        currMin = MIN(currMin, c);
        currMin = MIN(currMin, d);

        currMax = MAX(currMax, a);
        currMax = MAX(currMax, b);
        currMax = MAX(currMax, c);
        currMax = MAX(currMax, d);
    }
    minMatrix[i][j] = currMin;
    maxMatrix[i][j] = currMax;
}

int main(int argc, char** argv)
{
    char* expr;
    char* opAr;
    int* valueAr;
    int numValues;

    expr = (char* )malloc(sizeof(char)*MAX_NUM_SYMBOLS);
    opAr = (char* )malloc(sizeof(char)*MAX_NUM_SYMBOLS);
    valueAr = (int* )malloc(sizeof(int)*MAX_NUM_SYMBOLS);

    scanf("%s",expr);
    numValues = (strlen(expr)/2)+1;
    int i=0;
    while(i<strlen(expr))
    {
        if (i%2 == 0)
        {
            char ch = expr[i];
            valueAr[i/2] = atoi(&ch);
        }
        else
            opAr[i/2] = expr[i];
        i++;
    }

    long long int minMatrix[MAX_NUM_SYMBOLS][MAX_NUM_SYMBOLS];
    long long int maxMatrix[MAX_NUM_SYMBOLS][MAX_NUM_SYMBOLS];

    for (i=0; i<numValues; i++)
    {
        minMatrix[i][i] = valueAr[i];
        maxMatrix[i][i] = valueAr[i];
    }

    for (int diff=1; diff<=(numValues-1); diff++)
    {
        for (i=0; i<=(numValues-diff-1); i++)
        {
            min_max(i, i+diff, minMatrix, maxMatrix, opAr);
        }
    }

#if 0
    for (i=0; i<numValues; i++)
    {
        for (int j=0; j<numValues; j++)
            printf("%lld ",minMatrix[i][j]);
        printf("\n");
    }

    for (i=0; i<numValues; i++)
    {
        for (int j=0; j<numValues; j++)
            printf("%lld ",maxMatrix[i][j]);
        printf("\n");
    }
#endif

    printf("%lld\n",maxMatrix[0][numValues-1]);
    free(expr);
    free(opAr);
    free(valueAr);

    return 0;

}
