import java.io.*;
import java.util.Random;

public class MaxCut
{ 
    public static char[] vertex = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
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

    public static char s1[] = {'A', 'B', 'C', 'E', '\u0000', '\u0000', '\u0000'};
    public static char s2[] = {'D', 'F', 'G', '\u0000', '\u0000', '\u0000', '\u0000'};

    public static void printAllSubsets(char[] arr) {
        byte[] counter = new byte[arr.length];
        int counting = 1;

        int largestCut = 0;
        int largestPos = 0;

        while (true) {
            int tmpCutSize;
            s1 = new char[7];
            s2 = new char[7];

            // let s1 contain all vertex's V
            for (int ii = 0; ii < vertex.length; ii++)
            {
                addVertex(vertex[ii], s1);
            }
            // Print combination
            System.out.print(counting + " - ");
            for (int i = 0; i < counter.length; i++) {
                if (counter[i] != 0)
                {
                    addVertex(arr[i], s2);
                    removeVertex(arr[i], s1);
                    System.out.print(arr[i] + " ");
                }
            }

            System.out.println();
            outputSet(s1);
            outputSet(s2);

            tmpCutSize = calculateCut(edges);
            if (tmpCutSize > largestCut)
            {
                largestCut = tmpCutSize;
                largestPos = counting;
            }

            System.out.print("cut size = " + calculateCut(edges) + " ");

            counting++;

            // Increment counter
            int i = 0;
            while (i < counter.length && counter[i] == 1)
                counter[i++] = 0;
            if (i == counter.length)
                break;

            if (counting == 65)
                break;

            counter[i] = 1;
        }

        System.out.println("");
        System.out.println("The largest cut was " + largestCut + " found at pos: " + largestPos);
        System.out.println("");
    }

    public static void main(String[] args)
    {
        Random rand = new Random();

        s1 = new char[7];
        s2 = new char[7];

        printAllSubsets(vertex);

        for (int ll = 0; ll < count(vertex); ll++)
        {
            // generate either 0 or 1
            if (rand.nextInt((1) + 1) % 2 == 0)
            {
                addVertex(vertex[ll], s1);
            }
            else
            {
                addVertex(vertex[ll], s2);
            }
        }
        System.out.println("Randomly split the graph into:");
        outputSet(s1);
        outputSet(s2);
        System.out.println("");


        char[] testEdges = getEdges('A');

        // compute the inner degree of all the nodes
        int[] innerDegree = {0, 0, 0, 0, 0, 0, 0};
        for(int ii = 0; ii < vertex.length; ii++)
        {
            innerDegree[ii] = innerDegree(vertex[ii]);
        }

        int currentCut = calculateCut(edges);
        for(int jj = 0; jj < innerDegree.length; jj++)
        {
            char tmpVertex = extractMax(innerDegree);
            System.out.println("#" + jj + " select " + tmpVertex + " cur cut = " + calculateCut(edges));

            char[] oldS1 = new char[7];
            char[] oldS2 = new char[7];

            System.arraycopy(s1, 0, oldS1, 0, s1.length);
            System.arraycopy(s2, 0, oldS2, 0, s2.length);

            if (inSet(tmpVertex, s1))
            {
                s1 = removeVertex(tmpVertex, s1);
                s2 = addVertex(tmpVertex, s2);
            }
            else
            {
                s1 = addVertex(tmpVertex, s1);
                s2 = removeVertex(tmpVertex, s2);
            }

            outputSet(s1);
            outputSet(s2);
            if (currentCut < calculateCut(edges))
            {
                System.out.println("\t moving " + tmpVertex + " given it's cut of: " + calculateCut(edges));
                currentCut = calculateCut(edges);
            }
            else
            {
                System.out.println("\t not moving " + tmpVertex + " given it's cut of " + calculateCut(edges));
                System.arraycopy(oldS1, 0, s1, 0, oldS1.length);
                System.arraycopy(oldS2, 0, s2, 0, oldS2.length);
            }
        }

        System.out.println("-----------------");
        System.out.println("Found a cut of size " + currentCut);
        outputSet(s1);
        outputSet(s2);
        System.out.println("");
        
    }

    public static void outputSet(char[] set)
    {
        System.out.print(" {");
        for(int ii = 0; ii < count(set); ii++)
        {
            System.out.print(set[ii] + ",");
        }
        System.out.print("} ");
    }

    public static char[] addVertex(char vertex, char[] set)
    {
        // can we fit one more??
        if (count(set) + 1 <= set.length)
        {
            set[count(set)] = vertex;
        }

        return set;
    }

    public static char[] removeVertex(char vertex, char[] set)
    {
        boolean found = false;
        if (count(set) >= 1)
        {
            int initalCount = count(set);
            for( int ii = 0; ii < initalCount - 1; ii++)
            {
                if (set[ii] == vertex)
                {
                    found = true;
                }
                
                if (found == true)
                {
                    set[ii] = set[ii+1];
                }
            }

            set[initalCount - 1] = '\u0000';
        }

        return set;
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
                if (edges[ii][jj] != '\n')
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
                                edges[getPos(edges[ii][jj])][kk] = '\n';
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
