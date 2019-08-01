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

typedef struct _SEGMENT_POINT_
{
    unsigned int pt;
    unsigned int start;
    struct _SEGMENT_POINT_* other;
} SEGMENT_POINT;

int seg_point_compare(void* ele1, void* ele2)
{
    SEGMENT_POINT* segPt1 = (SEGMENT_POINT* )ele1;
    SEGMENT_POINT* segPt2 = (SEGMENT_POINT* )ele2;

    if (segPt1->pt > segPt2->pt)
        return 1;
    else if (segPt1->pt < segPt2->pt)
        return -1;

    return 0;
}


int main(int argc, char** argv)
{
    int numSegments;


    // FILE* pFile;
    // pFile = fopen("test_case_3.txt", "r+");

    scanf("%d",&numSegments);

    SEGMENT_POINT** segPtArr;
    segPtArr = (SEGMENT_POINT** )malloc(sizeof(SEGMENT_POINT* )*numSegments*2);
    for (int i=0; i<numSegments*2; i++)
        segPtArr[i] = (SEGMENT_POINT* )malloc(sizeof(SEGMENT_POINT));

    for (int i=0; i<numSegments; i++)
    {
        scanf("%d %d",&(segPtArr[2*i]->pt), &(segPtArr[2*i+1]->pt));
        segPtArr[2*i]->start = 1;
        segPtArr[2*i+1]->start = 0;
        segPtArr[2*i]->other = segPtArr[2*i+1];
        segPtArr[2*i+1]->other = segPtArr[2*i];
    }

    merge_sort((void** )segPtArr, numSegments*2, seg_point_compare);
#if 0
    for (int i=0; i<(numSegments*2); i++)
    {
        printf("%d %d ",segPtArr[i]->pt, segPtArr[i]->start);
    }
    printf("\n");
#endif

    int numPoints=0;
    SEGMENT_POINT* pSegPt;
    int* points;
    points = (int* )malloc(sizeof(int)*numSegments);
    memset(points, 0, sizeof(int)*numSegments);

    int i=0;
    while (i < (numSegments*2))
    {
        pSegPt = segPtArr[i];
        i++;
        if (pSegPt->start == 0) {
            int searchPt = pSegPt->other->pt;
            if ((points[numPoints-1] < searchPt)
                || (numPoints == 0))
            {
                points[numPoints] = pSegPt->pt;
                numPoints++;
            }
        }
    }

    printf("%d\n",numPoints);

    for (int i=0; i<numPoints; i++)
    {
        printf("%d ",points[i]);
    }
    printf("\n");

    free(points);
    for (int i=0; i<numSegments*2; i++)
        free(segPtArr[i]);
    free(segPtArr);

    return 0;
}

