
public class CommandoParaDrop extends Move {
	
	
	public CommandoParaDrop(Player movingPlayer, GridSpace gridSpace, GameStateNode gameStateNode){
		super("CommandoParaDrop",movingPlayer, gridSpace, gameStateNode);
	}
	
	public boolean makeMove(){
		if(!isAllowable(this.gameStateNode)){
			return false;
		}
		//FIXME: finish
		return true;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
