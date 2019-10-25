#include "stdio.h"
#include "stdlib.h"
#include "string.h"

#define STR_MAX_LENGTH 100
#define MIN(a,b) ((a <= b) ? a : b)
int main(int argc, char** argv)
{
    char* str1;
    char* str2;
    unsigned int editDistanceMatrix[STR_MAX_LENGTH+1][STR_MAX_LENGTH+1];
    for (int i=0; i<(STR_MAX_LENGTH+1); i++)
    {
        memset(editDistanceMatrix[i], 0, (sizeof(unsigned int)*(STR_MAX_LENGTH+1)));
        if (i != 0)
        {
            editDistanceMatrix[0][i] = i;
            editDistanceMatrix[i][0] = i;
        }
    }

    str1 = (char* )malloc(sizeof(char)*STR_MAX_LENGTH);
    str2 = (char* )malloc(sizeof(char)*STR_MAX_LENGTH);

    scanf("%s",str1);
    scanf("%s",str2);

    for (int i=1; i<strlen(str1)+1; i++)
    {
        for (int j=1; j<strlen(str2)+1; j++)
        {
            if (str1[i-1] == str2[j-1])
            {
                editDistanceMatrix[i][j] = MIN(MIN(editDistanceMatrix[i-1][j]+1, editDistanceMatrix[i][j-1]+1),
                    editDistanceMatrix[i-1][j-1]);
            }
            else
            {
                editDistanceMatrix[i][j] = MIN(MIN(editDistanceMatrix[i-1][j]+1, editDistanceMatrix[i][j-1]+1),
                    editDistanceMatrix[i-1][j-1]+1);
            }
        }
    }

    printf("%d\n",editDistanceMatrix[strlen(str1)][strlen(str2)]);

    free(str1);
    free(str2);
    return 0;
}
