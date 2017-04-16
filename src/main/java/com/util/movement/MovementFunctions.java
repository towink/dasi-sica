package com.util.movement;

import sim.field.grid.Grid2D;
import sim.util.Int2D;

public class MovementFunctions {
	/**
	 * Fits the point within the grid limits using the given mode
	 * @param p the Point to be fitted
	 * @param mode can be Grid2D.TOROIDAL, Grid2D.BOUNDED, Grid2D.UNBOUNDED
	 * @param gridWidth
	 * @param gridHeight
	 * @return a new Int2D if it was modified, or the same one fitted to specificaiton
	 * @throws IllegalArgument if mode is not one of the 3 specified
	 */
	public static Int2D fitToGrid(Int2D origin, int mode, int gridWidth, int gridHeight) {
		switch(mode) {
		//keep in the interval [0, gridXX). Roll over if over/under limits
		case Grid2D.TOROIDAL:
			return new Int2D(
					Math.floorMod(origin.x, gridWidth),
					Math.floorMod(origin.y, gridWidth)
				);
		//keep in the interval [0, gridXX). Clamp to interval if over/under limits
		case Grid2D.BOUNDED:
			return new Int2D(
					Math.max(0, Math.min(origin.x, gridWidth-1)),
					Math.max(0, Math.min(origin.y, gridHeight-1))
				);
		//no checks to do here
		case Grid2D.UNBOUNDED:
			return origin;
		default:
			throw new IllegalArgumentException("Mode not recognized @Point.fitToGrid");
		}
	}
}
