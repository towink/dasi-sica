package com.util.searching;


/**
 * Class to store the cell states for every cell in the map.
 * Used for storing which cells have already been visited
 * or are still unexplored.
 */
public class Map {
	private int width, height;
	private boolean[][] mapState;
	
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		this.mapState = new boolean[width][height];
	}
	
	/**
	 * Resets the state so that all cells are unexplored
	 */
	public void resetState() {
		for (int i = 0; i < width; i++) 
			for (int j = 0; j < height; j++) 
				this.mapState[i][j] = false;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return true if the give position has already been processed
	 */
	public boolean isVisited(int x, int y) {
		return this.mapState[x][y];
	}

	/**
	 * Update if the position (x,y) has been visited
	 * @param x
	 * @param y
	 * @param visited 
	 */
	public void setVisited(int x, int y) {
		this.mapState[x][y] = true;
	}
	
	/**
	 * @return the width of this map
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return the height of this map
	 */
	public int getHeight() {
		return this.height;
	}
}