import java.io.File;
import java.util.ArrayList;

public class TestRunner {

	public TestRunner(){
		
	}
	
	public void testBoardStateFromFile(String filename){
		File gameBoardFile = new File(filename);
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
	}
	
	public void testDrawingBoard(){
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		
		Player p1 = new Player("player1", false, 0);
		Player p2 = new Player("player2", false, 0);
		GameStateNode state = new GameStateNode(p1, p2, bs);
		
		DrawingBoard db = new DrawingBoard(state);
		db.drawInitialState();
	}
	
	public void testAllowableMoves(){
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		
		Player p1 = new Player("player1", false, 0);
		Player p2 = new Player("player2", false, 0);
		GameStateNode state = new GameStateNode(p1, p2, bs);
		
		DrawingBoard db = new DrawingBoard(state);
		db.drawInitialState();
		
		ArrayList<Move> allowableMoves = state.getAllowableMoves(p1);
		int dropCount = 0;
		int blitzCount = 0;
		for(Move m : allowableMoves){
			if(m instanceof CommandoParaDrop){
				dropCount++;
			}
			else if(m instanceof M1DeathBlitz){
				blitzCount++;
			}
			else{
				System.out.println("Shouldn't have reached.");
			}
		}
		System.out.println("Allowable Moves:");
		System.out.println("Number of para drops -> " + dropCount);
		System.out.println("Number of death blitz -> " + blitzCount);
	}
	
	
	public static void main(String[] args) {
		TestRunner tr = new TestRunner();
		
		//tr.testBoardStateFromFile("./src/main/resources/game_boards/Smolensk.txt");
		//tr.testDrawingBoard();
		tr.testAllowableMoves();
	}

}
