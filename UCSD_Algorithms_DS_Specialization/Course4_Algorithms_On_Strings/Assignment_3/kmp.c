#include "stdio.h"
#include "stdlib.h"
#include "string.h"

#define MAX_PATTERN_LEN (1*1000*1000)
#define MAX_GENOME_LEN (1*1000*1000)

int main(int argc, char** argv)
{
    char* pattern;
    char* genome;
    unsigned int patternLen;
    unsigned int genomeLen;
    unsigned int* lcpArr;

    pattern = (char* )malloc(sizeof(char)*(MAX_PATTERN_LEN+1));
    genome = (char* )malloc(sizeof(char)*(MAX_PATTERN_LEN+1));
    scanf("%s",pattern);
    scanf("%s",genome);
    patternLen = strlen(pattern);
    pattern[patternLen] = '$';
    genomeLen = strlen(genome);

    if ((patternLen-1) > genomeLen)
        return 0;

    lcpArr = (unsigned int* )malloc(sizeof(unsigned int)*(patternLen+1));
    memset(lcpArr, 0, (sizeof(unsigned int)*(patternLen+1)));

    unsigned int border=0;
    for (unsigned int i=1; i<(patternLen+1); i++)
    {
        while ((border>0) && (pattern[i] != pattern[border]))
            border = lcpArr[border-1];

        if (pattern[i] == pattern[border])
            border = border+1;
        else
            border = 0;

        lcpArr[i] = border;
    }


    border = 0;
    for (unsigned int i=0; i<genomeLen; i++)
    {
        while ((border>0) && (genome[i] != pattern[border]))
            border = lcpArr[border-1];

        if (genome[i] == pattern[border])
            border = border+1;
        else
            border = 0;

        if (border == patternLen)
            printf("%d ",(i-patternLen+1));

    }

    free(pattern);
    free(genome);
    free(lcpArr);

    return 0;
}
