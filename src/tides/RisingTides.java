package tides;

import java.util.*;

/**
 * This class contains methods that provide information about select terrains 
 * using 2D arrays. Uses floodfill to flood given maps and uses that 
 * information to understand the potential impacts. 
 * Instance Variables:
 *  - a double array for all the heights for each cell
 *  - a GridLocation array for the sources of water on empty terrain 
 * 
 * @author Original Creator Keith Scharz (NIFTY STANFORD) 
 * @author Vian Miranda (Rutgers University)
 */
public class RisingTides {

    // Instance variables
    private double[][] terrain;     // an array for all the heights for each cell
    private GridLocation[] sources; // an array for the sources of water on empty terrain 

    /**
     * DO NOT EDIT!
     * Constructor for RisingTides.
     * @param terrain passes in the selected terrain 
     */
    public RisingTides(Terrain terrain) {
        this.terrain = terrain.heights;
        this.sources = terrain.sources;
    }

    /**
     * Find the lowest and highest point of the terrain and output it.
     * 
     * @return double[][], with index 0 and index 1 being the lowest and 
     * highest points of the terrain, respectively
     */
    public double[] elevationExtrema() {
        /* WRITE YOUR CODE BELOW */
        double lowest= terrain[0][0];
        double highest= terrain [0][0];
        for(int i=0; i< terrain.length; i++ ){
            for (int j =0; j< terrain[i].length; j++){
                double temp = terrain[i][j];
                if(temp< lowest){
                    lowest = temp;
                }

                if(temp >highest){
                    highest = temp;
                }
            }
        }
        return new double [] {lowest, highest} ; 
    }

    /**
     * Implement the floodfill algorithm using the provided terrain and sources.
     * 
     * All water originates from the source GridLocation. If the height of the 
     * water is greater than that of the neighboring terrain, flood the cells. 
     * Repeat iteratively till the neighboring terrain is higher than the water 
     * height.
     * 
     * 
     * @param height of the water
     * @return boolean[][], where flooded cells are true, otherwise false
     */
    public boolean[][] floodedRegionsIn(double height) {
        /* WRITE YOUR CODE BELOW */
        boolean[][] resultingarray =new boolean[terrain.length][terrain[0].length];
        ArrayList<GridLocation> gridlocations= new ArrayList<>();
            for (GridLocation source:sources) {
            gridlocations.add(source);
            int row =source.row;
            int col= source.col;
            resultingarray[row][col]= true;
        }
        while (!gridlocations.isEmpty()) {
            GridLocation currentLocation= gridlocations.remove(0);
            int row= currentLocation.row;
            int col =currentLocation.col;    
            if (row+ 1 <terrain.length&& terrain[row+1][col]<= height&& !resultingarray[row+ 1][col]){
                resultingarray[row+1] [col] =true;
                gridlocations.add(new GridLocation(row+1,col));
            }
            if (row -1 >=0 && terrain[row- 1][col]<= height&& !resultingarray[row-1][col]){
                resultingarray[row-1][col]=true;
                gridlocations.add(new GridLocation(row -1,col));
            }
            if (col+ 1< terrain[0].length && terrain[row][col +1]<= height&& !resultingarray[row][col+1]){
                resultingarray[row][col+1]=true;
                gridlocations.add(new GridLocation(row, col+ 1));
            }
            if (col-1>= 0 &&terrain[row][col- 1]<= height && !resultingarray[row][col-1]){
                resultingarray[row][col-1]=true;
                gridlocations.add(new GridLocation(row,col- 1));
            }
        }
        return resultingarray;
    }

    /**
     * Checks if a given cell is flooded at a certain water height.
     * 
     * @param height of the water
     * @param cell location 
     * @return boolean, true if cell is flooded, otherwise false
     */
    public boolean isFlooded(double height, GridLocation cell) { 
        /* WRITE YOUR CODE BELOW */
        boolean[][] flooded= floodedRegionsIn(height);
    if(cell.row>= 0 && cell.row< flooded.length && cell.col>= 0 && cell.col< flooded[0].length){
        return flooded[cell.row][cell.col];
    }
    return false;
    }

    /**
     * Given the water height and a GridLocation find the difference between 
     * the chosen cells height and the water height.
     * 
     * If the return value is negative, the Driver will display "meters below"
     * If the return value is positive, the Driver will display "meters above"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param cell location
     * @return double, representing how high/deep a cell is above/below water
     */
    public double heightAboveWater(double height, GridLocation cell) {
        /* WRITE YOUR CODE BELOW */
        double difference= terrain [cell.row] [cell.col]- height;
        if( difference< 0){
           return difference;
        }
        else {
            return difference;
        }  
    }

