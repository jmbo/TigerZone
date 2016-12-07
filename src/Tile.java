/***********************************

	Created By: Group N

	Logic:

		Stores the Edges with the
		appropriate properties that
		are associated with a Tile

************************************/

import java.util.HashMap;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

public class Tile {

	//Private Variables
  private String description;
	private int animal;
	private boolean den;
  private boolean croc;
  private boolean goat;
	private int eT;
	private int eL;
	private int eR;
	private int eB;
	private boolean cTL = false;
	private boolean cTR = false;
	private boolean cBL = false;
	private boolean cBR = false;
	private boolean oLR = false;
	private boolean oTB = false;
	private int beenRotated = 0;
	private int[][] miniZones;

	private Position position;


	//Public Constructors for the Tile Class

		//Empty Constructor
		//Using for Testing purpose
	public Tile(){
		miniZones = new int[3][3];

		for(int i=0; i < miniZones.length; i++){
			for(int j=0; j < miniZones.length; j++){

				Random rand = new Random();
				int n = rand.nextInt(3);

				miniZones[i][j] = n;
			}

		}
	}

	public Tile(Position pos){
		this.position = pos;
		miniZones = new int[3][3];
	}


	public Tile(int animal, boolean den, boolean croc, int eT, int eL, int eR, int eB, boolean cTL, boolean cTR, boolean cBL, boolean cBR, boolean oLR, boolean oTB, String description, int beenRotated, int[][] miniZones)
	{
		this.animal = animal;
		this.den = den;
    this.croc = croc;
    this.goat = false;
		this.eT = eT;
		this.eL = eL;
		this.eR = eR;
		this.eB = eB;
		this.cTL = cTL;
		this.cTR = cTR;
		this.cBL = cBL;
		this.cBR = cBR;
		this.oLR = oLR;
		this.oTB = oTB;
    this.description = description;
    this.beenRotated = beenRotated;
    this.miniZones = miniZones;
	}

	//Getter

    public String getDescription(){
        return description;
    }

	public Position getPosition()
	{
		return position;
	}

	public int getAnimal()
	{
		return animal;
	}

	public boolean getDen()
	{
		return den;
	}

  public boolean getCroc(){
      return croc;
  }

  public void setGoat(){
    this.goat = true;
  }
  public boolean getGoat(){
    return goat;
  }
	public int getEdgeT()
	{
		return eT;
	}

	public int getEdgeL()
	{
		return eL;
	}

	public int getEdgeR()
	{
		return eR;
	}

	public int getEdgeB()
	{
		return eB;
	}

	public boolean getCTL()
	{
		return cTL;
	}

	public boolean getCTR()
	{
		return cTR;
	}

	public boolean getCBR()
	{
		return cBR;
	}

	public boolean getCBL()
	{
		return cBL;
	}

	public boolean getOLR()
	{
		return oLR;
	}

	public boolean getOTB()
	{
		return oTB;
	}

	public int[][] getMiniZones(){
		return miniZones;
	}

	//Setter

	public void setPosition(Position pos)
	{
		this.position = pos;
	}

	public String printTile()
	{
		return ("cTL is " + this.cTL + ". cTR is " + this.cTR + ". cBL is " + this.cBL + ". cBR is " + this.cBR
			+ "\neT is " + this.eT + ". eL is " + this.eL + ". eR is " + this.eR + ". eB is " + this.eB
			+ "\noTB is " + this.oTB + ". oLR is " + this.oLR);
	}

	// ---- ROTATE COUNTER CLOCKWISE
	public void rotate()
	{

		/*

			This for loop is used to rotate the minizones
			in a counter clockwise fashion

		*/

    	for (int i = 0; i < miniZones.length/2; i++) {
			for (int j=i; j<miniZones.length-i-1; j++) {

				int temp=miniZones[i][j];
				miniZones[i][j]=miniZones[j][miniZones.length-i-1];
				miniZones[j][miniZones.length-i-1]=miniZones[miniZones.length-i-1][miniZones.length-j-1];
				miniZones[miniZones.length-i-1][miniZones.length-j-1]=miniZones[miniZones.length-j-1][i];
				miniZones[miniZones.length-j-1][i]=temp;

			}
		}

		/*

			Rest of these values are rotated according
			to the location of the features on the
			minizones

		*/

        boolean bitTemp = cTL;
		int edgeTemp = eT;
		cTL = cTR;
		cTR = cBR;
		cBR = cBL;
		cBL = bitTemp;
		eT = eR;
		eR = eB;
		eB = eL;
		eL = edgeTemp;
		bitTemp = oTB;
		oTB = oLR;
		oLR = bitTemp;
		beenRotated = (beenRotated + 1) % 4;
	}

