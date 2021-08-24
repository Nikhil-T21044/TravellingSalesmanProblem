/*
 * @author Nikhil
 * */
import java.util.LinkedList;

public class backtrackingThread extends Thread {
	LinkedList<GraphNode> nodeList = new LinkedList<GraphNode>();
	int ans = Integer.MAX_VALUE;
	
	public void run() {
//		System.out.println("thread runinng");		
		int size = nodeList.size();
		while(size-- > 0) {
			GraphNode node = nodeList.get(size);
			node.processPath(); // calculate distance
//			System.out.println(node.ans);
			ans = Math.min(node.ans, ans);
		}
    }
	
	public backtrackingThread(GraphNode node){
		nodeList.add(node);
		start();
	}


	void addNode(GraphNode node){
		nodeList.add(node);
	}
}
