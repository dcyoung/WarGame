import java.util.ArrayList;

public class GridNode {

	public int row;
	public int col;
	public int value;
	private boolean bOccupied;
	private String residentPlayerID;
	private ArrayList<GridNode> neighborNodes;
	
	public GridNode(int row, int col, int value){
		this.row = row;
		this.col = col;
		this.value = value;
	}
	
	
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isbOccupied() {
		return bOccupied;
	}

	public void setbOccupied(boolean bOccupied) {
		this.bOccupied = bOccupied;
	}

	public String getResidentPlayerID() {
		return residentPlayerID;
	}

	public void setResidentPlayerID(String residentPlayerID) {
		this.residentPlayerID = residentPlayerID;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public ArrayList<GridNode> getNeighborNodes() {
		return neighborNodes;
	}

	public void setNeighborNodes(ArrayList<GridNode> neighborNodes) {
		this.neighborNodes = neighborNodes;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