	public int getRotation() {
		return beenRotated * 90;
	}

	/*

		Function is used for the Feature Areas in the HashBoard.java

		Finds the lakes that are connected in the minizones for
		each tile

	*/

	public ArrayList<HashSet<Integer>> connectedLakes(){

		ArrayList<HashSet<Integer>> lakeSet = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> lset = new HashSet<Integer>();;

		for(int i=0; i < miniZones.length; i++){


			for(int j=0; j < miniZones[i].length; j++){

				//	Getting the Current Position that
				//	can be added to the Set

				int currentPosition = 0;

				if(i == 0 && j == 0){
					currentPosition = 1;
				}

				else if(i == 0 && j == 1){
					currentPosition = 2;
				}

				else if(i == 0 && j == 2){
					currentPosition = 3;
				}

				else if(i == 1 && j == 0){
					currentPosition = 4;
				}

				else if(i == 1 && j == 1){
					currentPosition = 5;
				}

				else if(i == 1 && j == 2){
					currentPosition = 6;
				}

				else if(i == 2 && j == 0){
					currentPosition = 7;
				}

				else if(i == 2 && j == 1){
					currentPosition = 8;
				}

				else if(i == 2 && j == 2){
					currentPosition = 9;
				}

				if(miniZones[i][j] == 2){
					lset.add(currentPosition);

					if((i+1) < (miniZones.length - 1)){
						if(miniZones[i+1][j] == 2){
							if(!lset.contains(miniZones[i+1][j])){
								lset.add(currentPosition + 1);
							}
						}
					}

					else if((i-1) >= 0){
						if(miniZones[i-1][j] == 2){
							if(!lset.contains(miniZones[i-1][j])){
								lset.add(currentPosition - 1);
							}
						}
					}

					else if((j+1) < (miniZones[i].length - 1)){
						if(miniZones[i][j+1] == 2){
							if(!lset.contains(miniZones[i][j+1])){
								lset.add(currentPosition + 3);
							}
						}
					}

					else if((j-1) >= 0){
						if(miniZones[i][j-1] == 2){
							if(!lset.contains(miniZones[i][j-1])){
								lset.add(currentPosition - 3);
							}
						}
					}
				}

			}

			if(!lakeSet.contains(lset) && !lset.isEmpty()){
				lakeSet.add(lset);
			}

		}

		return lakeSet;

	}

	/*

		Function is used for the Feature Areas in the HashBoard.java

		This function solves for the particular case of the minizones
		where the lakes are not connected, which is the "JLLJ-" and "LJLJ-"
		tiles

	*/

