
public class Player {
	
	private String playerID;
	private boolean bShouldUseAlphaBetaPruning;
	private int currentScore;
	
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

	public Player deepCopyPlayer() {
		return new Player(this.playerID, this.bShouldUseAlphaBetaPruning, this.currentScore);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


}
