/**
 * TestRunner:
 * 		Some basic tests which are runnable from a static main 
 * 		method. This should be replaced by dedicated tests in 
 * 		the proper maven file hierarchy.
 * 
 * @author dcyoung3
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;

public class TestRunner {

	private String puzzleName;
	private String folderName;
	private PrintWriter durationWriterP1;
	private PrintWriter durationWriterP2;
	private PrintWriter expandedNodesWriterP1;
	private PrintWriter expandedNodesWriterP2;
	private PrintWriter summaryWriter;
	


	/**
	 * Constructor
	 */
	public TestRunner(){
		
	}
	
	/**
	 * 
	 * @param filename
	 */
	public void testBoardStateFromFile(String filename){
		File gameBoardFile = new File(filename);
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
	}
	
	/**
	 * 
	 * @param state
	 * @param player1Move
	 * @param useAlphaBeta
	 * @param mmDepth
	 * @param abDepth
	 * @return
	 */
	private GameStateNode getPostSearchedMoveState(GameStateNode state, boolean player1Move, boolean useAlphaBeta,int mmDepth, int abDepth){
		Player maximizingPlayer;
		if(player1Move){
			maximizingPlayer = state.getPlayer1();
			state.getPlayer1().setMaximizingPlayer(true);
			state.getPlayer2().setMaximizingPlayer(false);
		}
		else{
			maximizingPlayer = state.getPlayer2();
			state.getPlayer1().setMaximizingPlayer(false);
			state.getPlayer2().setMaximizingPlayer(true);
		}
		
		ArrayList<GameStateNode> children = state.getNodeChildren(maximizingPlayer);
		GameStateNode bestChoice = null;
		int bestValSoFar = Integer.MIN_VALUE;
		int tempVal;
		
		int moveExpandedNodes = 0;
		for( GameStateNode child : children ){
			AdversarialSearch mmSearch = new AdversarialSearch(child, mmDepth, abDepth, useAlphaBeta);
			tempVal = mmSearch.conductSearch();
			moveExpandedNodes += mmSearch.getNumExpandedNodes();
			if(tempVal > bestValSoFar){
				bestChoice = child;
				bestValSoFar = tempVal;
			}
		}
		
		if(player1Move){
			this.expandedNodesWriterP1.println(moveExpandedNodes);
		}
		else{
			this.expandedNodesWriterP2.println(moveExpandedNodes);
		}
		return bestChoice;
	}
	
	/**
	 * 
	 * @param state
	 * @param p1UseAlphaBeta
	 * @param p2UseAlphaBeta
	 * @param mmDepth
	 * @param abDepth
	 * @param sleepDuration
	 */
	public void testMiniMaxAdversaryMoves(GameStateNode state, boolean p1UseAlphaBeta, boolean p2UseAlphaBeta, int mmDepth, int abDepth, int sleepDuration) {
		this.folderName = "./src/main/resources/savedGameplay/" + this.puzzleName + "/";
		if(p1UseAlphaBeta){this.folderName += "ABvs";}else{this.folderName += "MMvs";}
		if(p2UseAlphaBeta){this.folderName += "AB/";}else{this.folderName += "MM/";}
		try {
			this.durationWriterP1 = new PrintWriter(this.folderName + "move_durations_p1.txt", "UTF-8");
			this.durationWriterP2 = new PrintWriter(this.folderName + "move_durations_p2.txt", "UTF-8");
			this.expandedNodesWriterP1 = new PrintWriter(this.folderName + "move_expanded_nodes_p1.txt", "UTF-8");
			this.expandedNodesWriterP2 = new PrintWriter(this.folderName + "move_expanded_nodes_p2.txt", "UTF-8");
			this.summaryWriter = new PrintWriter(this.folderName + "summary.txt", "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long startTime = System.currentTimeMillis();
		
		
		
		System.out.println("-----------------------------------New Game---------------------------------------\n\n");
		DrawingBoard db = new DrawingBoard(state);
		GameStateNode changingState = state;
		int moveCount = 0;
		long moveStartTime;
		long moveDuration;
		
		while(!changingState.isLeafNode()){
			moveStartTime = System.currentTimeMillis();
			//player1 moves on even count, player2 on odd
			if( moveCount%2 == 0){
				//System.out.println("Begin Move #" + (moveCount+1) + "\t\t [Player 1] :");
				changingState = getPostSearchedMoveState(changingState, true, p1UseAlphaBeta, mmDepth, abDepth);
				moveDuration = System.currentTimeMillis() - moveStartTime;
				this.durationWriterP1.println(moveDuration);
			}
			else{
				//System.out.println("Begin Move #" + (moveCount+1) + "\t\t [Player 2] :");
				changingState = getPostSearchedMoveState(changingState, false, p2UseAlphaBeta, mmDepth, abDepth);
				moveDuration = System.currentTimeMillis() - moveStartTime;
				this.durationWriterP2.println(moveDuration);
			}
			//System.out.println(moveDuration + "ms");
			
			db.setGameStateNode(changingState);
			db.drawCurrentBoardState();
			db.saveImage(this.folderName + moveCount + ".png");
			sleep(sleepDuration);
			moveCount++;
		}
		System.out.println("\nA-B: [p1,p2] = [" + p1UseAlphaBeta + ", " + p2UseAlphaBeta + "],\tMM-Depth: " + mmDepth + "\tAB-Depth: " + abDepth );
		this.printGameResults(changingState, startTime);
		
		this.durationWriterP1.close();
		this.durationWriterP2.close();
		this.expandedNodesWriterP1.close();
		this.expandedNodesWriterP2.close();
		this.summaryWriter.close();
	}
	
	
			

	private void printGameResults(GameStateNode state, long startTime){
		this.summaryWriter.println("Results:");
		this.summaryWriter.println("Player ["+ state.getPlayer1().getPlayerID() + "],\tscore: " + state.getPlayer1().getCurrentScore() );
		this.summaryWriter.println("Player ["+ state.getPlayer2().getPlayerID() + "],\tscore: " + state.getPlayer2().getCurrentScore() );
		this.summaryWriter.println("Winning Player: Player [" + state.getLeadingPlayer().getPlayerID() + "]" );
		this.summaryWriter.println("Total game duration: " + (System.currentTimeMillis() - startTime) );
	}
	
	private void sleep(int duration){
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public GameStateNode createTestGameState(){
		//File gameBoardFile = new File("./src/main/resources/game_boards/easy.txt");
		//this.puzzleName = "easy";
		//File gameBoardFile = new File("./src/main/resources/game_boards/Keren.txt");
		//this.puzzleName = "Keren";
		//File gameBoardFile = new File("./src/main/resources/game_boards/Narvik.txt");
		//this.puzzleName = "Narvik";
		//File gameBoardFile = new File("./src/main/resources/game_boards/Sevastopol.txt");
		//this.puzzleName = "Sevastopol";
		//File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		//this.puzzleName = "Smolensk";
		File gameBoardFile = new File("./src/main/resources/game_boards/Westerplatte.txt");
		this.puzzleName = "Westerplatte";
		
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		
		Player p1 = new Player("player1", false, 0);
		Player p2 = new Player("player2", false, 0);
		return new GameStateNode(p1, p2, bs);
	}
	
	
	
	/**
	 * Main, used to run the tests easily in eclipse. 
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		TestRunner tr = new TestRunner();
		
		int mmDepth = 2;
		int abDepth = 2;
		int viewMovePauseDuration = 0; //increase to watch each move
		
		
		/*TEST: 
		 * p1 = minimax, 
		 * p2 = minimax*/
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), false, false, mmDepth, abDepth, viewMovePauseDuration);
		
		
		/*TEST: 
		 * p1 = minimax, 
		 * p2 = alpha beta*/
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), false, true, mmDepth, abDepth, viewMovePauseDuration);
		
		
		/*TEST: 
		 * test p1 = alpha beta, 
		 * p2 = minimax*/
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), true, false, mmDepth, abDepth, viewMovePauseDuration);
		
		/*TEST: 
		 * p1 = alpha beta, 
		 * p2 = alpha beta*/
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), true, true, mmDepth, abDepth, viewMovePauseDuration);
		
	}


}
