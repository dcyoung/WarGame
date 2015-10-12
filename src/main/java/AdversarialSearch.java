/**
 * AdversarialSearch:
 * 		Conducts a minimax or enhanced minimax (alpha + beta pruning)
 * 		on a game state.
 * 		
 * 		Big idea is to evaluate the utility of the leaf nodes such that
 * 		each node in the tree can be assigned a worth value to the maximizing
 * 		agent (AI player). 
 * 	Minimax: 
 * 		Takes a root node and moves down the tree until it reaches leaf
 * 		nodes or the maximum permitted depth (for performance reasons).
 * 		When it reaches such a stopping condition, it returns the heuristic
 * 		value associated with that node and then considers all these leaf 
 * 		values as the recursion closes back down such that only the choices
 * 		maximizing utility in the end (or at least by the max searchable depth)
 * 		will be selected. 
 * 
 * @author dcyoung3
 */
import java.util.ArrayList;

public class AdversarialSearch {
	private GameStateNode root;
	private int depthLimit;
	
	/**
	 * Constructor
	 * @param root
	 * @param depthLimit
	 */
	public AdversarialSearch(GameStateNode root, int depthLimit){
		this.root = root;
		this.depthLimit = depthLimit;
	}
	
	/**
	 * kicks off the search
	 * @return
	 */
	public int conductSearch(){
		return minimax(this.root, this.depthLimit, true);
	}
	
	/**
	 * 
	 * @param state
	 * @param depthLimit
	 * @param bIsMaximizingPlayer
	 * @return 
	 */
	public int minimax(GameStateNode state, int depthLimit, boolean bIsMaximizingPlayer){
		if( state.isLeafNode() || depthLimit == 0 ){
			return evaluate(state);
		}
		else if(bIsMaximizingPlayer){
			//n is a max node
			int bestValue = Integer.MIN_VALUE;
			int value;
			for(GameStateNode child : state.getNodeChildren(state.getMaximizingPlayer())){
				value = minimax(child, depthLimit-1, false);
				bestValue = Math.max(bestValue, value);
			}
			return bestValue;
		}
		else{ 
			//n is a min node
			int bestValue = Integer.MAX_VALUE;
			int value;
			for(GameStateNode child : state.getNodeChildren(state.getMaximizingPlayer())){
				value = minimax(child, depthLimit-1, true);
				bestValue = Math.min(bestValue, value);
			}
			return bestValue;
		}
	}
	
	/**
	 * 
	 * @param n
	 * @return heuristic value of the node
	 */
	public int evaluate(GameStateNode state){
		int heuristicVal = 0;
		int scoreDifference = state.getPlayer1().getCurrentScore()-state.getPlayer2().getCurrentScore();;
		if(state.getPlayer1().isMaximizingPlayer()){
			heuristicVal += scoreDifference;
		}
		else{
			heuristicVal += -scoreDifference;
		}
		
		if(state.isLeafNode()){
			if(state.getLeadingPlayer().isMaximizingPlayer()){
				heuristicVal += 36*99;
			}
			else{
				heuristicVal += -36*99;
			}
		}
		return heuristicVal;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
