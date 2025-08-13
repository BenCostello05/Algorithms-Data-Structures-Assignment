// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;

//Exception for queue
class QueueException extends Exception {
    public QueueException(String m)
    {
        super(m);
    }
}//End of Exception  

//Interface for Queue
interface Queue {
    public void enQueue(int x) throws QueueException;
    public int deQueue() throws QueueException;
    public boolean isEmpty();   
}//End of Interface

//QueueCb Class - Circular Queue used in breadth first search
class QueueCB implements Queue {
    private int q[], back, front;
    private int qmax, size;

    //Constructor
    public QueueCB() {
        qmax = 10;
        size = front = back = 0;
        q = new int[qmax];
    }//End of Constructor

    //enQueue Method - inserts the integer at the back of the queue
    public void enQueue( int x) throws QueueException  {
        
        if(size != qmax) //Checks if the queue is full
        {
            q[back] = x; //Inserts integer at the back of the queue
            back = (back+1) % qmax; //Divide by the maximum amount of space in the queue so that it goes back to  the start of the queue (0) when it is at the end
            ++size;
        }
        else //Throws an exception if the queue is full
        {
            throw new QueueException("The queue is full");
        }
    }//End of Enqueue Method
    
    //deQueue Method - removes the integer that is at the front of the queue
    public int deQueue()  throws QueueException 
    {
        int deleteValue = 0;
        if(size != 0) //Checks that the queue is not empty
        {
            deleteValue = q[front]; 
            front = (front+1) % qmax; //Divide by the maximum amount of space in the queue so that it goes back to  the start of the queue (0) when it is at the end
            --size;
            return deleteValue; 

        }
        else //If the queue is empty it throws an exception
        {
            throw new QueueException("The queue is empty");
        }
    }//End of deQueue Method

