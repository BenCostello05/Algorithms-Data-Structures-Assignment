import java.io.*;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }

    //siftUp Method - Reorders the heap and inserts the integer into the correct position in it
    public void siftUp( int k) 
    {
        int v = a[k];;
        // code yourself
        // must use hPos[] and dist[] arrays
        a[0] = 0;
        while (dist[v] < dist[a[k/2]]) //Keeps looping while the distance of the integer (v) is less then the distance of it's parent in the heap
        {
            //Swaps the integers positions in the heap and stores it
            a[k] = a[k/2]; // Inserts the parent integer into the lower position in the heap
            hPos[a[k/2]] = k; //Stores the position of the integer in the heap
            k = k/2;
        }
        //Inserts v into the correct position in the heap and stores it
        a[k] = v;
        hPos[v] = k;

    }

    //siftDown Method - reorders the array after removing the first integer in the heap
    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays

        j = 2*k; //Sets j to the left child of v
        while(j <=N) //Keeps looping while j is less than or equal to the size of the heap (N)
        {
            //j < N means there is a another node
            //It then checks if the distance of the right child is less than the distance of the less child and if it is adds 1 to j so we are looking at the right child
            if(j<N && dist[a[j+1]] < dist[a[j]])
            {
                ++j;
            }
            //Checks if the distance of the integer (v) is less than or equal to the distance of the node j is at and if so breaks the while loop
            if(dist[v] <= dist[a[j]])
            {
                break;
            }
            //Swaps the position of the integers in the heap and stores it
            a[k] = a[j];
            hPos[a[j]] = k;
            k = j;
            j = 2*k;
        }
        a[k] = v;
        hPos[v] = k;


        
    }//End of siftDown Method

    //insert method - inserts an integer into the heap and calls siftUp method for that new integer
    public void insert( int x) 
    {
        a[++N] = x;
        siftUp( N);

    }//End of insert method


    //remove Method - removes an integer from the heap and puts the last integer in the heap at the top and calls siftDown method
    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        a[1] = a[N--];
        siftDown(1);
        
        return v;

    } //End of remove method

     
}//End of Heap Class

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    //parent = Used to keep track of the parent of each vertex
    private int[] visited,parent,d,f;
    private int id;
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t,prev,curr;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        visited = new int[V+1];
        parent = new int[V+1];
        d = new int[V+1];
        f = new int[V+1];
        adj = new Node[V+1];

        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + u + "--(" + wgt + ")--" + v);   

            // write code to put edge into adjacency matrix    
            t = new Node();
            //Sets t to a new node with the first vertex in the edge
            t.vert = v;
            t.wgt = wgt;
            z.vert = v;
            //Sets curr to the node at position v in the adjacency list
            curr = adj[u];
            prev = null;

            //Inserts node into the correct position in the list
            while(curr.vert < t.vert) //Keeps looping to find the correct position for the node in the list
            {
                prev = curr;
                curr = curr.next;
            }
            t.next = curr;
            if(prev != null) //If not at the start of the list
            {
                prev.next = t;
            }
            else
            {
                adj[u] = t;
            }

            //Sets t to a new node containing the second vertex in the edge
            t = new Node();
            t.vert = u;
            z.vert = u;
            t.wgt = wgt;
            //Sets curr to the node at position v in the adjacency list
            curr = adj[v];
            prev = null;
            //Inserts node into the correct position in the list
            while(curr.vert < t.vert) //Keeps looping to find the correct position for the node in the list
            {
                prev = curr;
                curr = curr.next;
            }
            t.next = curr;
            if(prev != null) //If not at the start of the list
            {
                prev.next = t;
            }
            else
            {
                adj[v] = t;
            }
            
        }	       
    } //End of Constructor


    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + v + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + n.vert + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

    //Dijkstra's Shortest Path Tree (SPT) algorithm
    public void SPT_Dijkstra(int s)
    {
        System.out.println("\n-------------------------------------");
        System.out.print("Dijkstra's Shortest Path Tree Algorithm\n");
        System.out.println("Starting with Vertex " + toChar(s));
        int v,u;
        int wgt = 0;
        int[] dist,hPos,parent;
        Node t;

        dist = new int[V+1]; //Used to keep track of the distance of each edge 
        hPos = new int[V+1]; //Used to keep track of the position of each vertex in the heap
        parent = new int[V+1]; //Used to keep track of the parent of each vertex

        //Loops through all the vertices and resets the arrays
        for(v=1;v <= V; ++v)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        Heap pq = new Heap(V,dist,hPos); //Creates a new Heap object
        pq.insert(s); //Calls the heap insert method and passes in s
        dist[s] = 0; //sets the distance of the vertex s to 0

        System.out.print("\nShortest Path Tree is:\n");
        System.out.println("Vertex \t Parent    Distance from " + s);

        while(!pq.isEmpty()) //Keeps looping while the heap is not empty
        {
            v = pq.remove(); //Calls the heap remove method sets v to the integer that is removed/returned 
            System.out.println("   " + v + " \t   " + parent[v] + " \t\t " + dist[v]);

            //Loops through all the edges from v
            for(t = adj[v]; t != z; t = t.next)
            {
                u = t.vert;
                wgt = t.wgt;
                if(dist[v] + wgt < dist[u]) //If the weight of the edge plus the distance for vertex v we already have is less then the distance we already have for the vertex u
                {
                    dist[u] = dist[v] + wgt; //Sets distance of vertex u to the distance of vertex v we already have plus the weight
                    if(hPos[u] == 0) //If the vertex u is not in the heap
                    {
                        pq.insert(u);//Calls the heap insert method and passes in u
                    }
                    else
                    {
                        pq.siftUp(hPos[u]); //Calls the heap siftUp Method and passes in the position of u in the heap
                    }
                    parent[u] = v; //Sets the parent of vertex u to vertex v
                }

            }
        }


    }//End of SPT_Dijkstra method


}//End of Graph class

public class Dijkstra {
    public static void main(String[] args) throws IOException
    {
        
        int s = 12;
        String fname = "sw24978_graph.txt";
        

        /* 
        //User input
        Scanner scanner = new Scanner(System.in);

        //Gets the user to input the text file and initializes the variable fname with it
        System.out.println("Please enter the text file that contains a sample graph you want to use:"); 
        String fname = scanner.nextLine();

        //Gets the user to input the vertex they want to start at and initializes the variable s with it
        System.out.println("Please enter the vertex you want to start at:");
        int s = scanner.nextInt();
        scanner.close();
        */
           
        
        Graph g = new Graph(fname);
       
        //g.display();
        Runtime runtime = Runtime.getRuntime();
        System.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTIme = System.nanoTime();
        g.SPT_Dijkstra(s);
        long endTime = System.nanoTime();
        System.gc();
        long memoryafter = runtime.totalMemory() - runtime.freeMemory(); 

        long duration = (endTime - startTIme);
        System.out.println("The running of the SPT code used " + (memoryafter - memoryBefore) + " bytes of memory  and took " + duration + " nanoseconds");



    }
}