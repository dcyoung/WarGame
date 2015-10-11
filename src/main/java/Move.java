
public class Move {
	
	protected Player movingPlayer;
	protected String type;
	protected GridSpace gridSpace;
	protected GameStateNode gameStateNode;
	
	public Move(String moveType, Player movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		this.type = moveType;
		this.movingPlayer = movingPlayer;
		this.gridSpace = gridSpace;
		this.gameStateNode = gameStateNode;
	}
	
	/**
	 * Both Commando-Para-Drops and M1-Death-Blitz are only allowed 
	 * if the space is unoccupied.
	 * @return true if the space the player is moving into is unoccupied
	 */
	public boolean isAllowable(GameStateNode n){
		return !gridSpace.isbOccupied();
	}
	
	public Player getMovingPlayer() {
		return movingPlayer;
	}

	public String getType() {
		return type;
	}

	public GridSpace getGridSpace(){
		return this.gridSpace;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
