/**
 * DrawingBoard:
 * 		Drawing board to draw the state of the war game dynamically.
 * 		Makes use of StdDraw.java, a basic 2D graphics library 
 * 		sourced from Princeton.
 * 	
 * @author dcyoung3
 */
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
	private Font valueFont;
	private Font scoreFont;
	
	/**
	 * constructor
	 * @param state
	 */
	DrawingBoard(GameStateNode state){
		this.state = state;
		this.numRows = state.getBoardState().getNumGridRows();
		this.numCols = state.getBoardState().getNumGridCols();
		this.bluePlayerID = state.getPlayer1().getPlayerID();
		this.greenPlayerID = state.getPlayer2().getPlayerID();
		createCanvas();
		
		int style = Font.BOLD | Font.ITALIC;
		this.valueFont = new Font ("Garamond", style , 36);
		this.scoreFont = new Font ("Arial", style , 24);
		
		this.drawInitialState();
	}
	
	public void setGameStateNode(GameStateNode n){
		this.state = n;
	}
	
	private void drawInitialState(){
		drawBlankGrid();
		drawGridAllGridValues();
		StdDraw.show(5);
	}
	
	private void createCanvas(){
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
	
	private void drawBlankGridSpace(int row, int col){
		StdDraw.setPenRadius();
		StdDraw.setPenColor(StdDraw.GRAY);
		StdDraw.filledSquare(col+0.5, (this.numRows-1-row)+0.5, .45 );
	}
	
	private void drawBlankGrid(){
		for(int row = 0; row < this.numRows; row++){
			for(int col = 0; col < this.numCols; col++){
				this.drawBlankGridSpace(row, col);
			}
		}
	}
	
	private void drawSingleGridSpaceValue(int row, int col) {
		StdDraw.setFont(this.valueFont);
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
	
	private void drawPlayerScores(){
		StdDraw.setFont(this.scoreFont);
		StdDraw.setPenColor(StdDraw.ORANGE);
		String p1Name = "" + this.state.getPlayer1().getPlayerID();
		String p2Name = "" + this.state.getPlayer2().getPlayerID();
		String p1Score = ""+ this.state.getPlayer1().getCurrentScore();
		String p2Score = ""+ this.state.getPlayer2().getCurrentScore();
		
		StdDraw.text(1, 6.1, "Player1 [" + p1Name + "] : " + p1Score);
		StdDraw.text(5, 6.1, "Player2 [" + p2Name + "] : " + p2Score);
	}
	
	/**
	 * will draw the current state of the game based off the 
	 * drawing board's instance variable of game state. To draw an
	 * updated picture of the game, set the game state instance 
	 * variable every time the game state changes (using setGameStateNode())
	 * before calling drawCurrentBoardState().
	 */
	public void drawCurrentBoardState(){
		StdDraw.clear();
		this.drawBlankGrid();
		this.drawAllGridSpacePlayerColor();
		this.drawGridAllGridValues();
		this.drawPlayerScores();
		StdDraw.show(5);
	}

	public static void main(String[] args) {
		
	}

}
