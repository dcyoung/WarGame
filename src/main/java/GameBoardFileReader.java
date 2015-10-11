import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GameBoardFileReader {

	private Scanner sc;
	private int numGridRows;
	private int numGridCols;
	private ArrayList<ArrayList<Integer>> gridVals;
	
	
	public GameBoardFileReader(File file){
		try{
			this.sc = new Scanner(file);
			this.gridVals = this.readGridVals();
			this.numGridRows = this.gridVals.size();
			
			if( this.numGridRows !=0 )
				this.numGridCols = this.gridVals.get(0).size();
			else
				this.numGridCols = 0;
		}
		catch (FileNotFoundException e) {
			System.out.println("File could not be found.");
			e.printStackTrace();
		}
	}
	
	private ArrayList<ArrayList<Integer>> readGridVals(){
		ArrayList<ArrayList<Integer>> tempGridVals =  new ArrayList<ArrayList<Integer>>();
		int lineCount = 0;
		String tempLine;
		Scanner lineScan;
		
		while(sc.hasNextLine()){
			tempLine = sc.nextLine();
			lineScan = new Scanner(tempLine);
			tempGridVals.add(new ArrayList<Integer>());
			while(lineScan.hasNextInt()){
				tempGridVals.get(lineCount).add(lineScan.nextInt());
			}
			lineCount++;
		}
		return tempGridVals;
	}
	
	
	/**
	 * Deep copies the input array
	 * @param inputArray
	 * @return a copy of the input array which can be saved without 
	 * 			being affected by changes in the input array
	 */
	private ArrayList<Integer> DeepCopyIntArrayList(ArrayList<Integer> inputArray){
		ArrayList<Integer> newArrayList = new ArrayList<Integer>();
		for(int i : inputArray){
			newArrayList.add(i);
		}
		return newArrayList;
	}
	
	
	public int getNumGridRows() {
		return numGridRows;
	}

	public int getNumGridCols() {
		return numGridCols;
	}

	public ArrayList<ArrayList<Integer>> getGridVals() {
		return gridVals;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
