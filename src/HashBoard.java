/**********************************************************************

	Created By: Group N

	Logic:

		Board is created using a HashMap that will map the values
		of the Positions to the Tile. Two functions that basically
		control the class:

			1.  didAddTile -- which returns a boolean value
				true: a tile was added to the board;
				false: a tile was not added to the board;

			2.  checkOpenSpots -- which will return a Set
				of the possible locations that a tile can be
				placed.

		CheckOpenSpots can be used by the Player in the future
		to allow the user/ai to pick which spot will be available

***********************************************************************/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;

public class HashBoard{
	
    HashMap <Position, Tile> gBoard;
	Set <Position> set;
	ArrayList<JungleArea> Jungle;
	ArrayList<FeatureArea> Trail;
	ArrayList<FeatureArea> Lake;
	ArrayList<Den> Dens;
	ArrayList<JungleArea> ClaimedJungle;
	ArrayList<FeatureArea> ClaimedTrail;
	ArrayList<FeatureArea> ClaimedLake;
	ArrayList<Den> ClaimedDens;


	public HashBoard(){
		gBoard = new HashMap<Position, Tile>();
		set = new HashSet<Position>();
		Jungle = new ArrayList<JungleArea>();
		Trail = new ArrayList<FeatureArea>();
		Lake = new ArrayList<FeatureArea>();
		Dens = new ArrayList<Den>();
		ClaimedJungle = new ArrayList<JungleArea>();
		ClaimedTrail = new ArrayList<FeatureArea>();
		ClaimedLake = new ArrayList<FeatureArea>();
		ClaimedDens = new ArrayList<Den>();
        
        DisplayBoard gameBoard = new DisplayBoard();
        //gameBoard.setTile("TLTJD",0,3,270);

		gBoard.put(new Position(0,0), new Tile());

		FeatureArea initialTrail = new FeatureArea();
		initialTrail.areaCoor.add(new Position(0,0));
		initialTrail.openBoundary.add(new Boundary(new Position(0,0),1));
		initialTrail.openBoundary.add(new Boundary(new Position(0,0),3));

		Trail.add(initialTrail);

		FeatureArea initialLake = new FeatureArea();
		initialLake.areaCoor.add(new Position(0,0));
		initialLake.openBoundary.add(new Boundary(new Position(0,0),2));

		Lake.add(initialLake);

		JungleArea initialJungle1 = new JungleArea();
		initialJungle1.areaCoor.add(new Position(0,0));
		Set<Integer> miniTile1 = new HashSet<Integer>();
		miniTile1.add(1);
		miniTile1.add(4);
		miniTile1.add(7);
		initialJungle1.boundary.add(new BoundaryJungle(new Position(0,0),miniTile1));

		Jungle.add(initialJungle1);

		JungleArea initialJungle2 = new JungleArea();
		initialJungle2.areaCoor.add(new Position(0,0));
		Set<Integer> miniTile2 = new HashSet<Integer>();
		miniTile2.add(3);
		miniTile2.add(9);
		initialJungle2.boundary.add(new BoundaryJungle(new Position(0,0),miniTile2));

		Jungle.add(initialJungle2);

	}
    
    public HashMap<Position, Tile> getMap(){
    
        return gBoard;
    
    }


	public void printKeys(){

		Set<Position> keySet = gBoard.keySet();

		System.out.println("Key Set");

		for(Position pos: keySet){
			System.out.println("Coordinates: " + pos.getXPosition() + " " + pos.getYPosition());
		}
		System.out.println("Open Spaces");
		for(Position pos: set){
			System.out.println("Coordinates:" + pos.getXPosition() + " " + pos.getYPosition());
		}

	}

	public void addTile(Position pos, Tile tile){
		//This is assuming that position and tile, is already been validated!
		//No new tiger or crocodile
		if(gBoard.isEmpty()) 
		{
				gBoard.put(pos, tile);
				System.out.println("Initial tile placed");
				return;
		}
		
		System.out.println("TRYING TO PLACE TILE AT " + pos.getXPosition() + " " + pos.getYPosition());
		if(!checkLegalMove(pos, tile)) 
		{
			System.out.println("INVALID LOCATION");
			return;
		}
		gBoard.put(pos,tile);
		updateOpenSpots(pos);
		updateFeatures(pos,tile);
		System.out.println("TILE PLACED AT " + pos.getXPosition() + " " + pos.getYPosition() + "**************************");
	}

