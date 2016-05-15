#include<stdio.h>
#include<time.h>
#include<stdlib.h>

/*
 * given the input graph G with vertex's V and edges
 * E generate all of the possible cuts for the graph.
 */
int main(int argc, char* argv[])
{
    int ii;

    srand(time(NULL));
    for(ii = 1; ii < argc; ii++)
    {
        printf("%s", argv[ii]);

        // 50% chance of s1 v s2
        if (rand() % 2 == 1)
        {
            printf(" = s1\n");
        }
        else
        {
            printf(" = s2\n");
        }
    }
}
