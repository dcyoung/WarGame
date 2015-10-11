
public class Move {
	
	protected Player movingPlayer;
	protected String type;
	protected GridSpace gridSpace;
	protected GameStateNode gameStateNode;
	
	public Move(String moveType, String movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		this.type = moveType;
		this.gameStateNode = gameStateNode;
		this.movingPlayer = gameStateNode.getPlayerByID(movingPlayerID);
		this.gridSpace = gridSpace;
	}
	
	/**
	 * Both Commando-Para-Drops and M1-Death-Blitz are only allowed 
	 * if the space is unoccupied.
	 * @return true if the space the player is moving into is unoccupied
	 */
	public boolean isAllowable(GameStateNode n){
		return !gridSpace.isOccupied();
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
