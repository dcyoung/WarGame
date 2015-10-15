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
	private int miniMaxDepthLimit;
	private int alphaBetaDepthLimit;
	private boolean bUseAlphaBeta;
	
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
	}
	
	/**
	 * kicks off the search
	 * @return
	 */
	public int conductSearch(){
		if(this.bUseAlphaBeta){
			return alphaBetaMinimax(this.root, this.alphaBetaDepthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
		}
		return minimax(this.root, this.miniMaxDepthLimit, true);
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
		else{
			ArrayList<Move> allowAbleMoves = state.getAllowableMoves(state.getMaximizingPlayer());
			GameStateNode child;
			Move move;
			int childMiniMaxValue;
			if(bIsMaximizingPlayer){
				//n is a max node
				int miniMaxValue = Integer.MIN_VALUE;
				for(int moveIndex = 0; moveIndex < allowAbleMoves.size(); moveIndex++){
					move = allowAbleMoves.get(moveIndex);
					child = state.getChildStateAfterMove(state.getMaximizingPlayer(), move);
					childMiniMaxValue = minimax(child, depthLimit-1, false);
					miniMaxValue = Math.max(miniMaxValue, childMiniMaxValue);
				}
				return miniMaxValue;
			}
			else{ 
				//n is a min node
				int miniMaxValue = Integer.MAX_VALUE;
				for(int moveIndex = 0; moveIndex < allowAbleMoves.size(); moveIndex++){
					move = allowAbleMoves.get(moveIndex);
					child = state.getChildStateAfterMove(state.getMaximizingPlayer(), move);
					childMiniMaxValue = minimax(child, depthLimit-1, true);
					miniMaxValue = Math.min(miniMaxValue, childMiniMaxValue);
				}
				return miniMaxValue;
			}
		}
	}
	
	/**
	 * FIXME: 
	 * @param state
	 * @param depthLimit
	 * @param alpha 
	 * @param beta 
	 * @param bIsMaximizingPlayer
	 * @return
	 */
	public int alphaBetaMinimax(GameStateNode state, int depthLimit, int alpha, int beta, boolean bIsMaximizingPlayer){
		if( state.isLeafNode() || depthLimit == 0 ){
			return evaluate(state);
		}
		else if(bIsMaximizingPlayer){
			//n is a max node
			int miniMaxValue = alpha;
			int childValue;
			
			ArrayList<Move> allowableMoves = state.getAllowableMoves(state.getMaximizingPlayer());
			GameStateNode childStateNode;
			
			for(int moveIndex = 0; moveIndex < allowableMoves.size(); moveIndex++){
				childStateNode = state.getChildStateAfterMove(state.getMaximizingPlayer(), allowableMoves.get(moveIndex));
				moveIndex++;
				childValue = alphaBetaMinimax(childStateNode, depthLimit-1, miniMaxValue, beta, false);
				miniMaxValue = Math.max(miniMaxValue, childValue);
				alpha = Math.max(alpha, miniMaxValue);
				if(beta >= alpha){
					break;
				}
			}
			return miniMaxValue;
		}
		else{ 
			//n is a min node
			int miniMaxValue = beta;
			int childValue;
			
			ArrayList<Move> allowableMoves = state.getAllowableMoves(state.getMaximizingPlayer());
			GameStateNode childStateNode;
			for(int moveIndex = 0; moveIndex < allowableMoves.size(); moveIndex++){
				childStateNode = state.getChildStateAfterMove(state.getMaximizingPlayer(), allowableMoves.get(moveIndex));
				moveIndex++;
				childValue = alphaBetaMinimax(childStateNode, depthLimit-1, alpha, miniMaxValue, true);
				miniMaxValue = Math.min(miniMaxValue, childValue);
				beta = Math.min(beta,  miniMaxValue);
				if(beta >= alpha){
					break;
				}
			}
			return miniMaxValue;
		}
	}
	
	/**
	 * 
	 * @param state
	 * @return
	 */
	public int evaluate(GameStateNode state){
		int scoreDifference = state.getMaximizingPlayer().getCurrentScore() - state.getMinimizingPlayer().getCurrentScore();
		return scoreDifference;
	}
	
	public void setMiniMaxDepthLimit(int depthLimit){
		this.miniMaxDepthLimit = depthLimit;
	}
	public void setAlphaBetaDepthLimit(int depthLimit){
		this.alphaBetaDepthLimit = depthLimit;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
