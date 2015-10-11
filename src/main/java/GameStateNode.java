import java.util.ArrayList;

public class GameStateNode {
	
	private GameStateNode parent;
	private boolean bIsMaxNode;
	
	private Player player1;
	private Player player2;
	private BoardState boardState;
	
	public GameStateNode(Player p1, Player p2, BoardState bs){
		this.player1 = p1;
		this.player2 = p2;
		this.boardState = bs;
	}
	
	public GameStateNode deepCopyGameStateNode(){
		return new GameStateNode(this.player1.deepCopyPlayer(), this.player2.deepCopyPlayer(), this.boardState.deepCopyBoardState());
	}
	
	
	public String getLeadingPlayer(){
		if(this.player1.getCurrentScore() >= this.player2.getCurrentScore())
			return player1.getPlayerID();
		else
			return player2.getPlayerID();
	}
	
	public ArrayList<GameStateNode> getAllowableMoves(){
		return null;
	}
	
	
	/**
	 * In WarGame, the game ends when all the squares are occupied by all 
	 * players since no more moves are left.
	 * @return true is the game is over
	 */  
	public boolean isLeafNode(){
		return false;
	}
	
	public GameStateNode getParent() {
		return parent;
	}


	public void setParent(GameStateNode parent) {
		this.parent = parent;
	}


	public boolean isMaxNode() {
		return bIsMaxNode;
	}


	public void setIsMaxNode(boolean bIsMaxNode) {
		this.bIsMaxNode = bIsMaxNode;
	}


	public BoardState getBoardState() {
		return boardState;
	}


	public void setBoardState(BoardState boardState) {
		this.boardState = boardState;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