    //isEmpty Method - checks if the queue is empty and if so returns true
    public boolean isEmpty() {
        return size == 0;
    }//End of isEmpty Method

        
}

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
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

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
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

    //Depth first search Method using recursion - takes in an integer s which specifies which vertex to start at
    //Called by the main method
    //0 = white
    //1= black
    //2 = grey
    public void DF( int s) 
    {
        int v;
        //Loops through the visited and parent array and sets them all to 0
        for(v=1; v<=V; ++v) {
            visited[v] = 0;   
            parent[v] = 0; 
        }
        
        System.out.println("-------------------------------------");
        System.out.print("Depth First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));
        
        id = 0;
        //Calls dfVisit method and passes in s  
        dfVisit(s);         
        
        System.out.print("\n\n");
    
    } //End of DF Method

    //Depth first search Method using recursion - takes in an integer v which specifies which vertex to start at
    //Called by the DF Method
    private void dfVisit(int v)
    {
        Node t;
        int u;
        ++id;
        d[v] = id;
        visited[v] = 2; //Sets vertex v to 2 to signify it has been discovered
        System.out.println("\nv = " + v);
        System.out.println("DF just visited vertex " + toChar(v) + " along edge " + toChar(parent[v]) + "--" + toChar(v));

        //Loops through all edges from vertex v
        for(t=adj[v]; t!=z; t=t.next)
        {
            u = t.vert; //Sets u to the connected vertex
            if(visited[u] == 0) //If vertex u has not been visited
            {
                parent[u] = v; //Sets the parent of vertex u
                dfVisit(u); //Calls itself and passes in u
            }
        }
        visited[v] = 1; //Sets vertex v to 1 to signify it has been visited/finished
        ++id;
        f[v] = id;

    } //End of dfVisit method

    //Breadth first search method - takes in an integer s which signifies the vertex to start at 
    //Called in main method
    // 0 - white
    // 1 - black
    // 2 - grey
    public void breadthFirst(int s) {

        System.out.println("-------------------------------------");
        System.out.print("Breadth First Graph Traversal\n");
        System.out.println("Starting with Vertex " + toChar(s));
        int v,u;
        //Loops through each vertex
        for(v=1; v<=V; ++v) {
            if(v == s) continue;
            visited[v] = 0;  
            d[v] = Integer.MAX_VALUE;
            parent[v] = 0;
        }
        visited[s] = 2; //Sets vertex s to 2 to signify it has been discovered
        d[s] = 0;
        parent[s] = 0;
        QueueCB Q = new QueueCB(); //Creates a new QueueCB Object
        try {
            Q.enQueue(s); //Calls the enQueue Method and passes in s 
        } catch (QueueException e) {
            System.out.println("Exception thrown:  " + e.getMessage()); 
        }
        while(!Q.isEmpty()) //Keeps looping until the queue is empty
        {
            try { 
                v = Q.deQueue(); //Calls the deQueue Method and sets v to the integer that gets returned 
            } catch (QueueException e) {
                System.out.println("Exception thrown: " + e.getMessage()); 
            }
            System.out.println("\nv = " + v);
            System.out.println("BF just visited vertex " + toChar(v) + " along edge " + toChar(parent[v])  + "--" + toChar(v) );
            //Loops through all edges from vertex v
            for(Node n = adj[v]; n != z; n = n.next) 
            {
                u = n.vert; //Sets u to the connected vertex
                if(visited[u] == 0) //If the vertex u has not been visited
                {
                    visited[u] = 2; //Sets vertex u to 2 to signify it has been discovered
                    d[u] = d[v] + 1;
                    parent[u] = v; //Sets the parent of vertex u
                    try {
                        Q.enQueue(u); //Calls the enQueue Method and passes in u 
                    } catch (QueueException e) {
                        System.out.println("Exception thrown:  " + e.getMessage()); 
                    }
                }
            }
                
        }
        visited[v] = 1; //Sets vertex v to 1 to signify it has been visited/finished


    } //End of breadthFirst method

    //Prim's Minimum Spanning Tree (MST) Algorithm - takes in an integer s which specifies the vertex to start at
	public void MST_Prim(int s)
	{
        
        System.out.println("\n-------------------------------------");
        System.out.print("Prim's Minimum Spanning Tree Algorithm\n");
        System.out.println("Starting with Vertex " + toChar(s));
        int v, u;
        int wgt =0 ,wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;
        

        //code here
        dist = new int[V+1]; //Used to keep track of the distance of each edge 
        parent = new int[V+1]; //Used to keep track of the parent of each vertex
        hPos = new int[V+1]; //Used to keep track of the position of each vertex in the heap
        
        //Loops through all the vertices and resets the arrays
        for(v = 1; v <= V; ++v)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0; //sets the distance of the vertex s to 0

        Heap h =  new Heap(V, dist, hPos); //Creates a new heap object and passes in the amount fo vertices V and the arrays dist and hPos
        h.insert(s); //Calls the heap insert method and passes in s 
        
        while (!h.isEmpty())  //Keeps looping while the heap is not empty
        {
            v = h.remove(); //Calls the heap remove method sets v to the integer that is removed/returned 
            dist[v] = -dist[v]; //sets the distance of the vertex v to negative to signify it is now in the MST
            System.out.println("\nv = " + v);
            System.out.println("MST_Prim just inserted " + toChar(v) + " into MST along edge " + toChar(parent[v])  + "--" + toChar(v));
            //Loops through all the edges from v
            for(t = adj[v]; t != z; t = t.next)
            {
                u = t.vert;
                wgt = t.wgt;
                if(wgt < dist[u]) //If the weight of the edge is less then the distance already saved for the vertex u
                {
                    dist[u] = wgt;
                    parent[u] = v;
                    if(hPos[u] == 0) //If the vertex u is not in the heap
                    {
                        h.insert(u); //Calls the heap insert method and passes in u
                    }
                    else
                    {
                        h.siftUp(hPos[u]); //Calls the heap siftUp Method and passes in the position of u in the heap
                    }
                }

            }
        }

        //Loops through the distance array and multiplies it by -1 and adds it to the total weight sum
        for(int x = 1; x < V+1; ++x)
        {
            wgt_sum += dist[x] * -1;
        }

        mst = parent; //Sets the mst array to the parent array
        

        System.out.print("\n\nWeight of MST = " + wgt_sum + "\n");

        
                  		
	}//End of MST_Prim method
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
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
            System.out.println("   " + toChar(v) + " \t   " + toChar(parent[v]) + " \t\t " + dist[v]);

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

public class GraphLists {
    public static void main(String[] args) throws IOException
    {
        /*
        int s = 12;
        String fname = "wGraph1.txt";
        */

        
        //User input
        Scanner scanner = new Scanner(System.in);

        //Gets the user to input the text file and initializes the variable fname with it
        System.out.println("Please enter the text file that contains a sample graph you want to use:"); 
        String fname = scanner.nextLine();

        //Gets the user to input the vertex they want to start at and initializes the variable s with it
        System.out.println("Please enter the vertex you want to start at:");
        int s = scanner.nextInt();
        scanner.close();
           
        
        Graph g = new Graph(fname);
       
        g.display();

        g.DF(s);
        g.breadthFirst(s);
        g.MST_Prim(s);
        g.showMST();      
        g.SPT_Dijkstra(s);    
    }
}
