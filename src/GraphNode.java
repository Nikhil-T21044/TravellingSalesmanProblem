/*
 * @author Nikhil
 * */
public class GraphNode {
	boolean[] v;
	int currPos; 
	int count;
	int cost;
	int ans;
	
	GraphNode(boolean[] v, int currPos, int count, int cost, int ans){
		this.v = v.clone();
		this.currPos = currPos;
		this.count = count;
		this.cost = cost;
		this.ans = ans;
	}
	
	void processPath(){
		ans = App.tsp(App.graph, v, currPos, App.n, count, cost, ans);
	}
}
