/**
 * TestRunner:
 * 		Some basic tests which are runnable from a static main 
 * 		method. This should be replaced by dedicated tests in 
 * 		the proper maven file hierarchy.
 * 
 * @author dcyoung3
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class TestRunner {

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
	private GameStateNode getPostSearchedMoveState(GameStateNode state, boolean player1Move, boolean useAlphaBeta,int mmDepth, int abDepth) {
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
		
		for( GameStateNode child : children ){
			AdversarialSearch mmSearch = new AdversarialSearch(child, mmDepth, abDepth, useAlphaBeta);
			tempVal = mmSearch.conductSearch();
			if(tempVal > bestValSoFar){
				bestChoice = child;
				bestValSoFar = tempVal;
			}
		}
		//System.out.println("expected utility: " + bestValSoFar);
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
	public void testMiniMaxAdversaryMoves(GameStateNode state, boolean p1UseAlphaBeta, boolean p2UseAlphaBeta, int mmDepth, int abDepth, int sleepDuration){
		DrawingBoard db = new DrawingBoard(state);
		GameStateNode changingState = state;
		int moveCount = 0;
		long moveStartTime;
		long moveDuration;
		while(!changingState.isLeafNode()){
			moveStartTime = System.currentTimeMillis();
			//player1 moves on even count, player2 on odd
			if( moveCount%2 == 0){
				//System.out.println("Move #" + (moveCount+1) + "\t\t [Player 1] :");
				changingState = getPostSearchedMoveState(changingState, true, p1UseAlphaBeta, mmDepth, abDepth);
			}
			else{
				//System.out.println("Move #" + (moveCount+1) + "\t\t [Player 2] :");
				changingState = getPostSearchedMoveState(changingState, false, p2UseAlphaBeta, mmDepth, abDepth);
			}
			//moveDuration = System.currentTimeMillis() - moveStartTime;
			//System.out.println(moveDuration + "ms");
			db.setGameStateNode(changingState);
			db.drawCurrentBoardState();
			sleep(sleepDuration);
			moveCount++;
		}
		System.out.println("\nA-B: [p1,p2] = [" + p1UseAlphaBeta + ", " + p2UseAlphaBeta + "],\tMM-Depth: " + mmDepth + "\tAB-Depth: " + abDepth );
		this.printGameResults(changingState);
	}
	
	
			

	private void printGameResults(GameStateNode state){
		
		
		System.out.println("Results:");
		System.out.println("Player ["+ state.getPlayer1().getPlayerID() + "],\tscore: " + state.getPlayer1().getCurrentScore() );
		System.out.println("Player ["+ state.getPlayer2().getPlayerID() + "],\tscore: " + state.getPlayer2().getCurrentScore() );
		System.out.println("Winning Player: Player [" + state.getLeadingPlayer().getPlayerID() + "]" );
	}
	
	private void sleep(int duration){
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public GameStateNode createTestGameState(){
		//File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		//File gameBoardFile = new File("./src/main/resources/game_boards/easy.txt");
		//File gameBoardFile = new File("./src/main/resources/game_boards/Narvik.txt");
		File gameBoardFile = new File("./src/main/resources/game_boards/Keren.txt");
		
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
		
		int mmDepth = 3;
		int abDepth = 4;
		int viewMovePauseDuration = 0; //increase to watch each move
		long startTime; 
		
		
		/*TEST: 
		 * p1 = minimax, 
		 * p2 = minimax*/
		startTime = System.currentTimeMillis();
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), false, false, mmDepth, abDepth, viewMovePauseDuration);
		System.out.println("Total game duration: " + (System.currentTimeMillis() - startTime) );
		
		/*TEST: 
		 * p1 = minimax, 
		 * p2 = alpha beta*/
		startTime = System.currentTimeMillis();
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), false, true, mmDepth, abDepth, viewMovePauseDuration);
		System.out.println("Total game duration: " + (System.currentTimeMillis() - startTime) );
		
		/*TEST: 
		 * test p1 = alpha beta, 
		 * p2 = minimax*/
		startTime = System.currentTimeMillis();
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), true, false, mmDepth, abDepth, viewMovePauseDuration);
		System.out.println("Total game duration: " + (System.currentTimeMillis() - startTime) );
		
		/*TEST: 
		 * p1 = alpha beta, 
		 * p2 = alpha beta*/
		startTime = System.currentTimeMillis();
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState(), true, true, mmDepth, abDepth, viewMovePauseDuration);
		System.out.println("Total game duration: " + (System.currentTimeMillis() - startTime) );
		
	}


}
