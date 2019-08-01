#include "stdio.h"
#include "stdlib.h"
#include "memory.h"

typedef struct _HEAP_INFO_
{
    void** elements;
    int (*compareTo)(void* , void* );
    int currNumElements;
    int currSize;
} HEAP_INFO;

HEAP_INFO* heap_init(int (*compareFunc)(void* , void* ))
{
    HEAP_INFO* heapInfo = (HEAP_INFO* )malloc(sizeof(HEAP_INFO));

    heapInfo->currNumElements = 1;
    heapInfo->currSize = 2;
    heapInfo->elements = (void** )malloc(sizeof(void* )*2);
    heapInfo->compareTo = compareFunc;

    return heapInfo;
}

void heap_free(HEAP_INFO* heapInfo)
{
    free(heapInfo->elements);
    free(heapInfo);
}

void heap_resize(HEAP_INFO* heapInfo)
{
    int newHeapSize = heapInfo->currSize*2;

    void** newArr = (void** )malloc(sizeof(void* )*newHeapSize);
    memcpy(newArr, heapInfo->elements, (sizeof(void* )*heapInfo->currSize));
    free(heapInfo->elements);
    heapInfo->elements = newArr;
    heapInfo->currSize = newHeapSize;
}

void swim(HEAP_INFO* heapInfo, int start)
{
    int k = start;

    while (k > 1)
    {
        void* element1 = heapInfo->elements[k/2];
        void* element2 = heapInfo->elements[k];
        if (heapInfo->compareTo(element1, element2) < 0)
        {
            heapInfo->elements[k] = element1;
            heapInfo->elements[k/2] = element2;
        }
        else
            break;

        k = k/2;
    }
}

void sink(HEAP_INFO* heapInfo, int start)
{
    int k = start;

    while (heapInfo->currNumElements > (2*k))
    {
        void* child1 = heapInfo->elements[2*k];
        void* greaterChild = child1;
        int greaterChildElementIdx = 2*k;

        if (heapInfo->currNumElements > (2*k + 1))
        {
            void* child2 = heapInfo->elements[2*k + 1];

            if (heapInfo->compareTo(child1, child2) < 0)
            {
                greaterChild = child2;
                greaterChildElementIdx = 2*k + 1;
            }

            if (heapInfo->compareTo(heapInfo->elements[k], greaterChild) < 0)
            {
                heapInfo->elements[greaterChildElementIdx] = heapInfo->elements[k];
                heapInfo->elements[k] = greaterChild;
            }
            else
                break;
        }
        else
        {
            if (heapInfo->compareTo(heapInfo->elements[k], greaterChild) < 0)
            {
                heapInfo->elements[greaterChildElementIdx] = heapInfo->elements[k];
                heapInfo->elements[k] = greaterChild;
            }
            else
                break;
        }
        k = greaterChildElementIdx;
    }
}

void heap_insert(HEAP_INFO* heapInfo, void* element)
{
    if (heapInfo->currNumElements >= heapInfo->currSize)
    {
        heap_resize(heapInfo);
    }

    heapInfo->elements[heapInfo->currNumElements] = element;
    swim(heapInfo, heapInfo->currNumElements);
    heapInfo->currNumElements++;
}

void* heap_root_get(HEAP_INFO* heapInfo)
{
    if (heapInfo->currNumElements == 1)
        return NULL;

    void* retElement = heapInfo->elements[1];
    heapInfo->currNumElements--;
    heapInfo->elements[1] = heapInfo->elements[heapInfo->currNumElements];
    heapInfo->elements[heapInfo->currNumElements] = NULL;
    sink(heapInfo, 1);
    return retElement;
}

int int_compare(void* ele1, void* ele2)
{
    if (*(int* )ele1 > *(int* )ele2)
        return 1;
    else if (*(int* )ele1 < *(int* )ele2)
        return -1;

    return 0;
}

int main(int argc, char** argv)
{
    unsigned int n;
    scanf("%d",&n);
    int* a;
    int* b;
    a = (int* )malloc(sizeof(int)*n);
    b = (int* )malloc(sizeof(int)*n);

    for (int i=0; i<n; i++) {
        scanf("%d",&a[i]);
    }

    for (int i=0; i<n; i++) {
        scanf("%d",&b[i]);
    }


    HEAP_INFO* heapInfoA = heap_init(int_compare);
    HEAP_INFO* heapInfoB = heap_init(int_compare);

    for (int i=0; i<n; i++) {
        heap_insert(heapInfoA, (void* )(&a[i]));
        heap_insert(heapInfoB, (void* )(&b[i]));
    }

    long long int maxSum = 0;
    for (int i=0; i<n; i++) {
        int currA = *(int* )heap_root_get(heapInfoA);
        int currB = *(int* )heap_root_get(heapInfoB);
        maxSum += ((long long)currA*currB);
    }
    printf("%lld\n",maxSum);

    heap_free(heapInfoA);
    heap_free(heapInfoB);

    free(a);
    free(b);
}
