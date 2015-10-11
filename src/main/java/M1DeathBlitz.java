import java.util.ArrayList;

public class M1DeathBlitz extends Move {
	
	private ArrayList<GridSpace> conqueredSpaces;
	
	public M1DeathBlitz(Player movingPlayer, GridSpace gridSpace, GameStateNode gameStateNode){
		super("M1DeathBlitz", movingPlayer, gridSpace, gameStateNode);
		this.conqueredSpaces = new ArrayList<GridSpace>();
		this.determineConqueredSpaces();
	}
	
	public boolean makeMove(){
		if(!isAllowable(this.gameStateNode)){
			return false;
		}
		//FIXME: finish
		return true;
	}
	
	
	
	
	private void determineConqueredSpaces(){
		ArrayList<GridSpace> neighbors = this.gridSpace.getNeighboringGridSpaces();
		
		//for each grid space that is blitzable (left, above, right, below)
		for( GridSpace gs : neighbors ){
			String occupyingPlayerID;
			if(gs.isbOccupied()){
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
