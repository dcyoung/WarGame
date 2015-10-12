/**
 * M1DeathBlitz:
 * 		Specific sublcass of "Move" which defines an M1DeathBlitz 
 * 		move in the game of war.
 * 
 * Gameplay Overview:
 * 		From any space you occupy on the board, you can take the 
 * 		one next to it (up, down, left, right, but not diagonally) 
 * 		if it is unoccupied. The space you originally held is still 
 * 		occupied. Thus, you get to create a new piece in the blitzed 
 * 		square. Any enemy touching the square you have taken is 
 * 		conquered and that square is turned to your side (you turn 
 * 		its piece to your side). An M1 Death Blitz can be done even 
 * 		if it will not conquer another piece. Once you have made 
 * 		this move, your turn is over.
 * 
 * @author dcyoung3
 */
import java.util.ArrayList;

public class M1DeathBlitz extends Move {
	
	private ArrayList<GridSpace> conqueredSpaces;
	
	/**
	 * Constructor
	 * @param movingPlayerID
	 * @param gridSpace
	 * @param gameStateNode
	 */
	public M1DeathBlitz(String movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		super("M1DeathBlitz", movingPlayerID, gridSpace, gameStateNode);
		this.conqueredSpaces = new ArrayList<GridSpace>();
		this.determineConqueredSpaces();
	}
	
	/**
	 * Executes the move on the associated state and returns the resultant state.
	 * @return The resultant GameStateNode after the move is executed.
	 */
	public GameStateNode makeMove(){
		
		//create a clone of the state so that making the move won't mess with the original
		GameStateNode resultantState = this.gameStateNode.deepCopyGameStateNode();
		GridSpace moveGridSpace = resultantState.getBoardState().getGrid().get(this.gridSpace.getRow()).get(this.gridSpace.getCol());
		
		//modify the grid space moved into by the moving player, and note score addition
		moveGridSpace.setResidentPlayerID(this.movingPlayer.getPlayerID());
		moveGridSpace.setbOccupied(true);
		
		Player activePlayer = resultantState.getPlayerByID(this.movingPlayer.getPlayerID());
		activePlayer.setCurrentScore(activePlayer.getCurrentScore() + moveGridSpace.getValue());
		
		//Adjust any conquered spaces, and adjust scores for both players accordingly
		Player conqueredPlayer;
		GridSpace conqueredSpace;
		for(GridSpace gs : this.getConqueredSpaces()){
			//space being conquered (belonging to the resultant state instance)
			conqueredSpace = resultantState.getBoardState().getGrid().get(gs.getRow()).get(gs.getCol());
			
			//remove the previously occupying character and adjust score 
			conqueredPlayer = resultantState.getPlayerByID(conqueredSpace.getResidentPlayerID());
			conqueredPlayer.setCurrentScore(conqueredPlayer.getCurrentScore()-conqueredSpace.getValue()); 
			
			//occupy the space with the moving player and adjust score
			conqueredSpace.setResidentPlayerID(this.movingPlayer.getPlayerID());
			activePlayer.setCurrentScore(activePlayer.getCurrentScore()+ conqueredSpace.getValue());
		}
		
		return resultantState;
	}
	
	/**
	 * determines the grid spaces which will be conquered if this move is executed
	 */
	private void determineConqueredSpaces(){
		ArrayList<GridSpace> neighbors = this.gridSpace.getNeighboringGridSpaces();
		
		//for each grid space that is blitzable (left, above, right, below)
		for( GridSpace gs : neighbors ){
			String occupyingPlayerID;
			if(gs.isOccupied()){
				occupyingPlayerID = gs.getResidentPlayerID();
				if(!occupyingPlayerID.equals(this.movingPlayer.getPlayerID())){
					//if the space is occupied by the other player, add it to conquered spaces
					this.conqueredSpaces.add(gs);
				}
			}
		}
		
	}
	
	public ArrayList<GridSpace> getConqueredSpaces(){
		return this.conqueredSpaces;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
