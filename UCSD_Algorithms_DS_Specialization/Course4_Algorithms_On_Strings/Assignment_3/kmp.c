#include "stdio.h"
#include "stdlib.h"
#include "string.h"

#define MAX_PATTERN_PLUS_GENOME_LEN (2*1024*1024)
int main(int argc, char** argv)
{
    char* patternGenome;
    unsigned long long int patternLen;
    unsigned long long int* lcpArr;

    patternGenome = (char* )malloc(sizeof(char)*(MAX_PATTERN_PLUS_GENOME_LEN+4));
    scanf("%s",patternGenome);
    patternLen = strlen(patternGenome);
    patternGenome[patternLen] = '$';
    scanf("%s",&patternGenome[patternLen+1]);
    unsigned long long int patternGenomeLen = strlen(patternGenome);

    if (patternLen > (patternGenomeLen-patternLen-1))
        return 0;

    lcpArr = (unsigned long long int* )malloc(sizeof(unsigned long long int)*(patternLen+4));
    memset(lcpArr, 0, (sizeof(unsigned long long int)*(patternLen+4)));

    long long int border = 0;
    for (long long int i=1; i<strlen(patternGenome); i++)
    {
        if (patternGenome[i] == patternGenome[border])
            border = border + 1;
        else
        {
            while ((border>0) && (patternGenome[i] != patternGenome[border]))
                border = lcpArr[border-1];

            if (patternGenome[i] == patternGenome[border])
                border = border+1;
            else
                border = 0;
        }

        if (i <= patternLen)
            lcpArr[i] = border;

        if (border == patternLen)
            printf("%lld ",i-(2*patternLen));

    }

    free(patternGenome);
    free(lcpArr);

    return 0;
}
