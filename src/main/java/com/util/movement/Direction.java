package com.util.movement;

import sim.util.Int2D;

public enum Direction {
	NORTH, SOUTH, EAST, WEST, NORTH_EAST, SOUTH_EAST, NORTH_WEST, SOUTH_WEST;
	
	public Int2D move(Int2D origin, int mode) {
		int x = origin.x, y = origin.y;
		switch(this) {
		case NORTH:
			y--;
			break;
		case SOUTH:
			y++;
			break;
		case EAST:
			x++;
			break;
		case WEST:
			x--;
			break;
		case NORTH_EAST:
			y--;
			x++;
			break;
		case SOUTH_EAST:
			y++;
			x++;
			break;
		case NORTH_WEST:
			y--;
			x--;
			break;
		case SOUTH_WEST:
			y++;
			x--;
			break;
		}
		return new Int2D(x, y);
	}
	
	public Int2D getMovementOf(Int2D origin, int mode, int gridWidth, int gridHeight) {
		Int2D result = this.move(origin, mode);
		result = PositioningFunctions.fitToGrid(result, mode, gridWidth, gridHeight);
		return result;
	}
}
