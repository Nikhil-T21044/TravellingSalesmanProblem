import java.io.File;
import java.io.IOException;

/*
 * @author Nikhil
 * */

public class App
{
	static int[][] graph;
//	static boolean[] v;
//	static int n;
//	static boolean branchNBound = false;

	static int tspParallel(int[][] graph, boolean[] v, int currPos, int count, int cost, int ans, boolean branchNBound, boolean mstInclude) {
		int noOfThreads = 8;//graph.length;
		backtrackingThread[] t = new backtrackingThread[noOfThreads];

		v[0] =  true;
		
		for (int i = 0; i < graph.length; i++)
		{
			if (v[i] == false && graph[currPos][i] > 0)
			{
				v[i] = true; // Mark as visited
				
				int threadNo = i%noOfThreads;
				if (t[threadNo] == null)
					t[threadNo] = new backtrackingThread(branchNBound, mstInclude, new GraphNode (v, i, count + 1, cost + graph[currPos][i], ans) );
				else
					t[threadNo].addNode(new GraphNode (v, i, count + 1, cost + graph[currPos][i], ans) );

				v[i] = false; // Mark ith node as unvisited
			}
		}
		
		return startThreadExecution(ans, t);
	}
	
	private static int startThreadExecution( int ans, backtrackingThread[] t) {
		try {
			int threads = t.length;
			while(threads-- > 0) // starting threads
				if(t[threads] != null)  t[threads].start();
				
			threads = t.length;
			while(threads-- > 0) { // waiting for threads
				if(t[threads] != null) { 
					t[threads].join();
					ans = Math.min(t[threads].ans, ans);
				}
			}
	 	} catch (InterruptedException e) {
	 			 System.out.println("Exception occured at runnning thread");
	 			e.printStackTrace();
	 	}
		return ans;
	}

	// Driver code
	public static void main(String[] args)
	{

		// n is the number of nodes i.e. V
		boolean[] v;
		int n;

		graph = generateMatrix();  //reading input from file
		n = graph.length;
		
		System.out.println("Execution time \tTOTAL_PATH_COST\t  Graph Size (" + n +"*"+ n + ") \t\t \n");

		// Boolean array to check if a node
		// has been visited or not
		v = new boolean[n];	
		long start , end;
		int ans;
		
		// MST WITH BRANCH & BOUND USING THREADS INCLUDING MST FOR PRUNING
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		TSP.minSoFar.set(Integer.MAX_VALUE);
		start = System.currentTimeMillis();
		ans = tspParallel(graph, v , 0 , 1, 0 , ans, true, true);
		end = System.currentTimeMillis();
		System.out.format( (end - start) + " ms \t");
		System.out.println( "\t| " + ans +  " | \t\tWith threading  with Branch N Bound including MST \n");
		
		// MST WITH BRANCH & BOUND WITHOUT ANY THREADS INCLUDING MST FOR PRUNING
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		TSP.minSoFar.set(Integer.MAX_VALUE);
		start = System.currentTimeMillis();
		ans = new TSP().tsp(graph, v, 0, n, 1, 0, ans, true);
		end = System.currentTimeMillis();
		System.out.format((end - start) + " ms \t");
		System.out.println(  "\t| " + ans + " | \t\tWithout threading  with Branch N Bound including MST \n");
		
		// MST WITH BRANCH & BOUND USING THREADS
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		TSP.minSoFar.set(Integer.MAX_VALUE);
		start = System.currentTimeMillis();
		ans = tspParallel(graph, v , 0 , 1, 0 , ans, true, false);
		end = System.currentTimeMillis();
		System.out.format( (end - start) + " ms \t");
		System.out.println( "\t| " + ans +  " | \t\tWith threading  with Branch N Bound \n");
		
		
		// MST WITH BRANCH & BOUND WITHOUT ANY THREADS
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		TSP.minSoFar.set(Integer.MAX_VALUE);
		start = System.currentTimeMillis();
		ans = new TSP().tsp(graph, v, 0, n, 1, 0, ans, false);
		end = System.currentTimeMillis();
		System.out.format((end - start) + " ms \t");
		System.out.println(  "\t| " + ans + " | \t\tWithout threading  with Branch N Bound \n");
		
		// SIMPLE CASE OF BACKTRACKING WITH THREADS
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		start = System.currentTimeMillis();
		ans = tspParallel(graph, v , 0 , 1, 0 , ans, false, false);
		end = System.currentTimeMillis();
		System.out.format( (end - start) + " ms \t");
		System.out.println( "\t| " + ans +  " | \t\tWith threading without Branch N Bound \n");

		// SIMPLE CASE OF BACKTRACKING WITHOUT ANY THREADS
		v = new boolean[n];
		v[0] = true;
		ans = Integer.MAX_VALUE;
		start = System.currentTimeMillis();
		ans = new TSP().tsp(graph, v, 0, n, 1, 0, ans);
		end = System.currentTimeMillis();
		System.out.format( (end - start) + " ms \t");
		System.out.println( "\t| "+ans +" | \t\tWithout threading "+ "\n");
		
		//printing the graph
//		printMatrix(graph);
	}
	
	static int[][] generateMatrix(int n){
    	int[][] matrix = new int [n] [n];
    	for (int i=0; i<n; i++) {
    	    for (int j=i; j<n; j++) {
    	    	if(i == j)	matrix[i][j] = 0;
    	    	else	matrix[i][j] = matrix[j][i] = (int) (Math.random()*10 + 1); //minimum edge weight to 1
    	    }           
    	}
    return matrix;	
    }
	
	static int[][] generateMatrix(){
    	try {
			return ( new Input().readFromFile(new File("./TSP/Input36.tsp")) );
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return new int [1][1];	 // if some exception occured
    }
	
    static void printMatrix(int[][] matrix) {
    	for (int i=0; i<matrix.length; i++) {
    		System.out.printf("{");
            for (int j=0; j<matrix[i].length; j++) {
            	if(j == 0) System.out.printf("%-4d", matrix [i][j]);
            	else System.out.printf(",%-4d", matrix [i][j]);
            }           
            System.out.printf("}\n");
        }
    }
}
