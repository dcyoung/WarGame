import java.util.ArrayList;

public class M1DeathBlitz extends Move {
	
	private ArrayList<GridSpace> conqueredSpaces;
	
	public M1DeathBlitz(String movingPlayerID, GridSpace gridSpace, GameStateNode gameStateNode){
		super("M1DeathBlitz", movingPlayerID, gridSpace, gameStateNode);
		this.conqueredSpaces = new ArrayList<GridSpace>();
		this.determineConqueredSpaces();
	}
	
	public void makeMove(){
		//modify the original grid space moved into by the moving player, and note score addition
		this.gridSpace.setResidentPlayerID(this.movingPlayer.getPlayerID());
		this.gridSpace.setbOccupied(true);
		this.movingPlayer.setCurrentScore(this.movingPlayer.getCurrentScore() + this.gridSpace.getValue());
		
		//Adjust any conquered spaces, and adjust scores for both players accordingly
		Player conqueredPlayer;
		for(GridSpace conqueredSpace : this.getConqueredSpaces()){
			//remove the previously occupying character and adjust score 
			conqueredPlayer = this.gameStateNode.getPlayerByID(conqueredSpace.getResidentPlayerID());
			conqueredPlayer.setCurrentScore(conqueredPlayer.getCurrentScore()-conqueredSpace.getValue()); 
			
			//occupy the space with the moving player and adjust score
			conqueredSpace.setResidentPlayerID(this.movingPlayer.getPlayerID());
			this.movingPlayer.setCurrentScore(this.movingPlayer.getCurrentScore()+ conqueredSpace.getValue());
		}
	}
	
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
