/**
 * AdversarialSearch:
 * 		Conducts a minimax or enhanced minimax (alpha + beta pruning)
 * 		on a game state. In practice, this class will be used to 
 * 		evaluate the outcome of different moves for a player, to help 
 * 		them decide which to select.
 * 
 * 	Minimax: 
 *		The minimax algorithm is a way of finding an optimal move in a 
 *		two player game. The big idea is to evaluate the utility of the 
 *		leaf nodes such that each node in the tree can be assigned a 
 *		worth value to the maximizing agent (AI player). 
 *
 *	Alpha Beta Pruning:
 *		Alpha-beta pruning is a way of finding the optimal minimax solution while 
 *		avoiding searching subtrees of moves which won't be selected. It accomplishes 
 *		the same task as normal minimax, but reduces the size of the search space 
 *		by pruning the tree. 
 * 
 * @author dcyoung3
 */
import java.util.ArrayList;

public class AdversarialSearch {
	private GameStateNode root;
	private int miniMaxDepthLimit;
	private int alphaBetaDepthLimit;
	private boolean bUseAlphaBeta;
	private int numExpandedNodes;
	
	/**
	 * Constructor
	 * @param root
	 * @param miniMaxDepthLimit
	 * @param useAlphaBetaPruning
	 */
	public AdversarialSearch(GameStateNode root, int miniMaxDepthLimit, int alphaBetaDepthLimit, boolean useAlphaBetaPruning){
		this.root = root;
		this.bUseAlphaBeta = useAlphaBetaPruning;
		this.miniMaxDepthLimit = miniMaxDepthLimit;
		this.alphaBetaDepthLimit = alphaBetaDepthLimit;
		this.numExpandedNodes = 0;
	}
	
