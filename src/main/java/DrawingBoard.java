import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;

//import java.awt.color.*;


public class DrawingBoard {
	private int numRows;
	private int numCols;
	private GameStateNode state;
	private String bluePlayerID;
	private String greenPlayerID;
	
	DrawingBoard(GameStateNode state){
		this.state = state;
		this.numRows = state.getBoardState().getNumGridRows();
		this.numCols = state.getBoardState().getNumGridCols();
		this.bluePlayerID = state.getPlayer1().getPlayerID();
		this.greenPlayerID = state.getPlayer2().getPlayerID();
		createCanvas();
		
		int style = Font.BOLD | Font.ITALIC;
		Font font = new Font ("Garamond", style , 36);
		//Font font = new Font ("Arial", style , 36);
		StdDraw.setFont(font);
	}
	
	public void setGameStateNode(GameStateNode n){
		this.state = n;
	}
	
	public void drawInitialState(){
		drawBlankGrid();
		drawGridAllGridValues();
		StdDraw.show(5);
	}
	
	public void createCanvas(){
		int canvasScale = 200;
		int maxCanvasSize = 900;
		while(this.numCols*canvasScale > maxCanvasSize || this.numRows*canvasScale > maxCanvasSize){
			canvasScale = canvasScale - 5;
			if(canvasScale <= 0){
				canvasScale = 1;
				break;
			}
		}
		StdDraw.setCanvasSize(this.numCols*canvasScale, this.numRows*canvasScale);
		StdDraw.setXscale(0, this.numCols);
        StdDraw.setYscale(0, this.numRows);
	}
	
	public void drawBlankGridSpace(int row, int col){
		StdDraw.setPenRadius();
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledSquare(col+0.5, (this.numRows-1-row)+0.5, .45 );
	}
	
	public void drawBlankGrid(){
		for(int row = 0; row < this.numRows; row++){
			for(int col = 0; col < this.numCols; col++){
				this.drawBlankGridSpace(row, col);
			}
		}
	}
	
	private void drawSingleGridSpaceValue(int row, int col) {
		StdDraw.setPenColor(StdDraw.WHITE);
		int value = this.state.getBoardState().getGrid().get(row).get(col).getValue();
		StdDraw.text(col+0.5, (this.numRows-1-row)+0.5, ""+ value);
	}
	
	private void drawGridAllGridValues() {
		for(int row = 0; row < this.numRows; row++){
			for(int col = 0; col < this.numCols; col++){
				this.drawSingleGridSpaceValue(row, col);
			}
		}
	}
	
	private void drawColoredCircle(int row, int col, Color playerColor){
		StdDraw.setPenRadius();
		StdDraw.setPenColor(playerColor);
		StdDraw.filledCircle(col+0.5, (this.numRows-1-row)+0.5, .45 );
	}
	
	private void drawSingleGridSpacePlayerColor(int row, int col) {
		GridSpace gs = this.state.getBoardState().getGrid().get(row).get(col);
		if(!gs.isOccupied()){
			return;
		}
		else{
			if(gs.getResidentPlayerID().equals(this.bluePlayerID)){
				drawColoredCircle(row, col, StdDraw.BLUE);
			}
			else{
				drawColoredCircle(row, col, StdDraw.GREEN);
			}
		}
	}
	
	private void drawAllGridSpacePlayerColor(){
		for(int row = 0; row < this.numRows; row++){
			for(int col = 0; col < this.numCols; col++){
				this.drawSingleGridSpacePlayerColor(row, col);
			}
		}
	}
	
	
	
	private void drawCurrentBoardState(){
		this.drawBlankGrid();
		this.drawAllGridSpacePlayerColor();
		this.drawGridAllGridValues();
		StdDraw.show(5);
	}

	public static void main(String[] args) {
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		//System.out.println(fr.getNumGridRows() + ", " + fr.getNumGridRows());
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
		
		Player p1 = new Player("player1", false, 0);
		Player p2 = new Player("player2", false, 0);
		GameStateNode state = new GameStateNode(p1, p2, bs);
		
		DrawingBoard db = new DrawingBoard(state);
		db.drawInitialState();
		
	}

}
