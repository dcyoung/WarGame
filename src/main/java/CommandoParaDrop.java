
public class CommandoParaDrop extends Move {
	
	
	public CommandoParaDrop(String movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		super("CommandoParaDrop", movingPlayerID, gridSpace, gameStateNode);
	}
	
	public void makeMove(){
		//modify the grid space
		this.gridSpace.setResidentPlayerID(this.movingPlayer.getPlayerID());
		this.gridSpace.setbOccupied(true);
		
		//adjust the player's score
		int updatedPlayerScore = this.movingPlayer.getCurrentScore() + this.gridSpace.getValue();
		this.movingPlayer.setCurrentScore(updatedPlayerScore);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
