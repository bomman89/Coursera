#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

#define MAX_STR_LEN (2*1024*1024)

typedef struct _SUFFIX_ARR_ELEMENT_
{
    unsigned int suffixIdx;
} SUFFIX_ARR_ELEMENT;
char* gInputStr;

void suffix_arr_sort_double(unsigned int* countArr, unsigned int* classes, unsigned int* newClasses,
                            unsigned int* suffixOrder, unsigned int* newSuffixOrder, unsigned int subStrLen)
{
    for (unsigned int i=0; i<strlen(gInputStr); i++)
        countArr[classes[i]]++;

    for (unsigned int i=1; i<strlen(gInputStr); i++)
        countArr[i] = countArr[i] + countArr[i-1];

    int newSuffixIdx=0;
    for (int i=strlen(gInputStr)-1; i>=0; i--)
    {
        newSuffixIdx = ((suffixOrder[i]-subStrLen)+strlen(gInputStr));
        newSuffixIdx = newSuffixIdx%strlen(gInputStr);
        countArr[classes[newSuffixIdx]]--;
        newSuffixOrder[countArr[classes[newSuffixIdx]]]=newSuffixIdx;
    }

    newClasses[newSuffixOrder[0]] = 0;
    for (unsigned int i=1; i<strlen(gInputStr); i++)
    {
        if (classes[newSuffixOrder[i]] != classes[newSuffixOrder[i-1]]) {
            newClasses[newSuffixOrder[i]] = newClasses[newSuffixOrder[i-1]]+1;
        }
        else {
            unsigned int subStrSuffixIdx1 = (newSuffixOrder[i]+subStrLen)%strlen(gInputStr);
            unsigned int subStrSuffixIdx2 = (newSuffixOrder[i-1]+subStrLen)%strlen(gInputStr);

            if (classes[subStrSuffixIdx1] != classes[subStrSuffixIdx2])
                newClasses[newSuffixOrder[i]] = newClasses[newSuffixOrder[i-1]]+1;
            else
                newClasses[newSuffixOrder[i]] = newClasses[newSuffixOrder[i-1]];
        }
    }
}


int main(int argc, void** argv)
{
    gInputStr = (char* )malloc(MAX_STR_LEN*sizeof(char));
    scanf("%s",gInputStr);

    unsigned int symToArIdx[27];
    unsigned int* countArr;
    unsigned int* classes;
    unsigned int* newClasses;
    unsigned int* suffixOrder;
    unsigned int* newSuffixOrder;

    unsigned int countAndClassArrLen = strlen(gInputStr);
    if (countAndClassArrLen < 5)
        countAndClassArrLen = 5;

    countArr = (unsigned int* )malloc(sizeof(unsigned int)*countAndClassArrLen);
    memset(countArr, 0, (sizeof(unsigned int)*countAndClassArrLen));

    classes = (unsigned int* )malloc(sizeof(unsigned int)*countAndClassArrLen);
    newClasses = (unsigned int* )malloc(sizeof(unsigned int)*countAndClassArrLen);
    suffixOrder  = (unsigned int* )malloc(sizeof(unsigned int)*strlen(gInputStr));
    newSuffixOrder  = (unsigned int* )malloc(sizeof(unsigned int)*strlen(gInputStr));

    symToArIdx[0] = 0;
    symToArIdx['A'-'A'+1] = 1;
    symToArIdx['C'-'A'+1] = 2;
    symToArIdx['G'-'A'+1] = 3;
    symToArIdx['T'-'A'+1] = 4;

    for (unsigned int i=0; i<strlen(gInputStr); i++)
    {
        if (gInputStr[i] == '$')
            countArr[0]++;
        else
            countArr[symToArIdx[gInputStr[i]-'A'+1]]++;
    }

    for (unsigned int i=1; i<5; i++)
        countArr[i] = countArr[i] + countArr[i-1];


    for (int i=strlen(gInputStr)-1; i>=0; i--)
    {
        if (gInputStr[i] == '$')
        {
            countArr[0]--;
            suffixOrder[countArr[0]]=i;
        }
        else
        {
            countArr[symToArIdx[gInputStr[i]-'A'+1]]--;
            suffixOrder[countArr[symToArIdx[gInputStr[i]-'A'+1]]]=i;
        }
    }

    classes[suffixOrder[0]]=0;
    for (unsigned int i=1; i<strlen(gInputStr); i++)
    {
        if (gInputStr[suffixOrder[i]] != gInputStr[suffixOrder[i-1]])
            classes[suffixOrder[i]] = classes[suffixOrder[i-1]]+1;
        else
            classes[suffixOrder[i]] = classes[suffixOrder[i-1]];
    }

    bool classArrSel=true;
    for (unsigned int subStrLen=1; subStrLen<=strlen(gInputStr); subStrLen*=2)
    {
        memset(countArr, 0, sizeof(unsigned int)*strlen(gInputStr));
        if (classArrSel)
            suffix_arr_sort_double(countArr, classes, newClasses, suffixOrder, newSuffixOrder, subStrLen);
        else
            suffix_arr_sort_double(countArr, newClasses, classes, newSuffixOrder, suffixOrder, subStrLen);
        classArrSel = !classArrSel;
    }

    for (unsigned int i=0; i<strlen(gInputStr); i++)
        printf("%d ",suffixOrder[i]);
    printf("\n");
    return 0;
}
