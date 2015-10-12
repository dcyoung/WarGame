/**
 * Player:
 * 		Represents a player in a game of war. Holds associated info
 * 		regarding the player's current status and search strategies.
 * 
 * @author dcyoung
 *
 */
public class Player {
	
	private String playerID;
	private boolean bShouldUseAlphaBetaPruning;
	private boolean isMaximizingPlayer;
	private int currentScore;
	
	/**
	 * Constructor
	 * @param playerID
	 * @param bShouldUseAlphaBetaPruning
	 * @param currentScore
	 */
	public Player(String playerID, boolean bShouldUseAlphaBetaPruning, int currentScore){
		this.playerID = playerID;
		this.bShouldUseAlphaBetaPruning = bShouldUseAlphaBetaPruning;
		this.currentScore = currentScore;
	}
	
	public String getPlayerID() {
		return this.playerID;
	}
	
	public boolean getBShouldUseAlphaBetaPruning() {
		return this.bShouldUseAlphaBetaPruning;
	}
	
	public int getCurrentScore() {
		return this.currentScore;
	}
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}

	public boolean isMaximizingPlayer() {
		return isMaximizingPlayer;
	}

	public void setMaximizingPlayer(boolean isMaximizingPlayer) {
		this.isMaximizingPlayer = isMaximizingPlayer;
	}

	public Player deepCopyPlayer() {
		Player newPlayer = new Player(this.playerID, this.bShouldUseAlphaBetaPruning, this.currentScore);
		newPlayer.isMaximizingPlayer = this.isMaximizingPlayer;
		return newPlayer;
		
	}
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
