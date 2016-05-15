import java.io.*;

public class MaxCut
{ public static char[] vertex = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
    public static char[][] edges = 
    {
        {'C', 'F', 'D', 'B'},
        {'A', 'D', 'E'},
        {'A', 'F'},
        {'A', 'B', 'E', 'F', 'G'},
        {'B', 'D', 'G'},
        {'C', 'A', 'D', 'G'},
        {'D', 'E', 'F'}
    };

    public static char s1[] = {'A', 'B', 'C', 'E'};
    public static char s2[] = {'D', 'F', 'G'};

    public static void main(String[] args)
    {
        // assume the subgraphs have already been made
        char[] testEdges = getEdges('A');

        // compute the inner degree of all the nodes
        int[] innerDegree = {0, 0, 0, 0, 0, 0, 0};
        for(int ii = 0; ii < vertex.length; ii++)
        {
            innerDegree[ii] = innerDegree(vertex[ii]);
        }

        for(int jj = 0; jj < innerDegree.length; jj++)
        {
            char tmpVertex = extractMax(innerDegree);
            int currentCut = 0;
            System.out.println("#" + jj + " select " + tmpVertex + " cur cut = " + calculateCut(edges));


            System.out.println("\tmoving a into other set creates a cut of " + 

        }
        
    }

    /*
     * for each edge, check if it's within the same cut.
     */
    public static int calculateCut(char[][] edges)
    {
        int cut = 0;

        for (int ii = 0; ii < count(vertex); ii++)
        {
            char firstNode = vertex[ii];
            for (int jj = 0; jj < count(edges[ii]); jj++)
            {
                if (edges[ii][jj] != '\u0000')
                {
                    if ( ! shareSet(firstNode, edges[ii][jj]))
                    {
                        cut++;
                        // set the edge to null
                        char[] connection = edges[getPos(edges[ii][jj])];
                        for (int kk = 0; kk < count(connection); kk++)
                        {
                            if (connection[kk] == firstNode)
                            {
                                edges[getPos(edges[ii][jj])][kk] = '\u0000';
                            }
                        }
                    }
                }
            }
        }

        return cut;
    }

    public static char extractMax(int[] innerDegree)
    {
        int max = -1;
        int maxPos = -1;

        for( int ii = 0; ii < innerDegree.length; ii++)
        {
            /*
             * note the lack of equals sign.
             * take it in alphabetical order if the same
             */
            if (innerDegree[ii] > max)
            {
                max = innerDegree[ii];
                maxPos = ii;
            }
        }

        // remove the max pos
        innerDegree[maxPos] = -1;

        return vertex[maxPos];
    }

    /*
     * calculate the degree of the vertex only taking
     * into account nodes within the same sumgraph
     */
    public static int innerDegree(char inVertex)
    {
        int innerDegree = 0;
        char[] tmpSet = s2;

        // loop through all of this vertex's edges check if they share a set
        if (inSet(inVertex, s1))
        {
            tmpSet = s1;
        }

        char[] vertexEdges = getEdges(inVertex);
        for(int ii = 0; ii < count(vertexEdges); ii++)
        {
            if (inSet(vertexEdges[ii], tmpSet))
            {
                innerDegree++;
            }
        }
        return innerDegree;
    }

    /*
     * given a char return a set of connected nodes.
     */
    public static char[] getEdges(char inVertex)
    {
        int pos = getPos(inVertex);
        return edges[pos];
    }

    /*
     * find the pos of the char in the vertex array
     */
    public static int getPos(char inVertex)
    {
        int pos = -1;

        for (int ii = 0; ii < count(vertex); ii++)
        {
            if (vertex[ii] == inVertex)
            {
                pos = ii;
            }
        }

        return pos;
    }

    /*
     * return true if the vertex is in the set.
     */
    public static boolean inSet(char inVertex, char set[])
    {
        boolean inSet = false;
        for(int ii = 0; ii < count(set); ii++)
        {
            if (set[ii] == inVertex)
            {
                inSet = true;
            }
        }

        return inSet;
    }

    /*
     * return true if the nodes are within the same set.
     */
    public static boolean shareSet(char charOne, char charTwo)
    {
        boolean share = false;

        /*
         * if both true they share s1, given we can only have at
         * most two sets
         */
        if (inSet(charOne, s1) == inSet(charTwo, s1))
        {
            share = true;
        }  

        return share;
    }

    /*
     * get the count of cells that are actually used
     * in the char array
     */
    public static int count(char array[])
    {
        int count = 0;
        for (int ii = 0; ii < array.length; ii++)
        {
            if (array[ii] != '\u0000')
            {
                count++;
            }
        }

        return count;
    }
}
