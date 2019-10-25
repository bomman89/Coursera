#include "stdio.h"
#include "stdlib.h"
#include "string.h"

#define MAX_PATTERN_LEN (1*1024*1024)
#define MAX_GENOME_LEN (1*1024*1024)
#define MAX_SYMBOLS (27)

const char possibleSyms[4] = {'A', 'C', 'G', 'T'};
int main(int argc, char** argv)
{
    char* pattern;
    char* genome;

    pattern = (char* )malloc(sizeof(char)*MAX_PATTERN_LEN);
    memset(pattern, 0, sizeof(char)*MAX_PATTERN_LEN);
    genome = (char* )malloc(sizeof(char)*MAX_GENOME_LEN);
    memset(genome, 0, sizeof(char)*MAX_GENOME_LEN);

    scanf("%s",pattern);
    scanf("%s",genome);

    unsigned int* stateTable[MAX_SYMBOLS];

    stateTable['A'-'A'] = (unsigned int* )malloc((strlen(pattern)+1)*sizeof(unsigned int));
    memset(stateTable['A'-'A'], 0, ((strlen(pattern)+1)*sizeof(unsigned int)));
    stateTable['C'-'A'] = (unsigned int* )malloc((strlen(pattern)+1)*sizeof(unsigned int));
    memset(stateTable['C'-'A'], 0, ((strlen(pattern)+1)*sizeof(unsigned int)));
    stateTable['G'-'A'] = (unsigned int* )malloc((strlen(pattern)+1)*sizeof(unsigned int));
    memset(stateTable['G'-'A'], 0, ((strlen(pattern)+1)*sizeof(unsigned int)));
    stateTable['T'-'A'] = (unsigned int* )malloc((strlen(pattern)+1)*sizeof(unsigned int));
    memset(stateTable['T'-'A'], 0, ((strlen(pattern)+1)*sizeof(unsigned int)));

    for (int i=0; i<strlen(pattern); i++)
        stateTable[pattern[i]-'A'][i] = (i+1);

    unsigned int prefixState=0;
    for (int i=1; i<strlen(pattern)+1; i++)
    {
        for (int j=0; j<4; j++)
        {
            if (pattern[i] == possibleSyms[j])
                continue;
            stateTable[possibleSyms[j]-'A'][i] = stateTable[possibleSyms[j]-'A'][prefixState];
        }
        if (i < strlen(pattern))
            prefixState = stateTable[pattern[i]-'A'][prefixState];
    }

    unsigned int nextState = 0;
    for (int i=0; i<strlen(genome); i++)
    {
        nextState = stateTable[genome[i]-'A'][nextState];
        if (nextState == strlen(pattern))
            printf("%d ",i-strlen(pattern)+1);
    }

    free(pattern);
    free(genome);
    free(stateTable['A'-'A']);
    free(stateTable['C'-'A']);
    free(stateTable['G'-'A']);
    free(stateTable['T'-'A']);

    return 0;
}
