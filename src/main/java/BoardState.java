/**
 * BoardState: holds the state of the game board, most notably 
 * the state of all grid spaces including their value and occupant
 * @author dcyoung3
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardState {
	//a grid containing the state of all current grid spaces
	private ArrayList<ArrayList<GridSpace>> grid;
	private int numGridRows;
	private int numGridCols;
	
	/**
	 * Constructor
	 * @param numGridRows
	 * @param numGridCols
	 * @param initialGridVals - the numerical values associated with each grid space read from the input file for this gameboard
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
	
	/**
	 * Populates the grid with new grid space objects and sets 
	 * their initial state (value and no occupant)
	 * @param initialGridVals
	 */
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
	
	/**
	 * handles initialization work that will be called both when
	 * constructing a new board state and when deep copying a board state
	 * specifically, determines all the neighboring grid spaces for each
	 * grid space which must be instance specific (hence needs to be repeated
	 * for the deepcopy)
	 */
	public void postInitializeGrid(){
		//for each grid space, populate it's neighboring spaces array
		for(ArrayList<GridSpace> row : this.grid){
			for(GridSpace gs : row){
				gs.setNeighboringGridSpaces(this.determineGridSpaceNeighbors(gs));
			}
		}
	}
	
	/**
	 * Determines the neighbors that are adjacent to a gridspace
	 * Only considers directly adjacent, not diagonal.
	 * @param gridSpace
	 * @return an array of grid spaces that are adjacent to the input gridspace
	 */
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
	
	/**
	 * simple print of the grid values for testing purposes
	 */
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

	/**
	 * Deep copies the board state (clone) so that it can be
	 * manipulated without altering the original.
	 * @return a clone of boardstate
	 */
	public BoardState deepCopyBoardState(){
		BoardState newBS = new BoardState();
		newBS.setNumGridRows(this.numGridRows);
		newBS.setNumGridCols(this.numGridCols);
		ArrayList<ArrayList<GridSpace>> gridCopy = deepCopyGrid();
		newBS.setGrid(gridCopy);
		newBS.postInitializeGrid();
		return newBS;
	}
	
	/**
	 * Deep copies the grid, used when deep copying the board state
	 * @return a clone of grid
	 */
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

	/**
	 * 
	 * @return true if every grid space on the grid is occupied
	 */
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

	/**
	 * Given a player and a gamestate, determine what moves are permitable for that player 
	 * @param playerID
	 * @param state
	 * @return an array of move objects that are permitable for the active player in the input state
	 */
	public ArrayList<Move> getAllowableMoves(String playerID, GameStateNode state) {
		ArrayList<Move> allowableMoves = new ArrayList<Move>();
		
		//check every grid space to see if the player can move into it
		for(int row = 0; row < this.numGridRows; row ++){
			for(int col = 0; col < this.numGridCols; col ++){
				GridSpace gridSpace = this.grid.get(row).get(col);
				//if the grid space is unoccupied
				if(!gridSpace.isOccupied()){
					//add a new move of type CommandoParaDrop
					allowableMoves.add(new CommandoParaDrop(playerID, gridSpace, state));
					
					//Check if a blitz is allowed
					//if the grid space has a neighbor that belongs to the moving player
					for(GridSpace neighbor : gridSpace.getNeighboringGridSpaces()){
						if(neighbor.isOccupied()){
							if(neighbor.getResidentPlayerID().equals(playerID)){
								//add a new move of type M1DeathBlitz
								allowableMoves.add(new M1DeathBlitz(playerID, gridSpace, state));
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
