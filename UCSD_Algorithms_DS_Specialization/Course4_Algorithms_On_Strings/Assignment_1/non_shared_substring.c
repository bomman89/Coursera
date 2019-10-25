#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include "time.h"
#include "stddef.h"
#include "stdbool.h"

typedef unsigned long long int U64;

typedef struct _LINKED_LIST_NODE_
{
    struct _LINKED_LIST_NODE_* next;
    struct _LINKED_LIST_NODE_* prev;
} LINKED_LIST_NODE;

void list_init(LINKED_LIST_NODE* list)
{
    list->next = list;
    list->prev = list;
}

void list_insert(LINKED_LIST_NODE* list, LINKED_LIST_NODE* node)
{
    node->prev = list->prev;
    node->next = list->next;
    list->prev->next = node;
    list->prev = node;
}

LINKED_LIST_NODE* list_remove(LINKED_LIST_NODE* list)
{
    LINKED_LIST_NODE* nodeToReturn = list->next;
    nodeToReturn->prev->next = nodeToReturn->next;
    nodeToReturn->next->prev = nodeToReturn->prev;
    nodeToReturn->next = nodeToReturn;
    nodeToReturn->prev = nodeToReturn;
    return nodeToReturn;
}

bool list_is_empty(LINKED_LIST_NODE* list)
{
    return (list->next == list);
}

#define MAX_STR_LENGTH (2000)
#define MAX_SUFFIX_TREE_NODES (4*(MAX_STR_LENGTH))
#define MAX_CHARS (27)
typedef struct _SUFFIX_TRIE_NODE_
{
    int start;
    int end;
    struct _SUFFIX_TRIE_NODE_* children[MAX_CHARS];
    LINKED_LIST_NODE node;
} SUFFIX_TRIE_NODE;

typedef struct _SUFFIX_TREE_INFO_
{
    SUFFIX_TRIE_NODE* suffixTrieNodePool;
    SUFFIX_TRIE_NODE* suffixTreeRoot;
    char* origStr;
    LINKED_LIST_NODE suffixTrieNodeFreeList;
} SUFFIX_TREE_INFO;
SUFFIX_TREE_INFO gSuffixTreeInfo;

void suffix_trie_node_pool_init()
{
    list_init(&(gSuffixTreeInfo.suffixTrieNodeFreeList));

    gSuffixTreeInfo.suffixTrieNodePool =
        (SUFFIX_TRIE_NODE* )malloc(sizeof(SUFFIX_TRIE_NODE)*MAX_SUFFIX_TREE_NODES);

    memset(gSuffixTreeInfo.suffixTrieNodePool, 0, (sizeof(SUFFIX_TRIE_NODE)*MAX_SUFFIX_TREE_NODES));

    for (int i=0; i<MAX_SUFFIX_TREE_NODES; i++)
        list_insert(&(gSuffixTreeInfo.suffixTrieNodeFreeList)
            , &(gSuffixTreeInfo.suffixTrieNodePool[i].node));
    gSuffixTreeInfo.suffixTreeRoot = NULL;
}

void suffix_trie_node_pool_free()
{
    free(gSuffixTreeInfo.suffixTrieNodePool);
}


SUFFIX_TRIE_NODE* suffix_trie_node_alloc()
{
    if (list_is_empty(&(gSuffixTreeInfo.suffixTrieNodeFreeList)))
    {
        printf("List Empty - ERROR\n");
        return NULL;
    }
    LINKED_LIST_NODE* suffixTrieLlNode = list_remove(&(gSuffixTreeInfo.suffixTrieNodeFreeList));

    SUFFIX_TRIE_NODE* suffixTrieNode =
        (SUFFIX_TRIE_NODE* )((U64)suffixTrieLlNode - offsetof(SUFFIX_TRIE_NODE, node));

    return suffixTrieNode;
}


