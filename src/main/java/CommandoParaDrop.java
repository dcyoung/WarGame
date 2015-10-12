/**
 * CommandoParaDrop:
 * 		Specific sublcass of "Move" which defines a commando para 
 * 		drop move in the game of war.
 * 
 * Gameplay Overview:
 * 		You can take any open space on the board with a Para Drop. 
 * 		This will create a new piece on the board. This move can 
 * 		be made as many times as one wants to during the game, but 
 * 		only once per turn. A Commando Para Drop cannot conquer any 
 * 		pieces. It simply allows one to arbitrarily place a piece 
 * 		on any unoccupied square on the board. Once you have done a 
 * 		Para Drop, your turn is complete.
 * 
 * @author dcyoung
 *
 */
public class CommandoParaDrop extends Move {
	
	/**
	 * Constructor
	 * @param movingPlayerID
	 * @param gridSpace
	 * @param gameStateNode
	 */
	public CommandoParaDrop(String movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		super("CommandoParaDrop", movingPlayerID, gridSpace, gameStateNode);
	}
	
	/**
	 * Executes the move on the associated state and returns the resultant state.
	 * @return The resultant GameStateNode after the move is executed.
	 */
	public GameStateNode makeMove(){
		//create a clone of the state so that making the move won't mess with the original
		GameStateNode resultantState = this.gameStateNode.deepCopyGameStateNode();
		GridSpace moveGridSpace = resultantState.getBoardState().getGrid().get(this.gridSpace.getRow()).get(this.gridSpace.getCol());
		
		Player activePlayer = resultantState.getPlayerByID(this.movingPlayer.getPlayerID());
		
		//modify the grid space
		moveGridSpace.setResidentPlayerID(activePlayer.getPlayerID());
		moveGridSpace.setbOccupied(true);
		
		//adjust the player's score
		int updatedPlayerScore = activePlayer.getCurrentScore() + moveGridSpace.getValue();
		activePlayer.setCurrentScore(updatedPlayerScore);
		
		return resultantState;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
