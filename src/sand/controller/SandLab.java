package sand.controller;

import java.awt.*;
import java.util.*;

public class SandLab
{
  //Step 4,6
  //add constants for particle types here

  public static int particleIndex = 0;
  
  public static enum Particles {
	   EMPTY("Empty", Color.BLACK), METAL("Metal", Color.GRAY), SAND("Sand", Color.YELLOW), WATER("Water", Color.BLUE);

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
