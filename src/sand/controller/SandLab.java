package sand.controller;

import java.awt.*;
import java.util.*;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here

  public static int particleIndex = 0;
  
  private static final int[][] NEIGHBOURS = {
		    {-1, -1}, {-1, 0}, {-1, +1},
		    { 0, -1},          { 0, +1},
		    {+1, -1}, {+1, 0}, {+1, +1}};
  
  public static enum Particles {
	   EMPTY("Empty", new Color(0, 191, 255)), METAL("Metal", Color.GRAY), SAND("Sand", Color.YELLOW), WATER("Water", Color.BLUE), BOMB("Bomb", Color.RED), TREE("Tree", new Color(83,49,35)), INACTIVE_TREE("Inactive Tree", new Color(83,49,35)), LEAF("Leaf",Color.GREEN), GRASS("Grass",new Color(85, 107, 47)), FIRE("Fire", new Color(255, 140, 0)), GLASS("Glass", Color.WHITE);

	   int index;
	   String name;
	   Color color;
	   Particles(String name, Color color) { //values in brackets are set to price property in enum
	      this.index = particleIndex;
	      this.name = name;
	      this.color = color;
	      particleIndex += 1;
	   }
	   int returnIndex() {
	      return index;  //you have to declare methods in enum to return value, there is no predefined function like returnValue()
	   } 
	   String returnName() {
		   return name;
	   }
	   Color returnColor() {
		   return color;
	   }
	}
  
  //do not add any more fields below
  private int[][] grid;
  private Color[] color;
  private SandDisplay display;
  
  
  /**
   * Constructor for SandLab
   * @param numRows The number of rows to start with
   * @param numCols The number of columns to start with;
   */
  public SandLab(int numRows, int numCols)
  {
    String[] names;
    // Change this value to add more buttons
    //Step 4,6
    
    int index = 0;
    for(Particles p : Particles.values()) {
    		index++;
    }
    
    names = new String[index];
    grid = new int[numRows][numCols];
    // Each value needs a name for the button
    for(Particles p : Particles.values()) {
		names[p.returnIndex()] = p.returnName();
    }
    
    color = new Color[names.length];
    for(Particles p : Particles.values()) {
		color[p.returnIndex()] = p.returnColor();
    }
    
    //1. Add code to initialize the data member grid with same dimensions
    
    
    display = new SandDisplay("Falling Sand", numRows, numCols, names);
  }
  
  //called when the user clicks on a location using the given tool
  private void locationClicked(int row, int col, int tool)
  {
    //2. Assign the values associated with the parameters to the grid
   grid[row][col] = tool;
  }

  //copies each element of grid into the display
  public void updateDisplay()
  {
      //Step 3
   //Hint - use a nested for loop
    for(int row = 0; row < grid.length; row++) {
    		for(int col = 0; col < grid[0].length; col++) {
    			display.setColor(row, col, color[grid[row][col]]);
    		}
    }
  }

  //Step 5,7
  //called repeatedly.
  //causes one random particle in grid to maybe do something.
  public void step()
  {
    //Remember, you need to access both row and column to specify a spot in the array
    //The scalar refers to how big the value could be
    //int someRandom = (int) (Math.random() * scalar)
    //remember that you need to watch for the edges of the array
	  
	  int row = (int) (Math.random() * grid.length);
	  int col = (int) (Math.random() * grid[0].length);
	  int direction = (int) (Math.random() * 3);
	  if(grid[row][col] == Particles.SAND.returnIndex()) {
		  if(row+1 < grid.length) {
			  if(grid[row+1][col] == Particles.EMPTY.returnIndex() || grid[row+1][col] == Particles.WATER.returnIndex()) {
				  grid[row+1][col] = Particles.SAND.returnIndex();
				  grid[row][col] = Particles.EMPTY.returnIndex();
			  }
		  }
	  }else if(grid[row][col] == Particles.WATER.returnIndex()) {
		  
		  //Direction is down
		  if(direction == 0) {
			  //Move down
			  if(row+1 < grid.length) {
				  if(grid[row+1][col] == Particles.EMPTY.returnIndex()) {
					  grid[row+1][col] = Particles.WATER.returnIndex();
					  grid[row][col] = Particles.EMPTY.returnIndex();
				  }
			  }
			  
			  
		  }
		  //Move right
		  else if(direction == 1) {
			  if(col+1 < grid[0].length) {
				  if(grid[row][col+1] == Particles.EMPTY.returnIndex()) {
					  grid[row][col+1] = Particles.WATER.returnIndex();
					  grid[row][col] = Particles.EMPTY.returnIndex();
				  }
			  }
		  }
		  //Move left
		  else {
			  
			  if(col-1 > -1) {
				  if(grid[row][col-1] == Particles.EMPTY.returnIndex()) {
					  grid[row][col-1] = Particles.WATER.returnIndex();
					  grid[row][col] = Particles.EMPTY.returnIndex();
				  }
			  }
		  }
	  }else if(grid[row][col] == Particles.BOMB.returnIndex()) {

		  grid[row][col] = Particles.EMPTY.returnIndex();
		  
		  if(row - 1 > -1) {
			  grid[row-1][col] = Particles.BOMB.returnIndex();
			  if(col - 1 > -1) {
				  grid[row][col-1] = Particles.BOMB.returnIndex();
			  }
		  }
	  }else if(grid[row][col] == Particles.TREE.returnIndex()) {
		  int treeGrowth = (int) (Math.random() * 3) - 1;
		  if(row-1 < grid.length && row-1 > -1 && col+1 < grid[0].length && col-1 > -1) {
			  if(!(grid[row][col-1] == Particles.TREE.returnIndex() || grid[row][col-1] == Particles.INACTIVE_TREE.returnIndex()) && !(grid[row][col-1] == Particles.TREE.returnIndex() || grid[row][col+1] == Particles.INACTIVE_TREE.returnIndex())) {
				  grid[row-1][col+treeGrowth] = Particles.TREE.returnIndex();
				  if((int) (Math.random()*5) > 1) {
					  grid[row][col] = Particles.INACTIVE_TREE.returnIndex();
				  }
			  }
		  }
		  //Grow leafs
		  if(col+1 < grid[0].length && col-1 > -1 && row < 5) {
			  grid[row][col+1] = Particles.LEAF.returnIndex();
			  grid[row][col-1] = Particles.LEAF.returnIndex();
		  }
	  }else if(grid[row][col] == Particles.FIRE.returnIndex()) {
		  grid[row][col] = Particles.EMPTY.returnIndex();
		  if(row - 1 > -1 && row + 1 < grid.length) {
			  if(col - 1 > -1 && col+1 < grid[0].length) {
				  int addedRow = 0;
				  int addedCol = 0;

				  for (int[] offset : NEIGHBOURS) {
				        	if(grid[row+offset[0]][col+offset[1]] == Particles.TREE.returnIndex() || grid[row+offset[0]][col+offset[1]] == Particles.INACTIVE_TREE.returnIndex()  || grid[row+offset[0]][col+offset[1]] == Particles.LEAF.returnIndex() || grid[row+offset[0]][col+offset[1]] == Particles.SAND.returnIndex()) {
								
								  grid[row+offset[0]][col+offset[1]] = Particles.FIRE.returnIndex();
				        	}
		  			}
			  }
		  }
	  }
	  
	  
	  
	  
    
  }
  
  //do not modify this method!
  public void run()
  {
    while (true) // infinite loop
    {
      for (int i = 0; i < display.getSpeed(); i++)
      {
        step();
      }
      updateDisplay();
      display.repaint();
      display.pause(1);  //wait for redrawing and for mouse
      int[] mouseLoc = display.getMouseLocation();
      if (mouseLoc != null)  //test if mouse clicked
      {
        locationClicked(mouseLoc[0], mouseLoc[1], display.getTool());
      }
    }
  }
}
