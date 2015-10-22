/**
 * GameStateNode:
 * 		Holds the state of the game at a given point.
 * 		Game states are the basic node used in the adversarial
 * 		search algorithms. 
 * 
 * 		The state of the game contains all information about
 * 		the current state, including player information and
 * 		gameboard information.
 *  
 * @author dcyoung3
 */
import java.util.ArrayList;

public class GameStateNode {
	
	private Player player1;
	private Player player2;
	private BoardState boardState;
	
	/**
	 * Constructor
	 * @param p1
	 * @param p2
	 * @param bs
	 */
	public GameStateNode(Player p1, Player p2, BoardState bs){
		this.player1 = p1;
		this.player2 = p2;
		this.boardState = bs;
	}
	
	
	/**
	 * 
	 * @param player
	 * @return the allowable moves for the specified player at this state
	 */
	public ArrayList<Move> getAllowableMoves(Player player){
		return this.boardState.getAllowableMoves(player.getPlayerID(), this);
	}
	
	/**
	 * Gets all the possible child states of this node based off all allowable moves. This
	 * is not very space efficient at all, but is useful in normal minimax to write shorthand
	 * when you know you won't be pruning any parts of the tree. But if you still care about
	 * not storing all the child states at once... then try using getChildStateAfterMove instead
	 * and simply iterate through the moves
	 * @param activePlayer
	 * @return array of subsequent states based of permittable moves from the current state
	 */
	public ArrayList<GameStateNode> getNodeChildren(Player activePlayer){
		ArrayList<GameStateNode> children = new ArrayList<GameStateNode>();
		for(Move m : this.getAllowableMoves(activePlayer) ){
			if(m instanceof CommandoParaDrop){
				CommandoParaDrop dropMove = (CommandoParaDrop) m;
				children.add(dropMove.makeMove());
			}
			else if(m instanceof M1DeathBlitz){
				M1DeathBlitz blitzMove = (M1DeathBlitz) m;
				children.add(blitzMove.makeMove());
			}
		}
		return children;
	}
	
	/**
	 * In WarGame, the game ends when all the squares are occupied 
	 * by players since no more moves are left.
	 * @return true is the game is over
	 */  
	public boolean isLeafNode(){
		return this.boardState.isGridFilled();
	}

	public BoardState getBoardState() {
		return boardState;
	}

	public Player getPlayer1() {
		return player1;
	}

	public Player getPlayer2() {
		return player2;
	}
	
	public Player getPlayerByID(String playerID){
		if(this.player1.getPlayerID().equals(playerID)){
			return this.player1;
		}
		else if(this.player2.getPlayerID().equals(playerID)){
			return this.player2;
		}
		return null;
	}

	public Player getLeadingPlayer(){
		if(this.player1.getCurrentScore() >= this.player2.getCurrentScore())
			return player1;
		else
			return player2;
	}

	public Player getMaximizingPlayer(){
		if(this.player1.isMaximizingPlayer()){
			return this.player1;
		}
		else if(this.player2.isMaximizingPlayer()){
			return this.player2;
		}
		return null;
	}
	
	public Player getMinimizingPlayer(){
		if(this.player1.isMaximizingPlayer()){
			return this.player2;
		}
		else if(this.player2.isMaximizingPlayer()){
			return this.player1;
		}
		return null;
	}
	
	/**
	 * Deep copies the game state node such that any modifications 
	 * will not alter the original. Essentially a clone.
	 * @return clone of the game state
	 */
	public GameStateNode deepCopyGameStateNode(){
		return new GameStateNode(this.player1.deepCopyPlayer(), this.player2.deepCopyPlayer(), this.boardState.deepCopyBoardState());
	}
	
	
	/**
	 * Good alternative to getNodeChildren (which returns all children), this will simply
	 * return 1 child based off a move. This is useful when searching a tree and 
	 * you prefer to iterate through the resulting states after a list of moves instead of
	 * grabbing all the children at once. This can save space by only holding one child at 
	 * a time, and save time by not retrieving children that wont be evaluated in A-B pruning 
	 * anyways.
	 * @param activePlayer
	 * @param move
	 * @return
	 */
	public GameStateNode getChildStateAfterMove(Player activePlayer, Move move){
		
		if(move instanceof CommandoParaDrop){
			CommandoParaDrop dropMove = (CommandoParaDrop) move;
			return dropMove.makeMove();
		}
		else if(move instanceof M1DeathBlitz){
			M1DeathBlitz blitzMove = (M1DeathBlitz) move;
			return blitzMove.makeMove();
		}
		return null;
	}
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



}
