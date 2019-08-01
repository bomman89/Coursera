#include "stdio.h"
#include "stdlib.h"
#include "memory.h"
#include "time.h"
#include "math.h"

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


typedef struct _POINT_
{
    long long int x;
    long long int y;
} POINT;

int point_compare(void* ele1, void* ele2)
{
    POINT* pt1 = (POINT* )ele1;
    POINT* pt2 = (POINT* )ele2;

    if (pt1->x > pt2->x)
        return 1;
    else if (pt1->x < pt2->x)
        return -1;
    else
    {
        if (pt1->y > pt2->y)
            return 1;
        else if (pt1->y < pt2->y)
            return -1;
    }
    return 0;
}




#define LL_INT_MAX (0x7FFFFFFFFFFFFFFF)
#define MIN(a,b)   ((a <= b) ? a : b)

long long int distance_between_squared(POINT** points, unsigned int pt1Idx, unsigned int pt2Idx)
{
    long long int distance = (points[pt1Idx]->x - points[pt2Idx]->x)*(points[pt1Idx]->x - points[pt2Idx]->x);
    distance += (points[pt1Idx]->y - points[pt2Idx]->y)*(points[pt1Idx]->y - points[pt2Idx]->y);
    return distance;
}

double find_min_distance(POINT** points, unsigned int numPoints)
{
    if (numPoints == 2)
    {
        return sqrt((double)distance_between_squared(points, 0, 1));
    }

    int subArSize, arIdx;
    long long int minDistanceOfAllSubArr = LL_INT_MAX;
    for (arIdx = 0; (arIdx+1)<numPoints; arIdx += 2)
    {
        long long int currDistance = distance_between_squared(points, arIdx, arIdx+1);
        if (currDistance < minDistanceOfAllSubArr)
            minDistanceOfAllSubArr = currDistance;
#ifdef VALIDATE_SOLN
        printf("%lld ",currDistance);
#endif
    }
#ifdef VALIDATE_SOLN
    printf("\n");
#endif

    for (subArSize=4; (subArSize/2)<numPoints; subArSize*=2)
    {
        for (arIdx=0; (arIdx+subArSize/2)<numPoints; arIdx += subArSize)
        {
            int startSubAr1Idx = arIdx + subArSize/2 - 1;
            int startSubAr2Idx = arIdx + subArSize/2;

            while (startSubAr1Idx >= arIdx)
            {
                long long int xDistanceSquared
                    = (points[startSubAr1Idx]->x - points[startSubAr2Idx]->x)
                      *(points[startSubAr1Idx]->x - points[startSubAr2Idx]->x);

                if (xDistanceSquared > minDistanceOfAllSubArr)
                {
                    if (startSubAr2Idx == (arIdx + subArSize/2))
                        break;
                    startSubAr1Idx--;
                    startSubAr2Idx = arIdx + subArSize/2;
                    continue;
                }

                long long int currDistance = distance_between_squared(points, startSubAr1Idx, startSubAr2Idx);

                if (currDistance < minDistanceOfAllSubArr)
                    minDistanceOfAllSubArr = currDistance;

                startSubAr2Idx++;
                if ((startSubAr2Idx >= numPoints)
                    || (startSubAr2Idx >= (arIdx+subArSize)))
                {
                    startSubAr1Idx--;
                    startSubAr2Idx = arIdx + subArSize/2;
                }
            }
        }
    }

    double minDistance = sqrt((double)minDistanceOfAllSubArr);
    return minDistance;
}

double find_min_distance_brute(POINT** points, unsigned int numPoints)
{
    long long int minDistance = LL_INT_MAX;
    for (int i=0; i<numPoints; i++)
    {
        for (int j=i+1; j<numPoints; j++)
        {
            long long int currentMinDistance;
            currentMinDistance = distance_between_squared(points, i, j);
            if (currentMinDistance < minDistance)
                minDistance = currentMinDistance;
        }
    }
    return sqrt((double)minDistance);
}

int main(int argc, char** argv)
{
    unsigned int numPoints;

#ifdef FINAL_SOLN
    scanf("%d",&numPoints);
#elif defined(VALIDATE_SOLN)
    numPoints = 20;
#endif
    POINT** points;
    srand(time(0));

    points = (POINT** )malloc(sizeof(POINT* )*numPoints);
    for (int i=0; i<numPoints; i++)
    {
        points[i] = (POINT* )malloc(sizeof(POINT));
#ifdef FINAL_SOLN
        scanf("%lld %lld",&(points[i]->x), &(points[i]->y));
#elif defined(VALIDATE_SOLN)
        points[i]->x = rand()%100;
        points[i]->y = rand()%100;
        printf("%lld %lld\n",(points[i]->x), (points[i]->y));
#endif
    }

    merge_sort((void** )points, numPoints, point_compare);
    printf("%lf\n",find_min_distance(points, numPoints));
#ifdef VALIDATE_SOLN
    printf("%lf\n",find_min_distance_brute(points, numPoints));
#endif
    return 0;
}
