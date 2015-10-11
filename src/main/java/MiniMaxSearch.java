
public class MiniMaxSearch {
	private BoardState bs;
	
	public MiniMaxSearch(BoardState bs){
		this.bs = bs;
	}
	/*
	 (the minimax value of n, searched to depth d)
	 fun minimax(n: node, d: int): int =
	   if leaf(n) or depth=0 return evaluate(n)
	   if n is a max node
	      v := L
	      for each child of n
	         v' := minimax (child,d-1)
	         if v' > v, v:= v'
	      return v
	   if n is a min node
	      v := W
	      for each child of n
	         v' := minimax (child,d-1)
	         if v' < v, v:= v'
	      return v
	 */
	public int determineMiniMaxValue(GameStateNode n, int depthLimit){
		if( n.isLeafNode() || depthLimit==0 ){
			return evaluate(n);
		}
		else if(n.isMaxNode()){
			//n is a max node
			
		}
		else{ 
			//n is a min node
			
		}
		return null;
	}
	
	
	public int evaluate(GameStateNode n){
		
		return null;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