	/**
	 * kicks off the search
	 * @return
	 */
	public int conductSearch(){
		this.numExpandedNodes = 0;
		int result; 
		if(this.bUseAlphaBeta){
			result = alphaBeta(this.root, this.alphaBetaDepthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		}else{
			result = minimax(this.root, this.miniMaxDepthLimit, true); 
		}
		return result;
	}
	
	/**
	 * minimax:
	 * 		The minimax algorithm is a way of finding an optimal move in a 
	 *		two player game. 
	 *
	 *		Each layer of the tree alternates as a MAX or MIN node.
	 *		The goal at a MAX node is to maximize the value of the subtree rooted 
	 *		at that node. To do this, a MAX node chooses the child with the greatest 
	 *		value, and that becomes the value of the MAX node. Similarly, a MIN
	 *		node tries to minimize the value of the subtree rooted at that node,
	 *		and will choose the child with the lowest value, and that becomes
	 *		the value of the MIN node.
	 *
	 * 		The  minimax algorithm takes a root node and moves down the tree 
	 * 		until it reaches leaf nodes (or the maximum permitted depth, 
	 * 		for performance reasons). When it reaches such a stopping condition, 
	 * 		it returns a heuristic value that measures or estimates the utility 
	 * 		of the root node to the max player.
	 * 		 
	 * @param root
	 * @param depthLimit
	 * @param bIsMaxNode
	 * @return a measure or estimate of the utility of the root node to the max player, 
	 * 			assuming the opponent plays optimally
	 */
	public int minimax(GameStateNode root, int depthLimit, boolean bIsMaxNode){
		//if the node is a leaf node report its utility,
		if( root.isLeafNode() || depthLimit == 0 ){
			//treat deep enough nodes as leaf nodes (this will be a utility estimate though) 
			return evaluate(root, bIsMaxNode);
		}
		else{
			ArrayList<Move> allowAbleMoves;
			Move move;
			GameStateNode child;
			int childMiniMaxValue;
			
			if(bIsMaxNode){
				//n is a max node
				int miniMaxValue = Integer.MIN_VALUE;
				
				//get the allowable moves for the current state
				allowAbleMoves = root.getAllowableMoves(root.getMaximizingPlayer());
				//consider every child state resulting from one of the allowable moves
				for(int moveIndex = 0; moveIndex < allowAbleMoves.size(); moveIndex++){
					move = allowAbleMoves.get(moveIndex);
					child = root.getChildStateAfterMove(root.getMaximizingPlayer(), move);
					//evaluate the child
					this.numExpandedNodes++;
					childMiniMaxValue = minimax(child, depthLimit-1, false);
					//n is a max node, its minimax value will be the max of all its children
					miniMaxValue = Math.max(miniMaxValue, childMiniMaxValue);
				}
				return miniMaxValue;
			}
			else{ 
				//n is a min node
				int miniMaxValue = Integer.MAX_VALUE;
				//get the allowable moves for the current state
				allowAbleMoves = root.getAllowableMoves(root.getMinimizingPlayer());
				//consider every child state resulting from one of the allowable moves
				for(int moveIndex = 0; moveIndex < allowAbleMoves.size(); moveIndex++){
					move = allowAbleMoves.get(moveIndex);
					child = root.getChildStateAfterMove(root.getMinimizingPlayer(), move);
					//evaluate the child
					this.numExpandedNodes++;
					childMiniMaxValue = minimax(child, depthLimit-1, true);
					//n is a min node, its minimax value will be the min of all its children
					miniMaxValue = Math.min(miniMaxValue, childMiniMaxValue);
				}
				return miniMaxValue;
			}
		}
	}
	
	/**
	 *	alphaBetaMinimax:
	 *		Alpha-beta pruning is a way of finding the optimal minimax solution while 
	 *		avoiding searching subtrees of moves which won't be selected. It accomplishes 
	 *		the same task as normal minimax, but reduces the size of the search space 
	 *		by pruning the tree. 
	 *
	 * 		Alpha-beta pruning uses two bounds that are passed around in the algorithm.
	 *		The bounds restrict the set of possible solutions based on the portion of 
	 *		the search tree that has already been seen.
	 *			alpha 	=  	maximum lower bound of possible solutions
	 *			beta 	= 	minimum upper bound of possible solutions
	 *		Therefore, for any node state to be considered as part of the path to a soln,
	 *		the current estimate value for that node must fall inside the range bounded
	 *		by alpha and beta. ie: alpha <= estimate value <= beta
	 *
	 *		As the algorithm runs, restrictions on the range of possible solutions are updated
	 *		based on min nodes (which may place an upper bound) and max nodes (which may place 
	 *		a lower bound). Moving through the tree, these bounds typically get closer together
	 *		and eventually cross, such that beta < alpha. If such a crossing occurs, the range
	 *		for the node's value becomes nonexistent because there is no overlapping region 
	 *		between alpha and beta. In this circumstance, the node could never belong in a 
	 *		solution path, so the algorithm stops processing the node meaning it stops
	 *		generating its children and moves back to the parent node. The algorithm must
	 *		still note the value of this node however... so it passes (to the parent) 
	 *		the value that was changed which caused the crossing of alpha and beta.
	 *
	 * @param root
	 * @param depthLimit
	 * @param alpha 
	 * @param beta 
	 * @param bIsMaxNode
	 * @return a measure or estimate of the utility of the root node to the max player, 
	 * 			assuming the opponent plays optimally
	 */
	public int alphaBeta(GameStateNode root, int depthLimit, int alpha, int beta, boolean bIsMaxNode){
		if( root.isLeafNode() || depthLimit == 0 ){
			return evaluate(root, bIsMaxNode);
		}
		else{
			
			//get all the allowable moves for this state
			ArrayList<Move> allowableMoves;
			GameStateNode childStateNode;
			
			if(bIsMaxNode){
				//get the allowable moves for the current state
				allowableMoves = root.getAllowableMoves(root.getMaximizingPlayer());
				//n is a max node
				int miniMaxValue = alpha;
				int childValue;
				
				//consider every child state resulting from an allowable move
				for(int moveIndex = 0; moveIndex < allowableMoves.size(); moveIndex++){
					childStateNode = root.getChildStateAfterMove(root.getMaximizingPlayer(), allowableMoves.get(moveIndex));
					//evaluate the child state
					this.numExpandedNodes++;
					childValue = alphaBeta(childStateNode, depthLimit-1, miniMaxValue, beta, false);
					//n is a max node, its minimax value will be the max of its children
					miniMaxValue = Math.max(miniMaxValue, childValue);
					
					//update alpha and check if alpha and beta crossed
					alpha = Math.max(alpha, miniMaxValue);
					if(beta < alpha){
						//break;
						return alpha;
					}
				}
				return miniMaxValue;
			}
			else{ 
				allowableMoves = root.getAllowableMoves(root.getMinimizingPlayer());
				//n is a min node
				int miniMaxValue = beta;
				int childValue;
				
				//consider every child state resulting from an allowable move
				for(int moveIndex = 0; moveIndex < allowableMoves.size(); moveIndex++){
					childStateNode = root.getChildStateAfterMove(root.getMinimizingPlayer(), allowableMoves.get(moveIndex));
					//evaluate the child state
					this.numExpandedNodes++;
					childValue = alphaBeta(childStateNode, depthLimit-1, alpha, miniMaxValue, true);
					miniMaxValue = Math.min(miniMaxValue, childValue);
					beta = Math.min(beta,  miniMaxValue);
					if(beta < alpha){
						return beta;
						//break;
					}
				}
				return miniMaxValue;
			}
		}
	}
	
	/**
	 * need to scale this somehow by the possibility of being blitzed... 
	 * should add a check for blitzable neighbors and tally the potential score
	 * to be stolen, weighting the score difference by the proportion that could
	 * be stolen maybe???
	 * @param state
	 * @return
	 */
	public int evaluate(GameStateNode state, boolean bIsMaxNode){
		int maxPlyrScore = state.getMaximizingPlayer().getCurrentScore();
		int minPlyrScore = state.getMinimizingPlayer().getCurrentScore();
		
		int heuristic;
		int scoreDifference = maxPlyrScore - minPlyrScore;
		
		
		//default heuristic
		heuristic = scoreDifference;
		return heuristic;
		
		
		//new addition #1: Secured Portion Weighting
		/*int portionSecuredForActivePlayer;
		
		if (bIsMaxNode) {
			//it is the max player's turn... best case is positive score difference and large portion secured
			if( maxPlyrScore > 0 ){
				int maxPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMaximizingPlayer().getPlayerID());
				portionSecuredForActivePlayer = 1 - ( maxPlyrVulnPoints / maxPlyrScore );
			}
			else{
				portionSecuredForActivePlayer = 1;
			}
			if(scoreDifference > 0){
				heuristic = scoreDifference * portionSecuredForActivePlayer;
			}
			else{
				heuristic = scoreDifference * (1-portionSecuredForActivePlayer);
			}
		} else {
			//it is the min player's turn... best case is negative score difference and large portion secured
			if( minPlyrScore > 0 ){
				int minPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMinimizingPlayer().getPlayerID());
				portionSecuredForActivePlayer = 1 - ( minPlyrVulnPoints / minPlyrScore );
			}
			else{
				portionSecuredForActivePlayer = 1;
			}
			if( scoreDifference > 0 ){
				heuristic = scoreDifference * (1 - portionSecuredForActivePlayer);
			}
			else{
				heuristic = scoreDifference * portionSecuredForActivePlayer;
			}
		}
		return heuristic;
		*/
		
		
		//new addition #2:  score difference after subtracting the vulnerable points from active player's score
/*		int securedScore;
		if (bIsMaxNode) {
			//it is the max player's turn... best case is positive score difference and large portion secured
			int maxPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMaximizingPlayer().getPlayerID());
			securedScore = maxPlyrScore - maxPlyrVulnPoints;
			heuristic = securedScore - minPlyrScore;
		} 
		else {
			//it is the min player's turn... best case is negative score difference and large portion secured
			int minPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMinimizingPlayer().getPlayerID());
			securedScore = minPlyrScore - minPlyrVulnPoints;
			heuristic = maxPlyrScore - securedScore;
		}
		return heuristic;
		*/
		
		
		//new addition #3:  tiny weight given to vulnerable points
	/*	int securedScore;
		float weight = 0.1f;
		if (bIsMaxNode) {
			//it is the max player's turn... best case is positive score difference and large portion secured
			int maxPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMaximizingPlayer().getPlayerID());
			heuristic = maxPlyrScore - minPlyrScore - (int)(maxPlyrVulnPoints*weight);
		} 
		else {
			//it is the min player's turn... best case is negative score difference and large portion secured
			int minPlyrVulnPoints = state.getBoardState().getVulnerablePoints(state.getMinimizingPlayer().getPlayerID());
			heuristic = maxPlyrScore - minPlyrScore + (int)(minPlyrVulnPoints*weight);
		}
		return heuristic;
		*/
		
	}
	
	public void setMiniMaxDepthLimit(int depthLimit){
		this.miniMaxDepthLimit = depthLimit;
	}
	public void setAlphaBetaDepthLimit(int depthLimit){
		this.alphaBetaDepthLimit = depthLimit;
	}

	public int getNumExpandedNodes() {
		return numExpandedNodes;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}




}