void suffix_tree_insert(char* str, unsigned int suffixIdx)
{
    // printf("%s %d\n",str,suffixIdx);
    if (gSuffixTreeInfo.suffixTreeRoot == NULL)
    {
        gSuffixTreeInfo.suffixTreeRoot = suffix_trie_node_alloc();
        gSuffixTreeInfo.suffixTreeRoot->end = -1;

        // printf("%c\n",str[0]);
        gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A'] = suffix_trie_node_alloc();
        gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A']->start = 1;
        gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A']->end = strlen(str)-1;
    }
    else
    {
        SUFFIX_TRIE_NODE* currNode = gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A'];
        if (currNode == NULL)
        {
            // printf("%c\n",str[0]);
            gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A'] = suffix_trie_node_alloc();
            gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A']->start = suffixIdx+1;
            gSuffixTreeInfo.suffixTreeRoot->children[str[0]-'A']->end = strlen(gSuffixTreeInfo.origStr)-1;
        }
        else {
            unsigned int currStrStartIdx=1;
            while (1)
            {
#if 0
                if (suffixIdx == 29)
                    printf("%s st %d en %d\n",&str[currStrStartIdx],currNode->start, currNode->end);
#endif
                unsigned int origStrStartIdx = currNode->start;
                if (currStrStartIdx >= strlen(str))
                    break;
                else if (origStrStartIdx > currNode->end)
                {
                    if (currNode->children[(str[currStrStartIdx]-'A')] == NULL)
                    {
                        SUFFIX_TRIE_NODE* newNode = suffix_trie_node_alloc();
                        newNode->start = currStrStartIdx+suffixIdx+1;
                        newNode->end = strlen(gSuffixTreeInfo.origStr)-1;
                        currNode->children[(str[currStrStartIdx]-'A')] = newNode;
                        break;
                    }
                    currNode = currNode->children[(str[currStrStartIdx]-'A')];
                    currStrStartIdx++;
                    continue;
                }

                while ((currStrStartIdx < strlen(str))
                    && (origStrStartIdx <= currNode->end))
                {
                    if (str[currStrStartIdx] == gSuffixTreeInfo.origStr[origStrStartIdx])
                    {
                        currStrStartIdx++;
                        origStrStartIdx++;
                    }
                    else
                        break;
                }

                if (currStrStartIdx >= strlen(str))
                    break;
                else if (origStrStartIdx > currNode->end)
                {
                    if (currNode->children[(str[currStrStartIdx]-'A')] == NULL)
                    {
                        SUFFIX_TRIE_NODE* newNode = suffix_trie_node_alloc();
                        newNode->start = currStrStartIdx+suffixIdx+1;
                        newNode->end = strlen(gSuffixTreeInfo.origStr)-1;
                        currNode->children[(str[currStrStartIdx]-'A')] = newNode;
                        break;
                    }
                    currNode = currNode->children[(str[currStrStartIdx]-'A')];
                    currStrStartIdx++;
                    continue;
                }
                else if (origStrStartIdx <= currNode->end)
                {
                    SUFFIX_TRIE_NODE* newNode = suffix_trie_node_alloc();
                    newNode->start = origStrStartIdx+1;
                    newNode->end = currNode->end;
                    memcpy(newNode->children, currNode->children, sizeof(SUFFIX_TRIE_NODE* )*MAX_CHARS);
                    memset(currNode->children, 0, sizeof(SUFFIX_TRIE_NODE* )*MAX_CHARS);
                    currNode->children[(gSuffixTreeInfo.origStr[origStrStartIdx]-'A')]
                        = newNode;
                    currNode->children[(str[currStrStartIdx]-'A')] = suffix_trie_node_alloc();
                    currNode->children[(str[currStrStartIdx]-'A')]->start = currStrStartIdx+suffixIdx+1;
                    currNode->children[(str[currStrStartIdx]-'A')]->end = strlen(gSuffixTreeInfo.origStr)-1;
                    currNode->end = origStrStartIdx-1;
                    break;
                }
            }
        }
    }
}

