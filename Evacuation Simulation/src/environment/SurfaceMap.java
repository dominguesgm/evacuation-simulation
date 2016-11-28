package environment;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SurfaceMap {
	ArrayList<ArrayList<Character>> physicalMap;
	ArrayList<ArrayList<Integer>> distanceMap;
	
	int height, width;
	
	ArrayList<Pair<Integer, Integer>> exits;
	
	Comparator<Pair<Integer,Integer>> distanceComparator;
	
	public SurfaceMap ( String filename ) {
		
		MapParser parser = new MapParser(filename);
		
		this.physicalMap = parser.getMap();
		this.exits = new ArrayList<Pair<Integer, Integer>>();
		this.height = this.physicalMap.size();
		this.width = this.physicalMap.get(0).size();
		this.distanceMap = new ArrayList<ArrayList<Integer>>();
		
		for(int i = 0; i < this.height; i++){
			ArrayList<Integer> line = new ArrayList<Integer>();
			for(int j = 0; j < this.width; j++){
				line.add(-1);
			}
			this.distanceMap.add(line);
		}
		
		determineExits();
		
		generateDistanceMap();
		
		distanceComparator = new Comparator<Pair<Integer,Integer>>() {
	        @Override
	        public int compare(Pair<Integer,Integer> cell1, Pair<Integer,Integer> cell2)
	        {

	            return  distanceMap.get(cell1.getY()).get(cell1.getX()).compareTo(distanceMap.get(cell2.getY()).get(cell2.getX()));
	        }
	    };
		
		
	}

	private void generateDistanceMap() {
		// TODO Auto-generated method stub
		Pair<Integer,Integer> north = null;
		Pair<Integer,Integer> east = null;
		Pair<Integer,Integer> west = null;
		Pair<Integer,Integer> south = null;
		
		for(Pair<Integer, Integer> exit : exits){
			int distance = 1;
			boolean covered = false;
			boolean foundNewCell = false;
			ArrayList<Pair<Integer,Integer>> visited = new ArrayList<Pair<Integer,Integer>>();
			ArrayDeque<Pair<Integer,Integer>> toVisit = new ArrayDeque<Pair<Integer,Integer>>();
			ArrayDeque<Pair<Integer,Integer>> newToVisit = new ArrayDeque<Pair<Integer,Integer>>();
			toVisit.add(exit);
			distanceMap.get(exit.getY()).set(exit.getX(), 0);
			
			while(!covered){
				for(Pair<Integer,Integer> cell : toVisit){
					toVisit.removeFirst();
					north = new Pair<Integer,Integer>(cell.getX(), cell.getY()-1);
					south = new Pair<Integer,Integer>(cell.getX(), cell.getY()+1);
					east = new Pair<Integer,Integer>(cell.getX()+1, cell.getY());
					west = new Pair<Integer,Integer>(cell.getX()-1, cell.getY());
					
					if(north.getX() >= 0 && north.getX() < this.width && north.getY() >= 0 && north.getY() < this.height && !visited.contains(north) && physicalMap.get(north.getY()).get(north.getX()) == ' '){
						if(distanceMap.get(north.getY()).get(north.getX()) > distance || distanceMap.get(north.getY()).get(north.getX()) == -1){
							distanceMap.get(north.getY()).set(north.getX(), distance);
							newToVisit.add(north);
							foundNewCell = true;
						}
						visited.add(north);
					}
					if(east.getX() >= 0 && east.getX() < this.width && east.getY() >= 0 && east.getY() < this.height && !visited.contains(east) && physicalMap.get(east.getY()).get(east.getX()) == ' '){
						if(distanceMap.get(east.getY()).get(east.getX()) > distance || distanceMap.get(east.getY()).get(east.getX()) == -1){
							distanceMap.get(east.getY()).set(east.getX(), distance);
							newToVisit.add(east);
							foundNewCell = true;
						}
						visited.add(east);
					}
					if(south.getX() >= 0 && south.getX() < this.width && south.getY() >= 0 && south.getY() < this.height && !visited.contains(south) && physicalMap.get(south.getY()).get(south.getX()) == ' '){
						if(distanceMap.get(south.getY()).get(south.getX()) > distance || distanceMap.get(south.getY()).get(south.getX()) == -1){
							distanceMap.get(south.getY()).set(south.getX(), distance);
							newToVisit.add(south);
							foundNewCell = true;
						}
						visited.add(south);
					}
					if(west.getX() >= 0 && west.getX() < this.width && west.getY() >= 0 && west.getY() < this.height && !visited.contains(west) && physicalMap.get(west.getY()).get(west.getX()) == ' '){
						if(distanceMap.get(west.getY()).get(west.getX()) > distance || distanceMap.get(west.getY()).get(west.getX()) == -1){
							distanceMap.get(west.getY()).set(west.getX(), distance);
							newToVisit.add(west);
							foundNewCell = true;
						}
						visited.add(west);
					}
				}
				toVisit = newToVisit;
				newToVisit = new ArrayDeque<Pair<Integer, Integer>>();
				distance++;
				if(!foundNewCell)
					covered = true;
				else foundNewCell = false;
				
			}
		}
		
	}

	private void determineExits() {
		// TODO Auto-generated method stub
		for(int y = 0; y < physicalMap.size(); y++){
			for(int x = 0; x < physicalMap.get(0).size(); x++){
				if(physicalMap.get(y).get(x) == 'E'){
					exits.add(new Pair<Integer, Integer>(x, y));
				}
			}
		}
	}

	public ArrayList<ArrayList<Character>> getPhysicalMap() {
		return physicalMap;
	}

	public ArrayList<ArrayList<Integer>> getDistanceMap() {
		return distanceMap;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public char getObjectAt(int x, int y){
		return physicalMap.get(y).get(x);
	}
	
	public int getDistanceAt(int x, int y){
		return distanceMap.get(y).get(x);
	}
	
	public ArrayList<Pair<Integer, Integer>> getBestPathFromCell(int x, int y){
		ArrayList<Pair<Integer,Integer>> neighbourCells = new ArrayList<Pair<Integer,Integer>>();
		
		if(x > 0)
			neighbourCells.add(new Pair<Integer, Integer>(x-1, y));
		
		if(y > 0)
			neighbourCells.add(new Pair<Integer, Integer>(x, y-1));
		
		if(x < width - 1)
			neighbourCells.add(new Pair<Integer, Integer>(x+1, y));
		
		if(y < height - 1)
			neighbourCells.add(new Pair<Integer, Integer>(x, y+1));
		
		Collections.sort(neighbourCells, distanceComparator);
		
		return neighbourCells;
	}
	
}
