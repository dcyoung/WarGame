import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardState {
	private ArrayList<ArrayList<GridSpace>> grid;
	private int numGridRows;
	private int numGridCols;
	
	/**
	 * 
	 * @param numGridRows
	 * @param numGridCols
	 * @param initialGridVals
	 */
	public BoardState(int numGridRows, int numGridCols, ArrayList<ArrayList<Integer>> initialGridVals){
		this.numGridRows = numGridRows;
		this.numGridCols = numGridCols;
		this.grid = new ArrayList<ArrayList<GridSpace>>();
		this.initializeGrid(initialGridVals);
	}
	
	/**
	 * Empty constructor, for use when deep copying an existing board state
	 */
	public BoardState(){
		
	}
	
	public void initializeGrid(ArrayList<ArrayList<Integer>> initialGridVals){
		
		//initialize the grid of spaces with the initial grid values
		for(int row = 0; row < this.numGridRows; row++ ){
			this.grid.add(new ArrayList<GridSpace>());
			for(int col = 0; col < this.numGridCols; col++ ){
				int val = initialGridVals.get(row).get(col);
				this.grid.get(row).add(new GridSpace(row, col, val));
			}
		}
		this.postInitializeGrid();
	}
	
	public void postInitializeGrid(){
		//for each grid space, populate it's neighboring spaces array
		for(ArrayList<GridSpace> row : this.grid){
			for(GridSpace gs : row){
				gs.setNeighboringGridSpaces(this.determineGridSpaceNeighbors(gs));
			}
		}
	}
	
	public ArrayList<GridSpace> determineGridSpaceNeighbors(GridSpace gridSpace){
		ArrayList<GridSpace> neighbors = new ArrayList<GridSpace>();
		//add left, top, right, bottom
		if( gridSpace.getCol() != 0 )
			neighbors.add(this.grid.get(gridSpace.row).get(gridSpace.col-1));
		if( gridSpace.getCol() != this.numGridCols-1 )
			neighbors.add(this.grid.get(gridSpace.row).get(gridSpace.col+1));
		if( gridSpace.getRow() != 0 )
			neighbors.add(this.grid.get(gridSpace.row-1).get(gridSpace.col));
		if( gridSpace.getRow() != this.numGridRows-1 )
			neighbors.add(this.grid.get(gridSpace.row+1).get(gridSpace.col));
		
		return neighbors;
	}
	
	public void printGridVals(){
		for(ArrayList<GridSpace> row : this.grid){
			for(GridSpace gs : row){
				System.out.print(gs.getValue() + "\t");
				//System.out.print(col.getValue() + "," + col.getNeighboringGridSpaces().size()+ "\t");
			}
			System.out.println("\n");
		}
	}
	
	
	
	
	
	
	
	public ArrayList<ArrayList<GridSpace>> getGrid() {
		return grid;
	}

	public void setGrid(ArrayList<ArrayList<GridSpace>> grid) {
		this.grid = grid;
	}

	public int getNumGridRows() {
		return numGridRows;
	}

	public void setNumGridRows(int numGridRows) {
		this.numGridRows = numGridRows;
	}

	public int getNumGridCols() {
		return numGridCols;
	}

	public void setNumGridCols(int numGridCols) {
		this.numGridCols = numGridCols;
	}

	public BoardState deepCopyBoardState(){
		BoardState newBS = new BoardState();
		newBS.setNumGridRows(this.numGridRows);
		newBS.setNumGridCols(this.numGridCols);
		ArrayList<ArrayList<GridSpace>> gridCopy = deepCopyGrid();
		newBS.setGrid(gridCopy);
		newBS.postInitializeGrid();
		return newBS;
	}
	
	
	private ArrayList<ArrayList<GridSpace>> deepCopyGrid() {
		ArrayList<ArrayList<GridSpace>> gridCopy = new ArrayList<ArrayList<GridSpace>>();
		for(int row = 0; row < this.numGridRows; row++ ){
			gridCopy.add(new ArrayList<GridSpace>());
			for(int col = 0; col < this.numGridCols; col++ ){
				GridSpace gridSpaceCopy = this.grid.get(row).get(col).deepCopyGridSpace();
				gridCopy.get(row).add(gridSpaceCopy);
			}
		}
		return gridCopy;
	}

	public boolean isGridFilled() {
		for(ArrayList<GridSpace> row : this.grid){
			for(GridSpace gs : row){
				if(!gs.isOccupied()){
					return false;
				}
			}
		}
		return true;
	}

	public ArrayList<Move> getAllowableMoves(String playerID, GameStateNode n) {
		ArrayList<Move> allowableMoves = new ArrayList<Move>();
		GameStateNode stateNodeClone;
		GridSpace gridSpaceClone;
		
		//check every grid space to see if the player can move into it
		for(int row = 0; row < this.numGridRows; row ++){
			for(int col = 0; col < this.numGridCols; col ++){
				GridSpace gs = this.grid.get(row).get(col);
				//if the grid space is unoccupied
				if(!gs.isOccupied()){
					//clone the game state so we don't mess with the current one
					stateNodeClone = n.deepCopyGameStateNode();
					gridSpaceClone = stateNodeClone.getBoardState().getGrid().get(row).get(col);
					
					//add a new move of type CommandoParaDrop
					allowableMoves.add(new CommandoParaDrop(playerID, gridSpaceClone, stateNodeClone));
					
					//Check if a blitz is allowed
					//if the grid space has a neighbor that belongs to the moving player
					for(GridSpace neighbor : gs.getNeighboringGridSpaces()){
						if(neighbor.isOccupied()){
							if(neighbor.getResidentPlayerID().equals(playerID)){
								//clone the game state so we don't mess with the current one
								stateNodeClone = n.deepCopyGameStateNode();
								gridSpaceClone = stateNodeClone.getBoardState().getGrid().get(row).get(col);
								//add a new move of type M1DeathBlitz
								allowableMoves.add(new M1DeathBlitz(playerID, gridSpaceClone, stateNodeClone));
								break;
							}
						}
					}
				}
			}
		}
		
		return allowableMoves;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		//System.out.println(fr.getNumGridRows() + ", " + fr.getNumGridRows());
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
		
//		System.out.println();
//		BoardState newBS = bs.deepCopyBoardState();
//		newBS.printGridVals();
	}

}