void suffix_tree_print(SUFFIX_TRIE_NODE* currNode);

void suffix_tree_generate(char* str)
{
    gSuffixTreeInfo.origStr = str;
    suffix_trie_node_pool_init();

    for (unsigned int suffixIdx=0; suffixIdx<strlen(str); suffixIdx++)
    {
        suffix_tree_insert(&str[suffixIdx], suffixIdx);
        // printf("suffixIdx %d\n",suffixIdx);
        // suffix_tree_print(gSuffixTreeInfo.suffixTreeRoot);
    }
}

void suffix_tree_print(SUFFIX_TRIE_NODE* currNode)
{
    if (currNode == NULL)
        return;


    printf("%d %d\n",currNode->start, currNode->end);

    suffix_tree_print(currNode->children['A'-'A']);
    suffix_tree_print(currNode->children['C'-'A']);
    suffix_tree_print(currNode->children['G'-'A']);
    suffix_tree_print(currNode->children['T'-'A']);
}

int suffix_tree_match(char* str)
{
    SUFFIX_TRIE_NODE* currNode = gSuffixTreeInfo.suffixTreeRoot;
    int matchingLen = 0;

    // printf("len %d\n",strlen(str));
    while (currNode != NULL)
    {
        // printf("st %d en %d\n", currNode->start, currNode->end);
        if (currNode->start <= currNode->end)
        {
            int currSubStrStart = currNode->start;

            while (currSubStrStart <= currNode->end)
            {
                if (gSuffixTreeInfo.origStr[currSubStrStart] != str[matchingLen])
                    return matchingLen;
                currSubStrStart++;
                matchingLen++;
                if (matchingLen >= strlen(str))
                    return (matchingLen);
            }
        }

        currNode = currNode->children[str[matchingLen]-'A'];
        if (currNode != NULL)
            matchingLen++;
        else
            return matchingLen;

        if (matchingLen >= strlen(str))
            return matchingLen;
    }

}
int main(int argc, char** argv)
{
    char* str1;
    char* str2;

    str1 = (char* )malloc((MAX_STR_LENGTH+2)*sizeof(char));
    str2 = (char* )malloc((MAX_STR_LENGTH+2)*sizeof(char));

    scanf("%s %s", str1, str2);

#if 0
    int strLenToTest=2000;
    int numTests=100;
    char testChars[] = {'A', 'C', 'G', 'T'};
    for (int i=0; i<numTests; i++)
    {
        suffix_trie_node_pool_free();
        srand(i+time(0));
        for (int j=0; j<strLenToTest; j++)
        {
            str2[j] = testChars[rand()%4];
        }
        str2[strLenToTest] = '\0';
        printf("%s\n",str2);
        suffix_tree_generate(str2);
    }
#endif
    suffix_tree_generate(str2);

    int minMatchingLen=-1;
    int minSuffixIdx=0;
    for (int i=0; i<strlen(str1); i++)
    {
        char* suffixStr = &str1[i];
        int matchingLen = suffix_tree_match(suffixStr);

        if (minMatchingLen == -1)
        {
            // printf("%d %d\n",i,matchingLen);
            minMatchingLen = matchingLen;
            minSuffixIdx = i;
        }
        else if ((matchingLen <= minMatchingLen) && (matchingLen < strlen(suffixStr)))
        {
            // printf("%d %d\n",i,matchingLen);
            minMatchingLen = matchingLen;
            minSuffixIdx = i;
        }
    }

    char minSuffixStr[2001];
    memcpy(minSuffixStr, &str1[minSuffixIdx], (minMatchingLen+1)*sizeof(char));
    minSuffixStr[minMatchingLen+1] = '\0';
    printf("%s\n",minSuffixStr);
    suffix_trie_node_pool_free();
    return 0;

}
