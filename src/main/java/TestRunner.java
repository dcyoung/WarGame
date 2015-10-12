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
	
	public void testBoardStateFromFile(String filename){
		File gameBoardFile = new File(filename);
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		bs.printGridVals();
	}
	
	
	public GameStateNode getPostRandomMoveState(GameStateNode state, boolean player1Move){
		ArrayList<Move> allowableMoves;
		if(player1Move){
			allowableMoves = state.getAllowableMoves(state.getPlayer1());
		}
		else{
			allowableMoves = state.getAllowableMoves(state.getPlayer2());
		}
		
		if( allowableMoves.size() > 0 ){
			Random random = new Random();
			Move m = allowableMoves.get(random.nextInt(allowableMoves.size()));
			if(m instanceof CommandoParaDrop){
				CommandoParaDrop dropMove = (CommandoParaDrop) m;
				return dropMove.makeMove();
			}
			else if(m instanceof M1DeathBlitz){
				M1DeathBlitz blitzMove = (M1DeathBlitz) m;
				return blitzMove.makeMove();
			}
			else{
				System.out.println("Shouldn't have reached.");
			}
		}
		return null;
	}
	
	public void testRandomAdversaryMoves(GameStateNode state){
		DrawingBoard db = new DrawingBoard(state);
		GameStateNode changingState = state;
		int moveCount = 0;
		while(!changingState.isLeafNode()){
			//player1 moves on even count, player2 on odd
			if( moveCount%2 == 0){
				changingState = getPostRandomMoveState(changingState, true);
			}
			else{
				changingState = getPostRandomMoveState(changingState, false);
			}
			db.setGameStateNode(changingState);
			db.drawCurrentBoardState();
			moveCount++;
			sleep(500);
		}
		this.printGameResults(changingState);
	}
	
	private GameStateNode getPostSearchedMoveState(GameStateNode state, boolean player1Move) {
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
			AdversarialSearch mmSearch = new AdversarialSearch(child, 3);
			tempVal = mmSearch.conductSearch();
			if(tempVal > bestValSoFar){
				bestChoice = child;
				bestValSoFar = tempVal;
			}
		}
		return bestChoice;
	}
	
	
	public void testMiniMaxAdversaryMoves(GameStateNode state){
		DrawingBoard db = new DrawingBoard(state);
		GameStateNode changingState = state;
		int moveCount = 0;
		while(!changingState.isLeafNode()){
			//player1 moves on even count, player2 on odd
			if( moveCount%2 == 0){
				changingState = getPostSearchedMoveState(changingState, true);
			}
			else{
				changingState = getPostSearchedMoveState(changingState, false);
			}
			db.setGameStateNode(changingState);
			db.drawCurrentBoardState();
			moveCount++;
			sleep(500);
		}
		this.printGameResults(changingState);
	}
	
	
			

	private void printGameResults(GameStateNode state){
		System.out.println("Results:");
		System.out.println("Player ["+ state.getPlayer1().getPlayerID() + "] score: " + state.getPlayer1().getCurrentScore() );
		System.out.println("Player ["+ state.getPlayer2().getPlayerID() + "] score: " + state.getPlayer2().getCurrentScore() );
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
		File gameBoardFile = new File("./src/main/resources/game_boards/Smolensk.txt");
		GameBoardFileReader fr = new GameBoardFileReader(gameBoardFile);
		BoardState bs = new BoardState(fr.getNumGridRows(), fr.getNumGridCols(), fr.getGridVals());
		
		Player p1 = new Player("player1", false, 0);
		Player p2 = new Player("player2", false, 0);
		return new GameStateNode(p1, p2, bs);
	}
	
	
	public static void main(String[] args) {
		TestRunner tr = new TestRunner();
		
		//tr.testBoardStateFromFile("./src/main/resources/game_boards/Smolensk.txt");
		//tr.testRandomAdversaryMoves(tr.createTestGameState());
		tr.testMiniMaxAdversaryMoves(tr.createTestGameState());
	}


}
