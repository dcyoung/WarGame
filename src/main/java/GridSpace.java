/**
 * GridSpace:
 * 		Defines a grid space on the game board in wargame,
 * 		along with all of its associated information.
 * @author dcyoung3
 */
import java.util.ArrayList;

public class GridSpace {

	public int row;
	public int col;
	public int value;
	private boolean bOccupied;
	private String residentPlayerID;
	private ArrayList<GridSpace> neighboringGridSpaces;
	
	/**
	 * Constructor
	 * @param row
	 * @param col
	 * @param value
	 */
	public GridSpace(int row, int col, int value){
		this.row = row;
		this.col = col;
		this.value = value;
		this.bOccupied = false;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public boolean isOccupied() {
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

	public ArrayList<GridSpace> getNeighboringGridSpaces() {
		return neighboringGridSpaces;
	}

	public void setNeighboringGridSpaces(ArrayList<GridSpace> neighborSpaces) {
		this.neighboringGridSpaces = neighborSpaces;
	}
	
	/**
	 * 
	 * @return clone of this grid space
	 */
	public GridSpace deepCopyGridSpace(){
		GridSpace gs = new GridSpace(this.row, this.col, this.value);
		gs.setbOccupied(this.bOccupied);
		gs.setResidentPlayerID(this.residentPlayerID);
		return gs;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}

}
