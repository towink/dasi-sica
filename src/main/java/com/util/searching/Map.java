package com.util.searching;

import com.util.knowledge.Knowledge;

public class Map {

	public static int IMPASSABLE = 10000;

	public int width;
	public int height;
	public boolean[][] visited;
	
	private Knowledge[][] map;

	public Map (int width, int height) {
		setWidth(width);
		setHeight(height);
		map = new Knowledge[width][height];
		visited = new boolean[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = Knowledge.EMPTY;
				setVisited(i, j, false);
			}
		}
	}

	/**
	 * Method to update the knowledge. It put in the position (x,y) the new data
	 * @param x
	 * @param y
	 * @param type
	 * @return true if the value has changed or false if it has not changed
	 */
	public boolean modifyMap (int x, int y, Knowledge type) {
		boolean changed = false;
		if (isValidPosition(x, y)) {
			if (map[x][y] != type) {
				changed = true;
				map[x][y] = type;
			}
		}
		return changed;
	}

	/**
	 * Method to update all the knowledge.
	 * @param type
	 * @param width
	 * @param height
	 */
	public void modifyMap (Knowledge [][] type, int width, int height) {
		if ((getWidth() == width) && (getHeight() == height)) {
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					map[i][j] = type[i][j];
				}
			}
		}
	}
	/**
	 * Method to know what is in the position (x,y)
	 * @param x
	 * @param y
	 * @return 
	 */
	public Knowledge getType (int x, int y) {
		if (isValidPosition(x, y)) {
			return map[x][y];
		}
		return Knowledge.UNKNOWN;
	}
	
	public int getCost (int x, int y) {
		if (isValidPosition(x, y)) {
			switch (map[x][y]) {
			case OBSTACLE:
				return IMPASSABLE;
			case EMPTY:
				return 1;
			case FLOWER:
				return 1;
			case BEE:
				return 2;
			case ENEMY:
				return IMPASSABLE;
			case UNKNOWN:
				return IMPASSABLE;
			default:
				break; 
			}
		}
		return -1;
	}
	
	/**
	 * Method to clear the map, it put every position like no visited
	 */
	public void resetVisited () {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				setVisited(i, j, false);
			}
		}
	}

	private boolean isValidPosition(int x, int y) {
		return (x >= 0) && (x < getWidth()) && (y >= 0) && (y < getHeight());
	}
	
	// getters and setters
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Method to check if the position (x,y) has been visited
	 * @param x
	 * @param y
	 * @return True if the position has been visited or false if it has not been visited
	 */
	public boolean isVisited(int x, int y) {
		if (isValidPosition(x, y)) {
			return visited[x][y];
		}
		return false;
	}

	/**
	 * Update if the position (x,y) has been visited
	 * @param x
	 * @param y
	 * @param visited 
	 */
	public void setVisited(int x, int y, boolean visited) {
		if (isValidPosition(x, y)) {
			this.visited[x][y] = visited;
		}
	}

}

