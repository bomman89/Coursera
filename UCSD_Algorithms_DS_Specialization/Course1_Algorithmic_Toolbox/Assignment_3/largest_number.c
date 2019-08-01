#include "stdio.h"
#include "stdlib.h"
#include "memory.h"


void merge_array(void** arr, int startIdx1, int startIdx2, int arSize, void** tempArr,
    int (*compareTo)(void* , void* ))
{
    memcpy(&tempArr[startIdx1], &arr[startIdx1], (startIdx2-startIdx1)*sizeof(void* ));

    int i, j, k;
    for (i=startIdx1, j=startIdx2, k=startIdx1; ; )
    {
        // printf("%d %d %d\n",i, j, k);
        if (compareTo(tempArr[i], arr[j]) <= 0)
        {
            arr[k] = tempArr[i];
            i++;
        }
        else
        {
            arr[k] = arr[j];
            j++;
        }
        k++;

        if (j >= (startIdx1+arSize))
        {
            memcpy(&arr[k], &tempArr[i], (startIdx2-i)*sizeof(void* ));
            break;
        }
        else if (i >= startIdx2)
        {
            break;
        }
    }
}

void merge_sort(void** arr, int numElements, int (*compareTo)(void* , void* ))
{
    void** newArr = (void** )malloc(sizeof(void* )*numElements);
    int arSize;

    for (arSize=2; arSize < numElements; arSize*=2)
    {
        for (int subArIdx=0; subArIdx < numElements; subArIdx+=arSize)
        {
            if (subArIdx+(arSize/2) < numElements)
            {
                int adjustedArSize = arSize;
                if ((subArIdx+arSize) > numElements)
                    adjustedArSize = numElements-subArIdx;

                merge_array(arr, subArIdx, subArIdx+(arSize/2), adjustedArSize, newArr, compareTo);
            }
        }
    }

    // printf("arSize %d\n",arSize);
    merge_array(arr, 0, arSize/2, numElements, newArr, compareTo);
}

typedef struct _VALUE_
{
    unsigned int value;
} VALUE;

int val_compare(void* ele1, void* ele2)
{
    VALUE* val1 = (VALUE* )ele1;
    VALUE* val2 = (VALUE* )ele2;

    unsigned int total1, total2;
    int currPower = 1000;
    while (1)
    {
        if (val2->value/currPower != 0) {
            total1 = val1->value*currPower*10 + val2->value;
            break;
        }
        currPower = currPower/10;
    }

    currPower = 1000;
    while (1)
    {
        if (val1->value/currPower != 0) {
            total2 = val2->value*currPower*10 + val1->value;
            break;
        }
        currPower = currPower/10;
    }

    if (total1 > total2)
        return -1;
    else if (total1 < total2)
        return 1;

    return 0;
}

int main(int argc, char** argv)
{
    int numValues;


    // FILE* pFile;
    // pFile = fopen("test_case_3.txt", "r+");

    scanf("%d",&numValues);

    VALUE** valArr;
    valArr = (VALUE** )malloc(sizeof(VALUE* )*numValues);
    for (int i=0; i<numValues; i++)
        valArr[i] = (VALUE* )malloc(sizeof(VALUE));

    for (int i=0; i<numValues; i++)
    {
        scanf("%d",&(valArr[i]->value));
    }

    merge_sort((void** )valArr, numValues, val_compare);
#if 1
    for (int i=0; i<numValues; i++)
    {
        printf("%d",valArr[i]->value);
    }
    printf("\n");
#endif
    for (int i=0; i<numValues; i++)
        free(valArr[i]);
    free(valArr);

    return 0;
}


