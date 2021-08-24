/*
 * @author Nikhil
 * */
public class App
{
	static int[][] graph;
	static boolean[] v;
	static int n;
	static boolean makeParallel = false;
	// Function to find the minimum weight
	// Hamiltonian Cycle
	static int tsp(int[][] graph, boolean[] v,
				int currPos, int n,
				int count, int cost, int ans)
	{

		// If last node is reached and it has a link
		// to the starting node i.e the source then
		// keep the minimum value out of the total cost
		// of traversal and "ans"
		// Finally return to check for more possible values
		if (count == n && graph[currPos][0] > 0)
		{
			ans = Math.min(ans, cost + graph[currPos][0]);
			return ans;
		}

		// BACKTRACKING STEP
		// Loop to traverse the adjacency list
		// of currPos node and increasing the count
		// by 1 and cost by graph[currPos,i] value
		
		int noOfThread = 7;
		backtrackingThread[] t = new backtrackingThread[noOfThread];
		for (int i = 0; i < n; i++)
		{
			if (v[i] == false && graph[currPos][i] > 0)
			{

				// Mark as visited
				v[i] = true;
				if(count == 1 && makeParallel) {
					int threadNo = i%noOfThread;
					 if (t[threadNo] == null)
						 t[threadNo] = new backtrackingThread( new GraphNode (v, i, count + 1, cost + graph[currPos][i], ans) );
					 else
						 t[threadNo].addNode(new GraphNode (v, i, count + 1, cost + graph[currPos][i], ans) );
				}else {
				ans = tsp(graph, v, i, n, count + 1,
						cost + graph[currPos][i], ans);
				}

				// Mark ith node as unvisited
				v[i] = false;
			}
		}
		if(count == 1 && makeParallel) {
			
			try {
				int threads = t.length;
			while(threads-- > 0) {
				if(t[threads] != null) { t[threads].join();
				ans = Math.min(t[threads].ans, ans);}
			}
	 		} catch (InterruptedException e) {
	 			 System.out.println("Exception occured at runnning thread");
	 			e.printStackTrace();
	 		}
		}
		return ans;
	}

	// Driver code
	public static void main(String[] args)
	{

		// n is the number of nodes i.e. V
		n = 12;

//		graph = new int[][] {{0, 10, 15, 20},
//						{10, 0, 35, 25},
//						{15, 35, 0, 30},
//						{20, 25, 30, 0}};
		
		graph = generateMatrix(n);

		// Boolean array to check if a node
		// has been visited or not
		v = new boolean[n];

		// Mark 0th node as visited
		v[0] = true;
		int ans = Integer.MAX_VALUE;

		// Find the minimum weight Hamiltonian Cycle
		long start = System.currentTimeMillis();
		ans = tsp(graph, v, 0, n, 1, 0, ans);
		long end = System.currentTimeMillis();
		System.out.format("\nexeccution time :\t"+ (end - start) + " ms \t\t");
		System.out.println( "Without threading "+ ans + "\n");
		
		ans = Integer.MAX_VALUE;
		start = System.currentTimeMillis();
		makeParallel = true;
		ans = tsp(graph, v, 0, n, 1, 0, ans);
		end = System.currentTimeMillis();
		System.out.format("\nexeccution time :\t"+ (end - start) + " ms \t\t");
		System.out.println( "With threading "+ ans + "\n");
		//printing the graph
		printMatrix(graph);

		// ans is the minimum weight Hamiltonian Cycle
//		System.out.println(ans);
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
	
    static void printMatrix(int[][] matrix) {
    	for (int i=0; i<matrix.length; i++) {
            for (int j=0; j<matrix[i].length; j++) {

                System.out.printf("%-4d", matrix [i][j]);
            }           
            System.out.println();
        }
    }
}