	public ArrayList<HashSet<Integer>> specialConnectedLakes(){

		ArrayList<HashSet<Integer>> lakeSet = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> lset;

		for(int i=0; i < miniZones.length; i++){

			lset = new HashSet<Integer>();

			for(int j=0; j < miniZones[i].length; j++){

				//	Getting the Current Position that
				//	can be added to the Set

				int currentPosition = 0;

				if(i == 0 && j == 0){
					currentPosition = 1;
				}

				else if(i == 0 && j == 1){
					currentPosition = 2;
				}

				else if(i == 0 && j == 2){
					currentPosition = 3;
				}

				else if(i == 1 && j == 0){
					currentPosition = 4;
				}

				else if(i == 1 && j == 1){
					currentPosition = 5;
				}

				else if(i == 1 && j == 2){
					currentPosition = 6;
				}

				else if(i == 2 && j == 0){
					currentPosition = 7;
				}

				else if(i == 2 && j == 1){
					currentPosition = 8;
				}

				else if(i == 2 && j == 2){
					currentPosition = 9;
				}

				if(miniZones[i][j] == 2){
					lset.add(currentPosition);

					if((i+1) < (miniZones.length - 1)){
						if(miniZones[i+1][j] == 2){
							if(!lset.contains(miniZones[i+1][j])){
								lset.add(currentPosition + 1);
							}
						}
					}

					else if((i-1) >= 0){
						if(miniZones[i-1][j] == 2){
							if(!lset.contains(miniZones[i-1][j])){
								lset.add(currentPosition - 1);
							}
						}
					}

					else if((j+1) < (miniZones[i].length - 1)){
						if(miniZones[i][j+1] == 2){
							if(!lset.contains(miniZones[i][j+1])){
								lset.add(currentPosition + 3);
							}
						}
					}

					else if((j-1) >= 0){
						if(miniZones[i][j-1] == 2){
							if(!lset.contains(miniZones[i][j-1])){
								lset.add(currentPosition - 3);
							}
						}
					}
				}

				if(!lakeSet.contains(lset) && !lset.isEmpty()){
					lakeSet.add(lset);
				}

			}

		}

		ArrayList<HashSet<Integer>> completeLakeSet = new ArrayList<HashSet<Integer>>();

		for(int i=0; i < lakeSet.size(); i++){
			if(i + 1 < lakeSet.size()){
				HashSet<Integer> set1 = lakeSet.get(i);
				HashSet<Integer> set2 = lakeSet.get(i+1);
				HashSet<Integer> intersection = new HashSet<Integer>(set1);

				intersection.retainAll(set2);

				if(!intersection.isEmpty()){
					set1.addAll(set2);
					completeLakeSet.add(set1);
				} else {
					completeLakeSet.add(set1);
					completeLakeSet.add(set2);
				}

			}

		}

		return completeLakeSet;

	}

	/*

	Function is used for the Feature Areas in the HashBoard.java

	This function solves for the connected roads/trails in the
	minizones

	*/


	public ArrayList<HashSet<Integer>> connectedRoads(){

		ArrayList<HashSet<Integer>> roadSet = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> rset;

		for(int i=0; i < miniZones.length; i++){

			rset = new HashSet<Integer>();

			for(int j=0; j < miniZones[i].length; j++){

				//	Getting the Current Position that
				//	can be added to the Set

				int currentPosition = 0;

				if(i == 0 && j == 0){
					currentPosition = 1;
				}

				else if(i == 0 && j == 1){
					currentPosition = 2;
				}

				else if(i == 0 && j == 2){
					currentPosition = 3;
				}

				else if(i == 1 && j == 0){
					currentPosition = 4;
				}

				else if(i == 1 && j == 1){
					currentPosition = 5;
				}

				else if(i == 1 && j == 2){
					currentPosition = 6;
				}

				else if(i == 2 && j == 0){
					currentPosition = 7;
				}

				else if(i == 2 && j == 1){
					currentPosition = 8;
				}

				else if(i == 2 && j == 2){
					currentPosition = 9;
				}

				if(miniZones[i][j] == 1){
					rset.add(currentPosition);
				}

			}

			if(!roadSet.contains(rset) && !rset.isEmpty()){
				roadSet.add(rset);
			}

		}

		ArrayList<HashSet<Integer>> completeRoadSet = new ArrayList<HashSet<Integer>>();
		HashSet<Integer> union = new HashSet<Integer>();

		for(int i=0; i < roadSet.size(); i++){
				HashSet<Integer> set1 = roadSet.get(i);
				union.addAll(set1);
		}

		completeRoadSet.add(union);

		return completeRoadSet;

	}

	/*	Prints the values that are in a minizone


		This particular function is mostly used for
		testing and checking to see if the functions for
		rotation the feature area and connected
		feature areas are working.

	*/

	public void printMiniZone(){

		HashSet<Integer> lset;

		for(int i=0; i < miniZones.length; i++){
			for(int j=0; j < miniZones[i].length; j++){
				System.out.print(miniZones[i][j] + " ");
			}
			System.out.println();
		}

	}

}
