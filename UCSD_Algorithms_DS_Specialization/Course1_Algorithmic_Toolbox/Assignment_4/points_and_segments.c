#include "stdio.h"
#include "stdlib.h"
#include "memory.h"
#include "time.h"

/*
 * Defining VALIDATE_SOLN is going to generate random segments and query points.
 * Brute force solution results are computed and compared against the optimal solution.
 */
// #define VALIDATE_SOLN

#define FINAL_SOLN

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
    if (numElements <= 1)
        return;

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

int segment_point_compare(void* ele1, void* ele2)
{
    long long int* segPt1 = (long long int* )ele1;
    long long int* segPt2 = (long long int* )ele2;

    if (*segPt1 > *segPt2)
        return 1;
    else if (*segPt1 < *segPt2)
        return -1;

    return 0;
}

unsigned int segment_points_lt_or_eq(long long int** segPtArr, int numSegPts, long long int pivotPt)
{
    unsigned int start=0;
    unsigned int end=numSegPts-1;
    unsigned int mid;

    if (*segPtArr[0] > pivotPt)
        return 0;

    if (*segPtArr[end] <= pivotPt)
        return numSegPts;

    while (end > start)
    {
        mid = (start + end)/2;
        if ((*segPtArr[mid]) > pivotPt)
        {
            end = mid;
            continue;
        }
        else
        {
            if ((*segPtArr[mid+1]) > pivotPt)
                return (mid+1);
            start = mid;
        }
    }
}

unsigned int segment_points_lt(long long int** segPtArr, int numSegPts, long long int pivotPt)
{
    unsigned int start=0;
    unsigned int end=numSegPts-1;
    unsigned int mid;

    if (*segPtArr[0] >= pivotPt)
        return 0;

    if (*segPtArr[end] < pivotPt)
        return numSegPts;

    while (end > start)
    {
        mid = (start + end)/2;
        if ((*segPtArr[mid]) >= pivotPt)
        {
            end = mid;
            continue;
        }
        else
        {
            if ((*segPtArr[mid+1]) >= pivotPt)
                return (mid+1);
            start = mid;
        }
    }
}

void optimal_soln(long long int** segmentStart,
                  long long int** segmentEnd,
                  long long int* queryPoints,
                  unsigned int numQueryPoints,
                  unsigned int numSegments
#ifdef VALIDATE_SOLN
                  ,unsigned int* result
#endif
                  )
{
    merge_sort((void** )segmentStart, numSegments, segment_point_compare);
    merge_sort((void** )segmentEnd, numSegments, segment_point_compare);

#if 0
    for (int i=0; i<numSegments; i++)
        printf("%lld ",*segmentStart[i]);
    printf("\n");

    for (int i=0; i<numSegments; i++)
        printf("%lld ",*segmentEnd[i]);
    printf("\n");
#endif

    for (int i=0; i<numQueryPoints; i++)
    {
        unsigned int numStartPointsLtOrEq = segment_points_lt_or_eq(segmentStart, numSegments, queryPoints[i]);
        unsigned int numEndPointsLt = segment_points_lt(segmentEnd, numSegments, queryPoints[i]);
#ifdef VALIDATE_SOLN
        result[i] = (numStartPointsLtOrEq-numEndPointsLt);
#elif defined(FINAL_SOLN)
        printf("%d ",(numStartPointsLtOrEq-numEndPointsLt));
#endif
    }

#ifdef FINAL_SOLN
    printf("\n");
#endif

}

void brute_soln(long long int** segmentStart,
                long long int** segmentEnd,
                long long int* queryPoints,
                unsigned int numQueryPoints,
                unsigned int numSegments,
                unsigned int* result)
{

    for (int i=0; i<numQueryPoints; i++)
    {
        result[i] = 0;
        for (int j=0; j<numSegments; j++)
        {
            if ((queryPoints[i] >= *segmentStart[j])
                && (queryPoints[i] <= *segmentEnd[j]))
            {
                result[i] = result[i] + 1;
            }
        }
    }

}

// const long long int segStart[] = {74, 13, 68, 4, 9, 84, 30, 46, 41, 36};
// const long long int segEnd[] = {89, 27, 92, 62, 51, 87, 45, 82, 90, 54};

int main(int argc, char** argv)
{
    unsigned int numSegments, numQueryPoints;
    long long int** segmentStart;
    long long int** segmentEnd;
    long long int* queryPoints;

#ifdef FINAL_SOLN
    scanf("%d %d",&numSegments, &numQueryPoints);
#endif

#ifdef VALIDATE_SOLN
    numSegments = 20;
    numQueryPoints = 5;
#endif

    segmentStart = (long long int**)malloc(numSegments*sizeof(long long int* ));
    segmentEnd = (long long int**)malloc(numSegments*sizeof(long long int* ));
    queryPoints = (long long int* )malloc(numSegments*sizeof(long long int));

    srand(time(0));

    for (int i=0; i<numSegments; i++)
    {
        segmentStart[i] = (long long int* )malloc(sizeof(long long int));
        segmentEnd[i] = (long long int* )malloc(sizeof(long long int));
#ifdef VALIDATE_SOLN
        *segmentStart[i] = rand()%100;
        *segmentEnd[i] = (rand()%(100 - *segmentStart[i] + 1)) + *segmentStart[i];
        printf("%lld %lld\n",*segmentStart[i], *segmentEnd[i]);
#elif defined(FINAL_SOLN)
        scanf("%lld %lld",segmentStart[i], segmentEnd[i]);
#endif
    }

    for (int i=0; i<numQueryPoints; i++)
    {
#ifdef VALIDATE_SOLN
        queryPoints[i] = rand()%100;
        printf("%lld ",queryPoints[i]);
#elif defined(FINAL_SOLN)
        scanf("%lld",&queryPoints[i]);
#endif
    }

#ifdef VALIDATE_SOLN
    printf("\n");
    unsigned int* result_optimal = (unsigned int* )malloc(sizeof(unsigned int)*numQueryPoints);
    unsigned int* result_brute = (unsigned int* )malloc(sizeof(unsigned int)*numQueryPoints);
    brute_soln(segmentStart, segmentEnd, queryPoints, numQueryPoints, numSegments, result_brute);
    optimal_soln(segmentStart, segmentEnd, queryPoints, numQueryPoints, numSegments, result_optimal);
#elif defined(FINAL_SOLN)
    optimal_soln(segmentStart, segmentEnd, queryPoints, numQueryPoints, numSegments);
#endif

#ifdef VALIDATE_SOLN
    for (int i=0; i<numQueryPoints; i++)
    {
        if (result_optimal[i] != result_brute[i])
            printf("%lld %d %d error\n",queryPoints[i], result_optimal[i], result_brute[i]);
    }
#endif

    for (int i=0; i<numSegments; i++)
    {
        free(segmentStart[i]);
        free(segmentEnd[i]);
    }
    free(segmentStart);
    free(segmentEnd);
    free(queryPoints);


    return 0;
}