    /**
     * Total land available (not underwater) given a certain water height.
     * 
     * @param height of the water
     * @return int, representing every cell above water
     */
    public int totalVisibleLand(double height) {
        /* WRITE YOUR CODE BELOW */
        int land =0;
        boolean[][] flooded = floodedRegionsIn(height);
        for(int i =0; i< terrain.length; i++){
            for(int j =0; j <terrain[i].length; j++){
               if(! flooded[i][j]){
                land++;
               } 
            }
        }
        return land;
    } 


    /**
     * Given 2 heights, find the difference in land available at each height. 
     * 
     * If the return value is negative, the Driver will display "Will gain"
     * If the return value is positive, the Driver will display "Will lose"
     * The value displayed will be positive.
     * 
     * @param height of the water
     * @param newHeight the future height of the water
     * @return int, representing the amount of land lost or gained
     */
    public int landLost(double height, double newHeight) {
        /* WRITE YOUR CODE BELOW */
        int lost = totalVisibleLand(height) - totalVisibleLand(newHeight);
        if (lost< 0){
            return lost;
        }
        else{
            return lost;
        }
    }

    /**
     * Count the total number of islands on the flooded terrain.
     * 
     * Parts of the terrain are considered "islands" if they are completely 
     * surround by water in all 8-directions. Should there be a direction (ie. 
     * left corner) where a certain piece of land is connected to another 
     * landmass, this should be considered as one island. A better example 
     * would be if there were two landmasses connected by one cell. Although 
     * seemingly two islands, after further inspection it should be realized 
     * this is one single island. Only if this connection were to be removed 
     * (height of water increased) should these two landmasses be considered 
     * two separate islands.
     * 
     * @param height of the water
     * @return int, representing the total number of islands
     */
    public int numOfIslands(double height) {
       /* WRITE YOUR CODE BELOW */
       WeightedQuickUnionUF tree= new WeightedQuickUnionUF(terrain.length, terrain[0].length);
       boolean [] []flooded = floodedRegionsIn(height);
       int numIslands= 0;
       boolean [][] accounted =new boolean [terrain.length][terrain[0].length];
       GridLocation[] [] roots = new GridLocation[terrain.length][terrain[0].length];
       for(int i= 0; i < terrain.length; i++){
        for(int j=0; j< terrain[0].length; j++){
            if(!flooded[i][j]){
                if(i-1>= 0 && j-1 >=0 && !flooded[i-1][j-1]){
                    tree.union( new GridLocation(i, j), new GridLocation(i-1, j-1));
                }
                if(i-1 >= 0&& !flooded[i-1][j]){
                    tree.union( new GridLocation(i, j), new GridLocation(i-1, j));
                }
                if(i-1 >=0 && j+1<terrain[0].length && !flooded[i-1][j+1]){
                    tree.union(new GridLocation(i, j), new GridLocation(i-1, j+1));
                }
                if(j-1 >=0 && !flooded[i][j-1]){
                    tree.union(new GridLocation(i, j), new GridLocation(i, j-1));
                }
                if( j+1< terrain[0].length && !flooded[i][j+1]){
                    tree.union( new GridLocation(i, j), new GridLocation(i, j+1));
                }
                if (i+1 < terrain.length && j-1>=0 && !flooded[i+1][j]){
                    tree.union(new GridLocation(i, j), new GridLocation(i+1, j-1));
                }
                if (i+1 <terrain.length && !flooded[i+1][j]){
                    tree.union(new GridLocation(i, j), new GridLocation(i+1, j));
                }
                if (i+1 < terrain.length && j+1< terrain[0].length && !flooded[i+1][j+1]){
                    tree.union(new GridLocation(i, j), new GridLocation(i+1, j+1));
                }
            }
        }
       }
       for (int i=0; i<terrain.length; i++){
        for (int j=0; j <terrain[0].length; j++){
            if (!flooded[i][j]){
                GridLocation root= tree.find (new GridLocation(i, j));
                roots[i][j]=root;
            }
        }
       }
       for(int i=0; i<terrain.length; i++){
        for(int j=0; j <terrain[0].length;j++){
            if(!flooded[i][j]&& !accounted[i][j]){
                GridLocation root = roots[i][j];
                if(!accounted[root.row][root.col]){
                    numIslands++;
                    accounted[root.row][root.col]=true;
                }
            }
        }
       }
     return numIslands;
  }
    
}
