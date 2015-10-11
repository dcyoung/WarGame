import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardState {
	private ArrayList<ArrayList<GridNode>> grid;
	private int numGridRows;
	private int numGridCols;
	
	public BoardState(int numGridRows, int numGridCols, ArrayList<ArrayList<Integer>> initialGridVals){
		this.numGridRows = numGridRows;
		this.numGridCols = numGridCols;
		this.grid = new ArrayList<ArrayList<GridNode>>();
		this.initializeState(initialGridVals);
		this.postInitializeState();
	}
	
	public void initializeState(ArrayList<ArrayList<Integer>> initialGridVals){
		//initialize the grid of nodes with the initial grid values
		for(int row = 0; row < this.numGridRows; row++ ){
			this.grid.add(new ArrayList<GridNode>());
			for(int col = 0; col < this.numGridCols; col++ ){
				int val = initialGridVals.get(row).get(col);
				this.grid.get(row).add(new GridNode(row, col, val));
			}
		}
	}
	
	public void postInitializeState(){
		//for each node, populate it's neighboring nodes
		for(ArrayList<GridNode> row : this.grid){
			for(GridNode node : row){
				node.setNeighborNodes(this.determineNodeNeighbors(node));
			}
		}
	}
	
	public ArrayList<GridNode> determineNodeNeighbors(GridNode node){
		ArrayList<GridNode> neighbors = new ArrayList<GridNode>();
		//add left, top, right, bottom
		if( node.getCol() != 0 )
			neighbors.add(this.grid.get(node.row).get(node.col-1));
		if( node.getCol() != this.numGridCols-1 )
			neighbors.add(this.grid.get(node.row).get(node.col+1));
		if( node.getRow() != 0 )
			neighbors.add(this.grid.get(node.row-1).get(node.col));
		if( node.getRow() != this.numGridRows-1 )
			neighbors.add(this.grid.get(node.row+1).get(node.col));
		
		return neighbors;
	}
	
	public void printGridVals(){
		for(ArrayList<GridNode> row : this.grid){
			for(GridNode node : row){
				System.out.print(node.getValue() + "\t");
			}
			System.out.println("\n");
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		//System.out.println(fr.getNumGridRows() + ", " + fr.getNumGridRows());
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
	}

}
