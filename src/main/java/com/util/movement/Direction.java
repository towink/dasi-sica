package com.util.movement;

import sim.util.Int2D;

public enum Direction {
	NORTH, SOUTH, EAST, WEST;
	
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
		}
		return new Int2D(x, y);
	}
	
	public Int2D getMovementOf(Int2D origin, int mode, int gridWidth, int gridHeight) {
		Int2D result = this.move(origin, mode);
		MovementFunctions.fitToGrid(result, mode, gridWidth, gridHeight);
		return result;
	}
}
