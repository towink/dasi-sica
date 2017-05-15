package com.util.movement;

import com.sica.entities.Entity;
import com.sica.entities.Entity.EntityType;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.TwoDVector;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;

import ec.util.MersenneTwisterFast;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class PositioningFunctions {
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
	
	
	/**
	 * Find a position that satisfies:
	 * 	it is at distance 'distance' to 'closeToWhere'
	 * 	while being as far as possible from 'farFromWhere'
	 * @param closeToWhere
	 * @param distance
	 * @param farFromWhere
	 * @param kMap do not return positions that are Obstacles from here
	 * @return a position satisfying the requierements
	 */
	public static Int2D findValidPosition(Int2D closeToWhere, int distance, Int2D farFromWhere, KnowledgeMapInterface kMap, MersenneTwisterFast random) {
		int vectorx = closeToWhere.x - farFromWhere.x;
		int vectory = closeToWhere.y - farFromWhere.y;
		TwoDVector vector;
		if (vectorx != 0 || vectory != 0) {	//if the vector is not zero
			vector = new TwoDVector(vectorx, vectory);
		} else { //find a random position
			vector = new TwoDVector(random.nextDouble() - 0.5, random.nextDouble() - 0.5);
		}
		vector.normalize();
		vector.dot(distance);
		
		Int2D destination = new Int2D(closeToWhere.x + (int) vector.x, closeToWhere.y + (int) vector.y);
		
		return PositioningFunctions.findValidPosition(destination, kMap, random);
	}
	
	/**
	 * Find a position that satisfies:
	 * 	it is at distance 'distance' to 'closeToWhere
	 * @param closeToWhere
	 * @param distance
	 * @param kMap do not return positions that are Obstacles from here
	 * @return a position satisfying the requierements
	 */
	public static Int2D findValidPosition(Int2D closeToWhere, int distance, KnowledgeMapInterface kMap, MersenneTwisterFast random) {
		TwoDVector vector = new TwoDVector(random.nextFloat() - 0.5, random.nextFloat() - 0.5);
		vector.normalize();
		vector.dot(distance);
		Int2D destination = new Int2D(closeToWhere.x + (int) vector.x, closeToWhere.y + (int) vector.y);
		
		return PositioningFunctions.findValidPosition(destination, kMap, random);
	}
	
	/**
	 * Finds a valid position (in the sense that it is not an obstacle)
	 * near the destination
	 * If it fails to do so, it will still return a position
	 * but it will be an obstacle
	 * @param destination
	 * @param kMap
	 * @param random
	 * @return the newly found position
	 */
	private static Int2D findValidPosition(Int2D destination, KnowledgeMapInterface kMap, MersenneTwisterFast random) {
		int timeout = 0; //try 10 times, otherwise just return what you can
		while (kMap.getKnowledgeAt(destination) == Knowledge.OBSTACLE && timeout < 10) { 
			destination = PositioningFunctions.fitToGrid(
					new Int2D(destination.x + random.nextInt(3) - 1, destination.y + random.nextInt(3) - 1), 
					SimulationConfig.ENV_MODE, 
					SimulationConfig.GRID_WIDTH, 
					SimulationConfig.GRID_HEIGHT);
			timeout++;
		}
		return destination;
	}
	
	/**
	 * Gets the center of the entity swarm sent in the bag
	 * if the type is restricted, only objects of the given
	 * type are taken into account, otherwise all objects
	 * are used
	 * @param simState
	 * @param entityBag
	 * @param restrictType
	 * @param type
	 * @return
	 */
	public static Int2D getSwarmCentroid(SimulationState simState, Bag entityBag, boolean restrictType, EntityType type) {
		float ax = 0, ay = 0, count = 0; //accumulators to calculate the centroid
		for (Object o: entityBag) {
			Entity e = (Entity) o;
			if (!restrictType || e.getType() == type) {
				Int2D otherlocation = simState.entities.getObjectLocation(e);
				ax += otherlocation.x;
				ay += otherlocation.y;
				count++;
			}
		}
		return new Int2D((int) (ax / count), (int) (ay / count));
	}
	
}