	public void updateFeatures(Position pos, Tile tile){
		Position right = new Position(pos.getXPosition() + 1, pos.getYPosition()); //2
		Position left = new Position(pos.getXPosition() - 1, pos.getYPosition()); //4
		Position top = new Position(pos.getXPosition(), pos.getYPosition() + 1); //1
		Position bottom = new Position(pos.getXPosition(), pos.getYPosition() - 1); //3

		boolean foundR = false;
		boolean foundL = false;
		boolean foundT = false;
		boolean foundB = false;


		FeatureArea RightArea=new FeatureArea();
		FeatureArea LeftArea=new FeatureArea();
		FeatureArea TopArea=new FeatureArea();
		FeatureArea BottomArea=new FeatureArea();

		Boundary checkRight = new Boundary(right,4);
		Boundary checkLeft = new Boundary(left,2);
		Boundary checkTop = new Boundary(top,3);
		Boundary checkBottom = new Boundary(bottom,1);

		//ArrayList<HashSet<Integer>> JungleConnection = tile.connectedJungle();
		boolean outsideConnect = false;


		/**JUNGLES**/
		//FAILED XD

		/**DENS**/
		if(tile.getDen()){
			Den newDen = new Den(pos);
			/**ADD TIGER**/
			if(newDen.getHasTiger()){
				ClaimedDens.add(newDen);
			}
			else{
				Dens.add(newDen);
			}
		}


		/**LAKES AND TRAILS**/

		/**RightArea**/
		if(gBoard.containsKey(right)){
			FeatureArea holder;
			if(tile.getEdgeR()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkRight)){
						RightArea=holder;
						foundR=true;
						break;
					}
				}
				if(!foundR) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext(); ) {
						holder=check.next();
						if (holder.openBoundary.contains(checkRight)) {
							RightArea = holder;
							foundR=true;
							break;
						}
					}
				}

			}
			else if (tile.getEdgeR()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkRight)){
						RightArea=holder;
						foundR=true;
						break;
					}
				}
				if(!foundR) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkRight)) {
							RightArea = holder;
							foundR=true;
							break;
						}
					}
				}
			}
		}
		else{
			RightArea=new FeatureArea();
			if(tile.getEdgeR()==2||tile.getEdgeR()==1){
				RightArea.areaCoor.add(pos);
				RightArea.openBoundary.add(new Boundary(pos,2));
			}
		}
		/**LeftArea**/
		if(gBoard.containsKey(left)){
			FeatureArea holder;
			if(tile.getEdgeL()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkLeft)){
						LeftArea=holder;
						foundL=true;
						break;
					}
				}
				if(!foundL) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkLeft)) {
							LeftArea = holder;
							foundL=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeL()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkLeft)){
						LeftArea=holder;
						foundL=true;
						break;
					}
				}
				if(!foundL) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkLeft)) {
							LeftArea = holder;
							foundL=true;
							break;
						}
					}
				}
			}
		}
		else{
			LeftArea=new FeatureArea();
			if(tile.getEdgeL()==2||tile.getEdgeL()==1) {
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.add(new Boundary(pos, 4));
			}
		}

		/**TopArea**/
		if(gBoard.containsKey(top)){
			FeatureArea holder;
			if(tile.getEdgeT()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkTop)){
						TopArea=holder;
						foundT=true;
						break;
					}
				}
				if(!foundT) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkTop)) {
							TopArea = holder;
							foundT=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeT()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkTop)){
						TopArea=holder;
						foundT=true;
						break;
					}
				}
				if(!foundT) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkTop)) {
							TopArea = holder;
							foundT=true;
							break;
						}
					}
				}
			}
		}
		else{
			TopArea=new FeatureArea();
			if(tile.getEdgeT()==2||tile.getEdgeT()==1){
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.add(new Boundary(pos,1));
			}
		}

		/**BottomArea**/
		if(gBoard.containsKey(bottom)){
			FeatureArea holder;
			if(tile.getEdgeB()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkBottom)){
						BottomArea=holder;
						foundB=true;
						break;
					}
				}
				if(!foundB) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkBottom)) {
							BottomArea = holder;
							foundB=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeB()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkBottom)){
						BottomArea=holder;
						foundB=true;
						break;
					}
				}
				if(!foundB) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkBottom)) {
							BottomArea = holder;
							foundB=true;
							break;
						}
					}
				}
			}
		}
		else{
			BottomArea=new FeatureArea();
			if(tile.getEdgeB()==2||tile.getEdgeB()==1){
				BottomArea.areaCoor.add(pos);
				BottomArea.openBoundary.add(new Boundary(pos,3));
			}
		}

		/**Prevent deletion of what is already deleted**/
		boolean sameR=false;
		boolean sameB=false;
		boolean sameL=false;

		if(TopArea.equals(RightArea)){
			sameR=true;
		}
		if(TopArea.equals(BottomArea)){
			sameB=true;
		}
		if(TopArea.equals(LeftArea)){
			sameL=true;
		}
		if(RightArea.equals(BottomArea)){
			sameB=true;
		}
		if(RightArea.equals(LeftArea)){
			sameL=true;
		}
		if(BottomArea.equals(LeftArea)){
			sameL=true;
		}

		FeatureArea holder;
		boolean found=false;
		if(tile.getEdgeT()==2&&foundT){
			for(Iterator<FeatureArea> check = Lake.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(TopArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedLake.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(TopArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		else if(tile.getEdgeT()==1){
			for(Iterator<FeatureArea> check = Trail.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(TopArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedTrail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(TopArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		found=false;
		if(tile.getEdgeR()==2&&!sameR&&foundR){
			for(Iterator<FeatureArea> check = Lake.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(RightArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedLake.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(RightArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		else if(tile.getEdgeR()==1&&!sameR&&foundR){
			for(Iterator<FeatureArea> check = Trail.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(RightArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedTrail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(RightArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		found=false;
		if(tile.getEdgeB()==2&&!sameB&&foundB){
			for(Iterator<FeatureArea> check = Lake.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(BottomArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedLake.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(BottomArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		else if(tile.getEdgeB()==1&&!sameB&&foundB){
			for(Iterator<FeatureArea> check = Trail.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(BottomArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedTrail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(BottomArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		found=false;
		if(tile.getEdgeL()==2&&!sameL&&foundL){
			for(Iterator<FeatureArea> check = Lake.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(LeftArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedLake.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(LeftArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}
		else if(tile.getEdgeL()==1&&!sameL&&foundL){
			for(Iterator<FeatureArea> check = Trail.iterator(); check.hasNext();){
				holder=check.next();
				if(holder.equals(LeftArea)){
					check.remove();
					found=true;
					break;
				}
			}
			if(!found){
				for(Iterator<FeatureArea> check = ClaimedTrail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.equals(LeftArea)){
						check.remove();
						found=true;
						break;
					}
				}
			}
		}


		boolean skipT=false;
		boolean skipR=false;
		boolean skipB=false;
		boolean skipL=false;

		/**Account if two or more edges of a tile become a part of the same area.**/
		/**TOP**/
		/**Lake**/
		boolean R=false;
		boolean B=false;
		boolean L=false;

		if(!skipT) {
			if (tile.getEdgeT() == 2) {
				if(TopArea.equals(RightArea)){
					skipR=true;
					R=true;
				}
				if(TopArea.equals(BottomArea)){
					skipB=true;
					B=true;
				}
				if(TopArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(R){
					TopArea.openBoundary.remove(checkRight);
				}
				if(B){
					TopArea.openBoundary.remove(checkBottom);
				}
				if(L){
					TopArea.openBoundary.remove(checkLeft);
				}
				if (tile.getCTR()&&!skipR) {
					TopArea.areaCoor.addAll(RightArea.areaCoor);
					TopArea.openBoundary.addAll((RightArea.openBoundary));
					TopArea.openBoundary.remove(checkRight);
					//if(foundR){
					//	checkR.remove();
					//}
					if (RightArea.getHasTiger()) {
						TopArea.tiger.addAll(RightArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(RightArea.getHasCrocodile()){
						TopArea.addCrocodile(RightArea.numOfCrocs);
					}
					if(RightArea.getHasAnimal()){
						TopArea.uniqueAnimal.addAll(RightArea.uniqueAnimal);
						TopArea.setHasAnimal(true);
					}
					skipR = true;
				}
				if (tile.getOTB()&&!skipB) {
					TopArea.areaCoor.addAll(BottomArea.areaCoor);
					TopArea.openBoundary.addAll((BottomArea.openBoundary));
					TopArea.openBoundary.remove(checkBottom);
					//if(foundB){
					//	checkB.remove();
					//}
					if (BottomArea.getHasTiger()) {
						TopArea.tiger.addAll(BottomArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(BottomArea.getHasCrocodile()){
						TopArea.addCrocodile(BottomArea.numOfCrocs);
					}
					if(BottomArea.getHasAnimal()){
						TopArea.uniqueAnimal.addAll(BottomArea.uniqueAnimal);
						TopArea.setHasAnimal(true);
					}
					skipB = true;
				}
				if (tile.getCTL()&&!skipL) {
					TopArea.areaCoor.addAll(LeftArea.areaCoor);
					TopArea.openBoundary.addAll((LeftArea.openBoundary));
					TopArea.openBoundary.remove(checkLeft);
					//if(foundL){
					//	checkL.remove();
					//}
					if (LeftArea.getHasTiger()) {
						TopArea.tiger.addAll(LeftArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						TopArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						TopArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
						TopArea.setHasAnimal(true);
					}
					skipL = true;
				}
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.remove(checkTop);
				if(tile.getCroc()){
					TopArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					TopArea.uniqueAnimal.add(tile.getAnimal());
					TopArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if (TopArea.openBoundary.isEmpty()) {
					TopArea.setCompleted(true);
				}
				//if(foundT){
				//	checkT.remove();
				//}
				if (TopArea.getHasTiger()) {
					ClaimedLake.add(TopArea);
				} else {
					Lake.add(TopArea);
				}
			}
			/**Trail**/
			else if (tile.getEdgeT() == 1) {
				int count = 0;

				if (tile.getEdgeR() == 1&&!skipR) {
					count++;
				}
				if (tile.getEdgeB() == 1&&!skipB) {
					count++;
				}
				if (tile.getEdgeL() == 1&&!skipL) {
					count++;
				}
				//check Intersection
				if (count == 1) {
					if(TopArea.equals(RightArea)){
						TopArea.openBoundary.remove(checkRight);
						skipR=true;
					}
					else if(TopArea.equals(BottomArea)){
						TopArea.openBoundary.remove(checkBottom);
						skipB=true;
					}
					else if(TopArea.equals(LeftArea)){
						TopArea.openBoundary.remove(checkLeft);
						skipL=true;
					}
					if (tile.getEdgeR() == 1&&!skipR) {
						TopArea.areaCoor.addAll(RightArea.areaCoor);
						TopArea.openBoundary.addAll(RightArea.openBoundary);
						TopArea.openBoundary.remove(checkRight);
						if (RightArea.getHasTiger()) {
							TopArea.tiger.addAll(RightArea.tiger);
							TopArea.setHasTiger(true);
						}
						if(RightArea.getHasCrocodile()){
							TopArea.addCrocodile(RightArea.numOfCrocs);
						}
						if(RightArea.getHasAnimal()){
							TopArea.animal.addAll(RightArea.animal);
							TopArea.setHasAnimal(true);
						}
						skipR = true;
					}
					else if (tile.getEdgeB() == 1&&!skipB) {
						TopArea.areaCoor.addAll(BottomArea.areaCoor);
						TopArea.openBoundary.addAll(BottomArea.openBoundary);
						TopArea.openBoundary.remove(checkBottom);
						if (BottomArea.getHasTiger()) {
							TopArea.tiger.addAll(BottomArea.tiger);
							TopArea.setHasTiger(true);
						}
						if(BottomArea.getHasCrocodile()){
							TopArea.addCrocodile(BottomArea.numOfCrocs);
						}
						if(BottomArea.getHasAnimal()){
							TopArea.animal.addAll(BottomArea.animal);
							TopArea.setHasAnimal(true);
						}
						skipB = true;
					}
					else if (tile.getEdgeL() == 1&&!skipL) {
						TopArea.areaCoor.addAll(LeftArea.areaCoor);
						TopArea.openBoundary.addAll(LeftArea.openBoundary);
						TopArea.openBoundary.remove(checkLeft);
						if (LeftArea.getHasTiger()) {
							TopArea.tiger.addAll(LeftArea.tiger);
							TopArea.setHasTiger(true);
						}
						if(LeftArea.getHasCrocodile()){
							TopArea.addCrocodile(LeftArea.numOfCrocs);
						}
						if(LeftArea.getHasAnimal()){
							TopArea.animal.addAll(LeftArea.animal);
							TopArea.setHasAnimal(true);
						}
						skipL = true;
					}
					TopArea.areaCoor.add(pos);
					TopArea.openBoundary.remove(checkTop);
					if(tile.getCroc()){
						TopArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						TopArea.animal.add(tile.getAnimal());
						TopArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (TopArea.openBoundary.isEmpty()) {
						TopArea.setCompleted(true);
					}
					//if(foundT){
					//	checkT.remove();
					//}
					if (TopArea.getHasTiger()) {
						ClaimedTrail.add(TopArea);
					} else {
						Trail.add(TopArea);
					}

				}
				else if (count > 1) {
					if(TopArea.equals(RightArea)){
						skipR=true;
						R=true;
					}
					if(TopArea.equals(BottomArea)){
						skipB=true;
						B=true;
					}
					if(TopArea.equals(LeftArea)){
						skipL=true;
						L=true;
					}
					if(R){
						TopArea.openBoundary.remove(checkRight);
					}
					if(B){
						TopArea.openBoundary.remove(checkBottom);
					}
					if(L){
						TopArea.openBoundary.remove(checkLeft);
					}
					if (tile.getEdgeR() == 1&&!skipR) {
						RightArea.areaCoor.add(pos);
						RightArea.openBoundary.remove(checkRight);
						if(tile.getCroc()){
							RightArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							RightArea.animal.add(tile.getAnimal());
							RightArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (RightArea.openBoundary.isEmpty()) {
							RightArea.setCompleted(true);
						}
						//if(foundR){
						//	checkR.remove();
						//}
						if (RightArea.getHasTiger()) {
							ClaimedTrail.add(RightArea);
						} else {
							Trail.add(RightArea);
						}
							skipR = true;
					}
					if (tile.getEdgeB() == 1&&!skipB) {
						BottomArea.areaCoor.add(pos);
						BottomArea.openBoundary.remove(checkBottom);
						if(tile.getCroc()){
							BottomArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							BottomArea.animal.add(tile.getAnimal());
							BottomArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (BottomArea.openBoundary.isEmpty()) {
							BottomArea.setCompleted(true);
						}
						//if(foundB){
						//	checkB.remove();
						//}
						if (BottomArea.getHasTiger()) {
							ClaimedTrail.add(BottomArea);
						} else {
							Trail.add(BottomArea);
						}
						skipB = true;
					}
					if (tile.getEdgeL() == 1&&!skipL) {
						LeftArea.areaCoor.add(pos);
						LeftArea.openBoundary.remove(checkLeft);
						if(tile.getCroc()){
							LeftArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							LeftArea.animal.add(tile.getAnimal());
							LeftArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (LeftArea.openBoundary.isEmpty()) {
							LeftArea.setCompleted(true);
						}
						//if(foundL){
						//	checkL.remove();
						//}
						if (LeftArea.getHasTiger()) {
							ClaimedTrail.add(LeftArea);
						} else {
							Trail.add(LeftArea);
						}
						skipL = true;
					}
					TopArea.areaCoor.add(pos);
					TopArea.openBoundary.remove(checkTop);
					if(tile.getCroc()){
						TopArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						TopArea.animal.add(tile.getAnimal());
						TopArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (TopArea.openBoundary.isEmpty()) {
						TopArea.setCompleted(true);
					}
					//if(foundT){
					//	checkT.remove();
					//}
					if (TopArea.getHasTiger()) {
						ClaimedTrail.add(TopArea);
					} else {
						Trail.add(TopArea);
					}
				}
				else{
					TopArea.areaCoor.add(pos);
					TopArea.openBoundary.remove(checkTop);
					if(tile.getCroc()){
						TopArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						TopArea.animal.add(tile.getAnimal());
						TopArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (TopArea.openBoundary.isEmpty()) {
						TopArea.setCompleted(true);
					}
					//if(foundT){
					//	checkT.remove();
					//}
					if (TopArea.getHasTiger()) {
						ClaimedTrail.add(TopArea);
					} else {
						Trail.add(TopArea);
					}
				}

			}
		}

		/**RIGHT**/
		B=false;
		L=false;
		if(!skipR){
			/**Lake**/
			if(tile.getEdgeR()==2){
				if(RightArea.equals(BottomArea)){
					skipB=true;
					B=true;
				}
				if(RightArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(B){
					RightArea.openBoundary.remove(checkBottom);
				}
				if(L){
					RightArea.openBoundary.remove(checkLeft);
				}
				if(tile.getCBR()&&!skipB){
					RightArea.areaCoor.addAll(BottomArea.areaCoor);
					RightArea.openBoundary.addAll((BottomArea.openBoundary));
					RightArea.openBoundary.remove(checkBottom);
					//if(foundB){
					//	checkB.remove();
					//}
					if(BottomArea.getHasTiger()){
						RightArea.tiger.addAll(BottomArea.tiger);
						RightArea.setHasTiger(true);
					}
					if(BottomArea.getHasCrocodile()){
						RightArea.addCrocodile(BottomArea.numOfCrocs);
					}
					if(BottomArea.getHasAnimal()){
						RightArea.uniqueAnimal.addAll(BottomArea.uniqueAnimal);
						RightArea.setHasAnimal(true);
					}
					skipB=true;
				}
				if(tile.getOLR()&&!skipL){
					RightArea.areaCoor.addAll(LeftArea.areaCoor);
					RightArea.openBoundary.addAll((LeftArea.openBoundary));
					RightArea.openBoundary.remove(checkLeft);
					//if(foundL){
					//	checkL.remove();
					//}
					if(LeftArea.getHasTiger()){
						RightArea.tiger.addAll(LeftArea.tiger);
						RightArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						RightArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						RightArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
						RightArea.setHasAnimal(true);
					}
					skipL=true;
				}
				RightArea.areaCoor.add(pos);
				RightArea.openBoundary.remove(checkRight);
				if(tile.getCroc()){
					RightArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					RightArea.uniqueAnimal.add(tile.getAnimal());
					RightArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(RightArea.openBoundary.isEmpty()){
					RightArea.setCompleted(true);
				}
				//if(foundR){
				//	checkR.remove();
				//}
				if(RightArea.getHasTiger()){
					ClaimedLake.add(RightArea);
				}
				else{
					Lake.add(RightArea);
				}
			}
			/**Trail**/
			else if(tile.getEdgeR()==1){
				int count = 0;
				if(tile.getEdgeB()==1&&!skipB){
					count++;
				}
				if(tile.getEdgeL()==1&&!skipL){
					count++;
				}
				//check Intersection
				if(count==1){
					if(RightArea.equals(BottomArea)){
						RightArea.openBoundary.remove(checkBottom);
						skipB=true;
					}
					else if(RightArea.equals(LeftArea)){
						RightArea.openBoundary.remove(checkLeft);
						skipL=true;
					}
					if(tile.getEdgeB()==1&&!skipB){
						RightArea.areaCoor.addAll(BottomArea.areaCoor);
						RightArea.openBoundary.addAll(BottomArea.openBoundary);
						RightArea.openBoundary.remove(checkBottom);
						if(BottomArea.getHasTiger()){
							RightArea.tiger.addAll(BottomArea.tiger);
							RightArea.setHasTiger(true);
						}
						if(BottomArea.getHasCrocodile()){
							RightArea.addCrocodile(BottomArea.numOfCrocs);
						}
						if(BottomArea.getHasAnimal()){
							RightArea.animal.addAll(BottomArea.animal);
							RightArea.setHasAnimal(true);
						}
						skipB=true;
					}
					else if(tile.getEdgeL()==1&&!skipL){
						RightArea.areaCoor.addAll(LeftArea.areaCoor);
						RightArea.openBoundary.addAll(LeftArea.openBoundary);
						RightArea.openBoundary.remove(checkLeft);
						if(LeftArea.getHasTiger()){
							RightArea.tiger.addAll(LeftArea.tiger);
							RightArea.setHasTiger(true);
						}
						if(LeftArea.getHasCrocodile()){
							RightArea.addCrocodile(LeftArea.numOfCrocs);
						}
						if(LeftArea.getHasAnimal()){
							RightArea.animal.addAll(LeftArea.animal);
							RightArea.setHasAnimal(true);
						}
						skipL=true;
					}
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}
					//if(foundR){
					//	checkR.remove();
					//}
					if(RightArea.getHasTiger()){
						ClaimedTrail.add(RightArea);
					}
					else{
						Trail.add(RightArea);
					}

				}
				else if(count>1) {
					if(RightArea.equals(BottomArea)){
						skipB=true;
						B=true;
					}
					if(RightArea.equals(LeftArea)){
						skipL=true;
						L=true;
					}
					if(B){
						RightArea.openBoundary.remove(checkBottom);
					}
					if(L){
						RightArea.openBoundary.remove(checkLeft);
					}
					if (tile.getEdgeB() == 1&&!skipB) {
						BottomArea.areaCoor.add(pos);
						BottomArea.openBoundary.remove(checkBottom);
						if(tile.getCroc()){
							BottomArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							BottomArea.animal.add(tile.getAnimal());
							BottomArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (BottomArea.openBoundary.isEmpty()) {
							BottomArea.setCompleted(true);
						}
						//if(foundB){
						//	checkB.remove();
						//}
						if (BottomArea.getHasTiger()) {
							ClaimedTrail.add(BottomArea);
						} else {
							Trail.add(BottomArea);
						}
						skipB = true;
					}
					if (tile.getEdgeL() == 1&&!skipL) {
						LeftArea.areaCoor.add(pos);
						LeftArea.openBoundary.remove(checkLeft);
						if(tile.getCroc()){
							LeftArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							LeftArea.animal.add(tile.getAnimal());
							LeftArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (LeftArea.openBoundary.isEmpty()) {
							LeftArea.setCompleted(true);
						}
						//if(foundL){
						//	checkL.remove();
						//}
						if (LeftArea.getHasTiger()) {
							ClaimedTrail.add(LeftArea);
						} else {
							Trail.add(LeftArea);
						}
						skipL = true;
					}
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}
					//if(foundR){
					//	checkR.remove();
					//}
					if(RightArea.getHasTiger()){
						ClaimedTrail.add(RightArea);
					}
					else{
						Trail.add(RightArea);
					}
				}
				else{
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}
					//if(foundR){
					//	checkR.remove();
					//}
					if(RightArea.getHasTiger()){
						ClaimedTrail.add(RightArea);
					}
					else{
						Trail.add(RightArea);
					}
				}


			}
		}

		/**BOTTOM**/
		L=false;
		if(!skipB){
			/**Lake**/
			if(tile.getEdgeB()==2){
				if(BottomArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(L){
					BottomArea.openBoundary.remove(checkLeft);
				}
				if(tile.getCBL()&&!skipL){
					BottomArea.areaCoor.addAll(LeftArea.areaCoor);
					BottomArea.openBoundary.addAll((LeftArea.openBoundary));
					BottomArea.openBoundary.remove(checkLeft);
					//if(foundL){
					//	checkL.remove();
					//}
					if(LeftArea.getHasTiger()){
						BottomArea.tiger.addAll(LeftArea.tiger);
						BottomArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						BottomArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						BottomArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
						BottomArea.setHasAnimal(true);
					}
					skipL=true;
				}
				BottomArea.areaCoor.add(pos);
				BottomArea.openBoundary.remove(checkBottom);
				if(tile.getCroc()){
					BottomArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					BottomArea.animal.add(tile.getAnimal());
					BottomArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(BottomArea.openBoundary.isEmpty()){
					BottomArea.setCompleted(true);
				}
				//if(foundB){
				//	checkB.remove();
				//}
				if(BottomArea.getHasTiger()){
					ClaimedLake.add(BottomArea);
				}
				else{
					Lake.add(BottomArea);
				}
			}
			/**Trail**/
			else if(tile.getEdgeB()==1){
				if(tile.getEdgeL()==1&&!skipL){
					if(BottomArea.equals(LeftArea)){
						skipL=true;
						L=true;
					}
					if(L){
						BottomArea.openBoundary.remove(checkLeft);
					}
					else{
						BottomArea.areaCoor.addAll(LeftArea.areaCoor);
						BottomArea.openBoundary.addAll(LeftArea.openBoundary);
						if(LeftArea.getHasCrocodile()){
							BottomArea.addCrocodile(LeftArea.numOfCrocs);
						}
						if(LeftArea.getHasAnimal()){
							BottomArea.animal.addAll(LeftArea.animal);
							BottomArea.setHasAnimal(true);
						}
						BottomArea.openBoundary.remove(checkLeft);
						if(LeftArea.getHasTiger()){
							BottomArea.tiger.addAll(LeftArea.tiger);
							BottomArea.setHasTiger(true);
						}
					skipL=true;
					}

					BottomArea.areaCoor.add(pos);
					BottomArea.openBoundary.remove(checkBottom);
					if(tile.getCroc()){
						BottomArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						BottomArea.animal.add(tile.getAnimal());
						BottomArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(BottomArea.openBoundary.isEmpty()){
						BottomArea.setCompleted(true);
					}
					//if(foundB){
					//	checkB.remove();
					//}
					if(BottomArea.getHasTiger()){
						ClaimedTrail.add(BottomArea);
					}
					else{
						Trail.add(BottomArea);
					}
				}
				else{
					BottomArea.areaCoor.add(pos);
					BottomArea.openBoundary.remove(checkBottom);
					if(tile.getCroc()){
						BottomArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						BottomArea.animal.add(tile.getAnimal());
						BottomArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(BottomArea.openBoundary.isEmpty()){
						BottomArea.setCompleted(true);
					}
					//if(foundB){
					//	checkB.remove();
					//}
					if(BottomArea.getHasTiger()){
						ClaimedTrail.add(BottomArea);
					}
					else{
						Trail.add(BottomArea);
					}
				}
			}
		}

		/**LEFT**/
		L=false;
		if(!skipL){
			/**Lake**/
			if(tile.getEdgeL()==2){
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.remove(checkLeft);
				if(tile.getCroc()){
					LeftArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					LeftArea.animal.add(tile.getAnimal());
					LeftArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(LeftArea.openBoundary.isEmpty()){
					LeftArea.setCompleted(true);
				}
				//if(foundL){
				//	checkL.remove();
				//}
				if(LeftArea.getHasTiger()){
					ClaimedLake.add(LeftArea);
				}
				else{
					Lake.add(LeftArea);
				}
			}
			/**Trail**/
			else if(tile.getEdgeL()==1){
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.remove(checkLeft);
				if(tile.getCroc()){
					LeftArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					LeftArea.animal.add(tile.getAnimal());
					LeftArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(LeftArea.openBoundary.isEmpty()){
					LeftArea.setCompleted(true);
				}
				//if(foundL){
				//	checkL.remove();
				//}
				if(LeftArea.getHasTiger()){
					ClaimedTrail.add(LeftArea);
				}
				else {
					Trail.add(LeftArea);
				}
			}
		}
	}

	public void AddTile(Position pos, Tile tile, Tiger tiger){

	}

	public void updateFeatures(Position pos, Tile tile, Tiger tiger){

	}

	public void updateOpenSpots(Position newpos){
		Position rPosX = new Position(newpos.getXPosition() + 1, newpos.getYPosition());
		Position lPosX = new Position(newpos.getXPosition() - 1, newpos.getYPosition());
		Position tPosY = new Position(newpos.getXPosition(), newpos.getYPosition() + 1);
		Position bPosY = new Position(newpos.getXPosition(), newpos.getYPosition() - 1);

		//Right
		//boolean exists=false;
		if(!gBoard.containsKey(rPosX)){
			set.add(rPosX);
		}
		if(!gBoard.containsKey(lPosX)){
			set.add(lPosX);
		}
		if(!gBoard.containsKey(tPosY)){
			set.add(tPosY);
		}
		if(!gBoard.containsKey(bPosY)){
			set.add(bPosY);
		}


		set.remove(newpos);

	}
	
	
	
	public boolean checkLegalMove(Position newpos, Tile currentTile)
	{
		
		boolean goodToGo = false;
		
		if (!set.contains(newpos)) return false;		// Make sure the space is open
		//System.out.println("MADE IT PAST OPEN CHECK");
	  
	   // For each adjacency (to the open newpos) make sure at least one of the following conditions is true
	   // 1. exists in open set
	   // 2. does not exist in either open or taken set
	   // 3. exists in taken set and has edge matching (BUT BC OF ACCESS WE NEED TO ASSURE OTHER TWO FIRST)
	   
		Position rPos = new Position(newpos.getXPosition() + 1, newpos.getYPosition());
	 	Position lPos = new Position(newpos.getXPosition() - 1, newpos.getYPosition());
	 	Position tPos = new Position(newpos.getXPosition(), newpos.getYPosition() + 1);
	 	Position bPos = new Position(newpos.getXPosition(), newpos.getYPosition() - 1);
	 
	 	// If the spot exists in the open set or does not exist in either set (2 tiles away)
	 	if ( (set.contains(tPos)) || ((!set.contains(tPos)) && (!gBoard.containsKey(tPos)))) goodToGo = true;	
	   
	 	if (!goodToGo){ 
	        if (gBoard.get(tPos).getEdgeB() != currentTile.getEdgeT()) return false;	// If all three conditions fail
	 	}
	 	//System.out.println("TPOS CHECKED OUT");
	        // REPEAT FOR OTHER THREE SURROUNDING
	        
	   goodToGo = false;
	 	if ( (set.contains(rPos)) || ((!set.contains(rPos)) && (!gBoard.containsKey(rPos)))) goodToGo = true;	
	   
	   if (!goodToGo) {
	        if (gBoard.get(rPos).getEdgeL() != currentTile.getEdgeR()) return false;
	   }
	   //System.out.println("RPOS CHECKED OUT");
	   goodToGo = false;
	   if ( (set.contains(bPos)) || ((!set.contains(bPos)) && (!gBoard.containsKey(bPos)))) goodToGo = true;	
	   
	   if (!goodToGo) {
	        if (gBoard.get(bPos).getEdgeT() != currentTile.getEdgeB()) return false;
	   }
	   //System.out.println("BPOS CHECKED OUT");
	   goodToGo = false;
	 	if ( (set.contains(lPos)) || ((!set.contains(lPos)) && (!gBoard.containsKey(lPos)))) goodToGo = true;	
	   
	   if (!goodToGo) 
	        if (gBoard.get(lPos).getEdgeR() != currentTile.getEdgeL()) return false;
	   //System.out.println("LPOS CHECKED OUT");
	   // SHOULD BE VALID... I THINK
			
			
			
	
		return true;
	}
	
	
	public Move FindBestMove(Tile t){
		
		int bestScore = -1;
		int currScore = -1;
		
		//Initialize a Move struct to send to server
		Move bestMove = null;
		
		//For all four rotations
		for (int i = 0; i < 4; i++)
		{
			//Go thru all open spaces and if there is a valid move, find out what score it would get. Compare to best and save best move
			for(Position pos: set)
			{
				if(checkLegalMove(pos, t))
				{
					currScore = getMoveScore(pos, t); // need to update with scoring method
					if(currScore > bestScore)
					{
						bestScore = currScore;
						bestMove = new Move();

					}
				}
			}

			t.rotate();
		}

		// case: tile is not valid on current board
		if(bestMove == null)
		{
			// tile is not placeable on board, so pass
			//Player.passOnTile(t);
			System.out.println("Hello World");
		}

		return bestMove;
	}

	
	public int getMoveScore(Position pos, Tile tile)
	{
		Position right = new Position(pos.getXPosition() + 1, pos.getYPosition()); //2
		Position left = new Position(pos.getXPosition() - 1, pos.getYPosition()); //4
		Position top = new Position(pos.getXPosition(), pos.getYPosition() + 1); //1
		Position bottom = new Position(pos.getXPosition(), pos.getYPosition() - 1); //3

		boolean foundR = false;
		boolean foundL = false;
		boolean foundT = false;
		boolean foundB = false;


		FeatureArea RightArea=new FeatureArea();
		FeatureArea LeftArea=new FeatureArea();
		FeatureArea TopArea=new FeatureArea();
		FeatureArea BottomArea=new FeatureArea();

		Boundary checkRight = new Boundary(right,4);
		Boundary checkLeft = new Boundary(left,2);
		Boundary checkTop = new Boundary(top,3);
		Boundary checkBottom = new Boundary(bottom,1);

		/**RightArea**/
		if(gBoard.containsKey(right)){
			FeatureArea holder;
			if(tile.getEdgeR()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkRight)){
						RightArea=holder;
						foundR=true;
						break;
					}
				}
				if(!foundR) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext(); ) {
						holder=check.next();
						if (holder.openBoundary.contains(checkRight)) {
							RightArea = holder;
							foundR=true;
							break;
						}
					}
				}

			}
			else if (tile.getEdgeR()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkRight)){
						RightArea=holder;
						foundR=true;
						break;
					}
				}
				if(!foundR) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkRight)) {
							RightArea = holder;
							foundR=true;
							break;
						}
					}
				}
			}
		}
		else{
			RightArea=new FeatureArea();
			if(tile.getEdgeR()==2||tile.getEdgeR()==1){
				RightArea.areaCoor.add(pos);
				RightArea.openBoundary.add(new Boundary(pos,2));
			}
		}
		/**LeftArea**/
		if(gBoard.containsKey(left)){
			FeatureArea holder;
			if(tile.getEdgeL()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkLeft)){
						LeftArea=holder;
						foundL=true;
						break;
					}
				}
				if(!foundL) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkLeft)) {
							LeftArea = holder;
							foundL=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeL()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkLeft)){
						LeftArea=holder;
						foundL=true;
						break;
					}
				}
				if(!foundL) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkLeft)) {
							LeftArea = holder;
							foundL=true;
							break;
						}
					}
				}
			}
		}
		else{
			LeftArea=new FeatureArea();
			if(tile.getEdgeL()==2||tile.getEdgeL()==1) {
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.add(new Boundary(pos, 4));
			}
		}

		/**TopArea**/
		if(gBoard.containsKey(top)){
			FeatureArea holder;
			if(tile.getEdgeT()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkTop)){
						TopArea=holder;
						foundT=true;
						break;
					}
				}
				if(!foundT) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkTop)) {
							TopArea = holder;
							foundT=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeT()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkTop)){
						TopArea=holder;
						foundT=true;
						break;
					}
				}
				if(!foundT) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkTop)) {
							TopArea = holder;
							foundT=true;
							break;
						}
					}
				}
			}
		}
		else{
			TopArea=new FeatureArea();
			if(tile.getEdgeT()==2||tile.getEdgeT()==1){
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.add(new Boundary(pos,1));
			}
		}

		/**BottomArea**/
		if(gBoard.containsKey(bottom)){
			FeatureArea holder;
			if(tile.getEdgeB()==2){
				for(Iterator<FeatureArea> check=Lake.iterator(); check.hasNext(); ){
					holder=check.next();
					if(holder.openBoundary.contains(checkBottom)){
						BottomArea=holder;
						foundB=true;
						break;
					}
				}
				if(!foundB) {
					for (Iterator<FeatureArea> check=ClaimedLake.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkBottom)) {
							BottomArea = holder;
							foundB=true;
							break;
						}
					}
				}
			}
			else if (tile.getEdgeB()==1){
				for(Iterator<FeatureArea> check=Trail.iterator(); check.hasNext();){
					holder=check.next();
					if(holder.openBoundary.contains(checkBottom)){
						BottomArea=holder;
						foundB=true;
						break;
					}
				}
				if(!foundB) {
					for (Iterator<FeatureArea> check=ClaimedTrail.iterator(); check.hasNext();) {
						holder=check.next();
						if (holder.openBoundary.contains(checkBottom)) {
							BottomArea = holder;
							foundB=true;
							break;
						}
					}
				}
			}
		}
		else{
			BottomArea=new FeatureArea();
			if(tile.getEdgeB()==2||tile.getEdgeB()==1){
				BottomArea.areaCoor.add(pos);
				BottomArea.openBoundary.add(new Boundary(pos,3));
			}
		}

		boolean skipR = false;
		boolean skipB = false;
		boolean skipL = false;

		boolean R=false;
		boolean B=false;
		boolean L=false;

		if (tile.getEdgeT() == 2) {
			if (TopArea.equals(RightArea)) {
				skipR = true;
				R = true;
			}
			if (TopArea.equals(BottomArea)) {
				skipB = true;
				B = true;
			}
			if (TopArea.equals(LeftArea)) {
				skipL = true;
				L = true;
			}
			if (R) {
				TopArea.openBoundary.remove(checkRight);
			}
			if (B) {
				TopArea.openBoundary.remove(checkBottom);
			}
			if (L) {
				TopArea.openBoundary.remove(checkLeft);
			}
			if (tile.getCTR() && !skipR) {
				TopArea.areaCoor.addAll(RightArea.areaCoor);
				TopArea.openBoundary.addAll((RightArea.openBoundary));
				TopArea.openBoundary.remove(checkRight);
				if (RightArea.getHasTiger()) {
					TopArea.tiger.addAll(RightArea.tiger);
					TopArea.setHasTiger(true);
				}
				if (RightArea.getHasCrocodile()) {
					TopArea.addCrocodile(RightArea.numOfCrocs);
				}
				if (RightArea.getHasAnimal()) {
					TopArea.uniqueAnimal.addAll(RightArea.uniqueAnimal);
					TopArea.setHasAnimal(true);
				}
				RightArea.areaCoor.clear();
				skipR = true;
			}
			if (tile.getOTB() && !skipB) {
				TopArea.areaCoor.addAll(BottomArea.areaCoor);
				TopArea.openBoundary.addAll((BottomArea.openBoundary));
				TopArea.openBoundary.remove(checkBottom);
				if (BottomArea.getHasTiger()) {
					TopArea.tiger.addAll(BottomArea.tiger);
					TopArea.setHasTiger(true);
				}
				if (BottomArea.getHasCrocodile()) {
					TopArea.addCrocodile(BottomArea.numOfCrocs);
				}
				if (BottomArea.getHasAnimal()) {
					TopArea.uniqueAnimal.addAll(BottomArea.uniqueAnimal);
					TopArea.setHasAnimal(true);
				}
				BottomArea.areaCoor.clear();
				skipB = true;
			}
			if (tile.getCTL() && !skipL) {
				TopArea.areaCoor.addAll(LeftArea.areaCoor);
				TopArea.openBoundary.addAll((LeftArea.openBoundary));
				TopArea.openBoundary.remove(checkLeft);
				if (LeftArea.getHasTiger()) {
					TopArea.tiger.addAll(LeftArea.tiger);
					TopArea.setHasTiger(true);
				}
				if (LeftArea.getHasCrocodile()) {
					TopArea.addCrocodile(LeftArea.numOfCrocs);
				}
				if (LeftArea.getHasAnimal()) {
					TopArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
					TopArea.setHasAnimal(true);
				}
				LeftArea.areaCoor.clear();
				skipL = true;
			}
			TopArea.areaCoor.add(pos);
			TopArea.openBoundary.remove(checkTop);
			if (tile.getCroc()) {
				TopArea.addCrocodile(1);
			}
			if (tile.getAnimal() != -1) {
				TopArea.uniqueAnimal.add(tile.getAnimal());
				TopArea.setHasAnimal(true);
			}
			/**ADD TIGER**/
			if (TopArea.openBoundary.isEmpty()) {
				TopArea.setCompleted(true);
			}
		}
		/**Trail**/
		else if (tile.getEdgeT() == 1) {
			int count = 0;
			if (tile.getEdgeR() == 1&&!skipR) {
				count++;
			}
			if (tile.getEdgeB() == 1&&!skipB) {
				count++;
			}
			if (tile.getEdgeL() == 1&&!skipL) {
				count++;
			}
			//check Intersection
			if (count == 1) {
				if(TopArea.equals(RightArea)){
					TopArea.openBoundary.remove(checkRight);
					skipR=true;
				}
				else if(TopArea.equals(BottomArea)){
					TopArea.openBoundary.remove(checkBottom);
					skipB=true;
				}
				else if(TopArea.equals(LeftArea)){
					TopArea.openBoundary.remove(checkLeft);
					skipL=true;
				}
				if (tile.getEdgeR() == 1&&!skipR) {
					TopArea.areaCoor.addAll(RightArea.areaCoor);
					TopArea.openBoundary.addAll(RightArea.openBoundary);
					TopArea.openBoundary.remove(checkRight);
					if (RightArea.getHasTiger()) {
						TopArea.tiger.addAll(RightArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(RightArea.getHasCrocodile()){
						TopArea.addCrocodile(RightArea.numOfCrocs);
					}
					if(RightArea.getHasAnimal()){
						TopArea.animal.addAll(RightArea.animal);
						TopArea.setHasAnimal(true);
					}
					RightArea.areaCoor.clear();
					skipR = true;
				}
				else if (tile.getEdgeB() == 1&&!skipB) {
					TopArea.areaCoor.addAll(BottomArea.areaCoor);
					TopArea.openBoundary.addAll(BottomArea.openBoundary);
					TopArea.openBoundary.remove(checkBottom);
					if (BottomArea.getHasTiger()) {
						TopArea.tiger.addAll(BottomArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(BottomArea.getHasCrocodile()){
						TopArea.addCrocodile(BottomArea.numOfCrocs);
					}
					if(BottomArea.getHasAnimal()){
						TopArea.animal.addAll(BottomArea.animal);
						TopArea.setHasAnimal(true);
					}
					BottomArea.areaCoor.clear();
					skipB = true;
				}
				else if (tile.getEdgeL() == 1&&!skipL) {
					TopArea.areaCoor.addAll(LeftArea.areaCoor);
					TopArea.openBoundary.addAll(LeftArea.openBoundary);
					TopArea.openBoundary.remove(checkLeft);
					if (LeftArea.getHasTiger()) {
						TopArea.tiger.addAll(LeftArea.tiger);
						TopArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						TopArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						TopArea.animal.addAll(LeftArea.animal);
						TopArea.setHasAnimal(true);
					}
					LeftArea.areaCoor.clear();
					skipL = true;
				}
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.remove(checkTop);
				if(tile.getCroc()){
					TopArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					TopArea.animal.add(tile.getAnimal());
					TopArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if (TopArea.openBoundary.isEmpty()) {
					TopArea.setCompleted(true);
				}
			}
			else if (count > 1) {
				if(TopArea.equals(RightArea)){
					skipR=true;
					R=true;
				}
				if(TopArea.equals(BottomArea)){
					skipB=true;
					B=true;
				}
				if(TopArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(R){
					TopArea.openBoundary.remove(checkRight);
				}
				if(B){
					TopArea.openBoundary.remove(checkBottom);
				}
				if(L){
					TopArea.openBoundary.remove(checkLeft);
				}
				if (tile.getEdgeR() == 1&&!skipR) {
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (RightArea.openBoundary.isEmpty()) {
						RightArea.setCompleted(true);
					}
					skipR = true;
				}
				if (tile.getEdgeB() == 1&&!skipB) {
					BottomArea.areaCoor.add(pos);
					BottomArea.openBoundary.remove(checkBottom);
					if(tile.getCroc()){
						BottomArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						BottomArea.animal.add(tile.getAnimal());
						BottomArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (BottomArea.openBoundary.isEmpty()) {
						BottomArea.setCompleted(true);
					}
					skipB = true;
				}
				if (tile.getEdgeL() == 1&&!skipL) {
					LeftArea.areaCoor.add(pos);
					LeftArea.openBoundary.remove(checkLeft);
					if(tile.getCroc()){
						LeftArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						LeftArea.animal.add(tile.getAnimal());
						LeftArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if (LeftArea.openBoundary.isEmpty()) {
						LeftArea.setCompleted(true);
					}
					skipL = true;
				}
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.remove(checkTop);
				if(tile.getCroc()){
					TopArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					TopArea.animal.add(tile.getAnimal());
					TopArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if (TopArea.openBoundary.isEmpty()) {
					TopArea.setCompleted(true);
				}
			}
			else{
				TopArea.areaCoor.add(pos);
				TopArea.openBoundary.remove(checkTop);
				if(tile.getCroc()){
					TopArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					TopArea.animal.add(tile.getAnimal());
					TopArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if (TopArea.openBoundary.isEmpty()) {
					TopArea.setCompleted(true);
				}
			}
		}


		/**RIGHT**/
		B=false;
		L=false;
		if(!skipR){
			/**Lake**/
			if(tile.getEdgeR()==2){
				if(RightArea.equals(BottomArea)){
					skipB=true;
					B=true;
				}
				if(RightArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(B){
					RightArea.openBoundary.remove(checkBottom);
				}
				if(L){
					RightArea.openBoundary.remove(checkLeft);
				}
				if(tile.getCBR()&&!skipB){
					RightArea.areaCoor.addAll(BottomArea.areaCoor);
					RightArea.openBoundary.addAll((BottomArea.openBoundary));
					RightArea.openBoundary.remove(checkBottom);
					if(BottomArea.getHasTiger()){
						RightArea.tiger.addAll(BottomArea.tiger);
						RightArea.setHasTiger(true);
					}
					if(BottomArea.getHasCrocodile()){
						RightArea.addCrocodile(BottomArea.numOfCrocs);
					}
					if(BottomArea.getHasAnimal()){
						RightArea.uniqueAnimal.addAll(BottomArea.uniqueAnimal);
						RightArea.setHasAnimal(true);
					}
					BottomArea.areaCoor.clear();
					skipB=true;
				}
				if(tile.getOLR()&&!skipL){
					RightArea.areaCoor.addAll(LeftArea.areaCoor);
					RightArea.openBoundary.addAll((LeftArea.openBoundary));
					RightArea.openBoundary.remove(checkLeft);
					if(LeftArea.getHasTiger()){
						RightArea.tiger.addAll(LeftArea.tiger);
						RightArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						RightArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						RightArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
						RightArea.setHasAnimal(true);
					}
					LeftArea.areaCoor.clear();
					skipL=true;
				}
				RightArea.areaCoor.add(pos);
				RightArea.openBoundary.remove(checkRight);
				if(tile.getCroc()){
					RightArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					RightArea.uniqueAnimal.add(tile.getAnimal());
					RightArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(RightArea.openBoundary.isEmpty()){
					RightArea.setCompleted(true);
				}
			}
			/**Trail**/
			else if(tile.getEdgeR()==1){
				int count = 0;
				if(tile.getEdgeB()==1&&!skipB){
					count++;
				}
				if(tile.getEdgeL()==1&&!skipL){
					count++;
				}
				//check Intersection
				if(count==1){
					if(RightArea.equals(BottomArea)){
						RightArea.openBoundary.remove(checkBottom);
						skipB=true;
					}
					else if(RightArea.equals(LeftArea)){
						RightArea.openBoundary.remove(checkLeft);
						skipL=true;
					}
					if(tile.getEdgeB()==1&&!skipB){
						RightArea.areaCoor.addAll(BottomArea.areaCoor);
						RightArea.openBoundary.addAll(BottomArea.openBoundary);
						RightArea.openBoundary.remove(checkBottom);
						if(BottomArea.getHasTiger()){
							RightArea.tiger.addAll(BottomArea.tiger);
							RightArea.setHasTiger(true);
						}
						if(BottomArea.getHasCrocodile()){
							RightArea.addCrocodile(BottomArea.numOfCrocs);
						}
						if(BottomArea.getHasAnimal()){
							RightArea.animal.addAll(BottomArea.animal);
							RightArea.setHasAnimal(true);
						}
						BottomArea.areaCoor.clear();
						skipB=true;
					}
					else if(tile.getEdgeL()==1&&!skipL){
						RightArea.areaCoor.addAll(LeftArea.areaCoor);
						RightArea.openBoundary.addAll(LeftArea.openBoundary);
						RightArea.openBoundary.remove(checkLeft);
						if(LeftArea.getHasTiger()){
							RightArea.tiger.addAll(LeftArea.tiger);
							RightArea.setHasTiger(true);
						}
						if(LeftArea.getHasCrocodile()){
							RightArea.addCrocodile(LeftArea.numOfCrocs);
						}
						if(LeftArea.getHasAnimal()){
							RightArea.animal.addAll(LeftArea.animal);
							RightArea.setHasAnimal(true);
						}
						LeftArea.areaCoor.clear();
						skipL=true;
					}
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}

				}
				else if(count>1) {
					if(RightArea.equals(BottomArea)){
						skipB=true;
						B=true;
					}
					if(RightArea.equals(LeftArea)){
						skipL=true;
						L=true;
					}
					if(B){
						RightArea.openBoundary.remove(checkBottom);
					}
					if(L){
						RightArea.openBoundary.remove(checkLeft);
					}
					if (tile.getEdgeB() == 1&&!skipB) {
						BottomArea.areaCoor.add(pos);
						BottomArea.openBoundary.remove(checkBottom);
						if(tile.getCroc()){
							BottomArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							BottomArea.animal.add(tile.getAnimal());
							BottomArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (BottomArea.openBoundary.isEmpty()) {
							BottomArea.setCompleted(true);
						}
						skipB = true;
					}
					if (tile.getEdgeL() == 1&&!skipL) {
						LeftArea.areaCoor.add(pos);
						LeftArea.openBoundary.remove(checkLeft);
						if(tile.getCroc()){
							LeftArea.addCrocodile(1);
						}
						if(tile.getAnimal()!=-1){
							LeftArea.animal.add(tile.getAnimal());
							LeftArea.setHasAnimal(true);
						}
						/**ADD TIGER**/
						if (LeftArea.openBoundary.isEmpty()) {
							LeftArea.setCompleted(true);
						}
						skipL = true;
					}
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}
				}
				else{
					RightArea.areaCoor.add(pos);
					RightArea.openBoundary.remove(checkRight);
					if(tile.getCroc()){
						RightArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						RightArea.animal.add(tile.getAnimal());
						RightArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(RightArea.openBoundary.isEmpty()){
						RightArea.setCompleted(true);
					}
				}


			}
		}

		/**BOTTOM**/
		L=false;
		if(!skipB){
			/**Lake**/
			if(tile.getEdgeB()==2){
				if(BottomArea.equals(LeftArea)){
					skipL=true;
					L=true;
				}
				if(L){
					BottomArea.openBoundary.remove(checkLeft);
				}
				if(tile.getCBL()&&!skipL){
					BottomArea.areaCoor.addAll(LeftArea.areaCoor);
					BottomArea.openBoundary.addAll((LeftArea.openBoundary));
					BottomArea.openBoundary.remove(checkLeft);
					if(LeftArea.getHasTiger()){
						BottomArea.tiger.addAll(LeftArea.tiger);
						BottomArea.setHasTiger(true);
					}
					if(LeftArea.getHasCrocodile()){
						BottomArea.addCrocodile(LeftArea.numOfCrocs);
					}
					if(LeftArea.getHasAnimal()){
						BottomArea.uniqueAnimal.addAll(LeftArea.uniqueAnimal);
						BottomArea.setHasAnimal(true);
					}
					LeftArea.areaCoor.clear();
					skipL=true;
				}
				BottomArea.areaCoor.add(pos);
				BottomArea.openBoundary.remove(checkBottom);
				if(tile.getCroc()){
					BottomArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					BottomArea.animal.add(tile.getAnimal());
					BottomArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(BottomArea.openBoundary.isEmpty()){
					BottomArea.setCompleted(true);
				}
			}
			/**Trail**/
			else if(tile.getEdgeB()==1){
				if(tile.getEdgeL()==1&&!skipL){
					if(BottomArea.equals(LeftArea)){
						skipL=true;
						L=true;
					}
					if(L){
						BottomArea.openBoundary.remove(checkLeft);
					}
					else{
						BottomArea.areaCoor.addAll(LeftArea.areaCoor);
						BottomArea.openBoundary.addAll(LeftArea.openBoundary);
						if(LeftArea.getHasCrocodile()){
							BottomArea.addCrocodile(LeftArea.numOfCrocs);
						}
						if(LeftArea.getHasAnimal()){
							BottomArea.animal.addAll(LeftArea.animal);
							BottomArea.setHasAnimal(true);
						}
						BottomArea.openBoundary.remove(checkLeft);
						if(LeftArea.getHasTiger()){
							BottomArea.tiger.addAll(LeftArea.tiger);
							BottomArea.setHasTiger(true);
						}
						LeftArea.areaCoor.clear();
						skipL=true;
					}

					BottomArea.areaCoor.add(pos);
					BottomArea.openBoundary.remove(checkBottom);
					if(tile.getCroc()){
						BottomArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						BottomArea.animal.add(tile.getAnimal());
						BottomArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(BottomArea.openBoundary.isEmpty()){
						BottomArea.setCompleted(true);
					}
				}
				else{
					BottomArea.areaCoor.add(pos);
					BottomArea.openBoundary.remove(checkBottom);
					if(tile.getCroc()){
						BottomArea.addCrocodile(1);
					}
					if(tile.getAnimal()!=-1){
						BottomArea.animal.add(tile.getAnimal());
						BottomArea.setHasAnimal(true);
					}
					/**ADD TIGER**/
					if(BottomArea.openBoundary.isEmpty()){
						BottomArea.setCompleted(true);
					}
				}
			}
		}

		/**LEFT**/
		if(!skipL){
			/**Lake**/
			if(tile.getEdgeL()==2){
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.remove(checkLeft);
				if(tile.getCroc()){
					LeftArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					LeftArea.animal.add(tile.getAnimal());
					LeftArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(LeftArea.openBoundary.isEmpty()){
					LeftArea.setCompleted(true);
				}
			}
			/**Trail**/
			else if(tile.getEdgeL()==1){
				LeftArea.areaCoor.add(pos);
				LeftArea.openBoundary.remove(checkLeft);
				if(tile.getCroc()){
					LeftArea.addCrocodile(1);
				}
				if(tile.getAnimal()!=-1){
					LeftArea.animal.add(tile.getAnimal());
					LeftArea.setHasAnimal(true);
				}
				/**ADD TIGER**/
				if(LeftArea.openBoundary.isEmpty()){
					LeftArea.setCompleted(true);
				}
			}
		}

		int potential = 0;
		
		return 0;
	}

	/**TESTING FUNCTIONS BELOW**/
	public void printLake(){
		for(FeatureArea lake : Lake){
			System.out.println();
			System.out.println("Lake Coordinates:");
			for(Position pos : lake.areaCoor){
				System.out.println(pos.getXPosition() + " " + pos.getYPosition());
			}
			System.out.println("Open Boundaries:");
			for(Boundary bound : lake.openBoundary){
				System.out.println("Coor:" + bound.position.getXPosition() + " " + bound.position.getYPosition() + " Boundary:" + bound.edge);
			}
		}
	}

	public void printTrail(){
		for(FeatureArea trail : Trail){
			System.out.println();
			System.out.println("Trail Coordinates:");
			for(Position pos : trail.areaCoor){
				System.out.println(pos.getXPosition() + " " + pos.getYPosition());
			}
			System.out.println("Open Boundaries:");
			for(Boundary bound : trail.openBoundary){
				System.out.println("Coor:" + bound.position.getXPosition() + " " + bound.position.getYPosition() + " Boundary:" + bound.edge);
			}
		}
	}
	

	public static void main(String[] args){
		/**Feature Updating Testing**/
		HashBoard board = new HashBoard();

		//Tile tile1= new Tile(-1,false,false,2,2,2,2,true,true,true,true,true,true);
		//board.gBoard.put(new Position(1,0), tile1);
		//board.updateFeatures(new Position(1,0), tile1);
		Tile tile2= new Tile(-1,false,false,1,1,1,1,true,true,true,true,true,true,"a");
		board.gBoard.put(new Position(0,1), tile2);
		board.updateFeatures(new Position(0,1),tile2);
		Tile tile3= new Tile(-1,false,false,1,1,2,2,true,false,false,true,false,false,"a");
		board.gBoard.put(new Position(1,1),tile3);
		board.updateFeatures(new Position(1,1),tile3);
		Tile tile4= new Tile(-1,false,false,1,2,1,2,false,true,true,false,false,false,"a");
		board.gBoard.put(new Position(2,1),tile4);
		board.updateFeatures(new Position(2,1),tile4);
		Tile tile5= new Tile(-1,false,false,2,2,2,2,true,true,true,true,true,true,"a");
		board.gBoard.put(new Position(2,0),tile5);
		board.updateFeatures(new Position(2,0),tile5);
		Tile tile6= new Tile(-1,false,false,2,2,2,2,true,true,true,true,true,true,"a");
		board.gBoard.put(new Position(2,-1),tile6);
		board.updateFeatures(new Position(2,-1),tile6);
		Tile tile7= new Tile(-1,false,false,2,2,2,2,true,true,true,true,true,true,"a");
		board.gBoard.put(new Position(1,-1),tile7);
		board.updateFeatures(new Position(1,-1),tile7);
		Tile tile1= new Tile(-1,false,false,2,2,2,2,true,true,true,true,true,true,"a");
		board.gBoard.put(new Position(1,0), tile1);
		board.updateFeatures(new Position(1,0), tile1);


		board.printLake();
		board.printTrail();


		/**Check Open Spots Testing**/
		/*HashBoard board = new HashBoard();
		board.gBoard.put(new Position(0, 0), new Tile());
		board.updateOpenSpots(new Position(0, 0));
		board.printKeys();
		board.gBoard.put(new Position(1, 0), new Tile());
		board.updateOpenSpots(new Position(1, 0));
		board.printKeys();
		board.gBoard.put(new Position(2, 0), new Tile());
		board.updateOpenSpots(new Position(2, 0));
		board.printKeys();
		board.gBoard.put(new Position(0, -1), new Tile());
		board.updateOpenSpots(new Position(0, -1));
		board.printKeys();*/



		/* Check if Valid Move Test
		TileInterpreter ti = new TileInterpreter();				
		ti.interpret("TLTJ-\nTTTT-\nTJTJ-\nJJJJ-\nLLJJ-");	
		Tile[] arr = ti.getTileArray();							
																
		HashBoard board = new HashBoard();
		board.AddTile(new Position(0,0), arr[0]);				
		board.updateOpenSpots(new Position(0, 0));
		board.printKeys();
		
		board.AddTile(new Position(0,1), arr[1]);
		board.AddTile(new Position(0,2), arr[2]);
		board.AddTile(new Position(-1,0), arr[3]);
		board.AddTile(new Position(-1,1), arr[4]);

		*/


		/**Check Open Boundary Equal function and HashCode works and FeatureArea equals function**/
		/*FeatureArea first = new FeatureArea();
		first.areaCoor.add(new Position(0,0));
		first.areaCoor.add(new Position(0,0));
		first.openBoundary.add(new Boundary(new Position (0,0),1));
		first.openBoundary.add(new Boundary(new Position (0,0),1));

		FeatureArea second = new FeatureArea();
		second.areaCoor.add(new Position(0,0));
		second.openBoundary.add(new Boundary(new Position (0,0),1));

		if(first.equals(second)){
			System.out.print("TRUE");
		}
		else System.out.print("FALSE");*/


		/**System.out.println("We Have Started a New Game");
		System.out.println("Choose Your Position (Format: X Y )");
		Scanner scan = new Scanner(System.in);
		int x = scan.nextInt();
		int y = scan.nextInt();

		System.out.println("X Location: " + x + " \nY Location: " + y);

		if(board.didAddTile(new Position(x, y), new Tile())){
			System.out.println("That is a viable location.");
		} else {
			System.out.println("Nahhhhhhh");
		}
		 **/
	}



}
