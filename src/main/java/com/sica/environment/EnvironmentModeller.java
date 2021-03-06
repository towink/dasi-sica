package com.sica.environment;

import com.sica.simulation.SimulationConfig;
import com.util.knowledge.Knowledge;

import ec.util.MersenneTwisterFast;
import sim.util.Int2D;

public class EnvironmentModeller {
	
	/**
	 * Generate a hive of the given size in the given environment
	 * @param environment
	 * @param hiveWidth
	 * @param hiveHeight
	 */
	@Deprecated
	public static void generateHive (Environment environment, int hiveWidth, int hiveHeight) {
		Int2D center = new Int2D (environment.getWidth()/2, environment.getHeight()/2);
		Int2D length = new Int2D (hiveWidth, hiveHeight);
		environment.set(SimulationConfig.GRID_WIDTH/2,  SimulationConfig.GRID_HEIGHT/2, Knowledge.HIVE);
		environment.drawRect (center, length, Knowledge.HIVE);
	}

	/**
	 * Generate flowers of the given size in the given environment
	 * @param environment
	 * @param flowerWidth
	 * @param flowerHeight
	 */
	public static void generateFlowers (Environment environment, int flowerWidth, int flowerHeight) {
		Int2D length = new Int2D (flowerWidth, flowerHeight);
		environment.drawFillRect(length, length, Knowledge.FLOWER);
	}
	
	/**
	 * Puts a flower at the given position in the environment. The flower will have
	 * the number of aliment units as meta data.
	 * @param env
	 * @param pos
	 * @param aliment
	 */
	public static void generateFlower(Environment env, Int2D pos, int aliment) {
		env.setMetadataAndTypeAt(pos, (short) aliment, Knowledge.FLOWER);
	}
	
	/**
	 * Puts count flowers in the environment at random spots
	 * @param env
	 * @param count
	 * @param minAlimentFlower
	 * @param maxAlimentFlower
	 * @param random
	 */
	public static void randomlyGenerateFlowers(
			Environment env,
			int count,
			int minAlimentFlower,
			int maxAlimentFlower,
			MersenneTwisterFast random) {
		for(int i = 0; i < count; i++) {
			int aliment = random.nextInt(maxAlimentFlower - minAlimentFlower + 1) + minAlimentFlower;
			Int2D pos = new Int2D(
					random.nextInt(SimulationConfig.GRID_WIDTH),
					random.nextInt(SimulationConfig.GRID_HEIGHT));
			// place flowers only in empty spots (could be over entities, but it doesn't matter)
			if (env.hasTypeAt(pos, Knowledge.EMPTY)) {
				generateFlower(env, pos, aliment);
			}
			/*
			if((env.inHive(pos, new Int2D (env.getWidth()/2, env.getHeight()/2))) && 
				(env.hasTypeAt(pos, Knowledge.EMPTY))) {
				i--;
			}
			else {
				generateFlower(env, pos, aliment);
			}*/
		}
	}

	/**
	 * Generate random obstacles across the given environment.
	 * ANY location with an EnvironmenType of EMPTY can be set as an obstacle
	 * with the given probability. No checks are made to see if all locations are still
	 * accessible after genreating the obstacles
	 * @param environment
	 * @param probability
	 * @param rnd should be the simulation's random number generator to always give the same results
	 */
	public static void generateRandomObstacles (Environment environment, float probability, MersenneTwisterFast rnd) {
		for (int x = 0; x < environment.getWidth(); x++) {
			for (int y = 0; y < environment.getHeight(); y++) {
				if ((environment.hasTypeAt(x, y, Knowledge.EMPTY)) &&
					(!environment.inHive(new Int2D (x, y), new Int2D (environment.getWidth()/2, environment.getHeight()/2)))) {
					if (rnd.nextFloat() < probability) {
						environment.set(x, y, Knowledge.OBSTACLE);
					}
				}
			}
		}	
	}

	/**
	 * Places a couple of straight obstacles (walls) in the environment. It is highly unlikely that
	 * this makes any location unaccessible.
	 * There is a chance that this method does not terminate if you try to place too many
	 * and/or too long walls, so be careful.
	 * @param environment
	 * @param count The number of walls to be placed.
	 * @param length Length in grid units of the walls. Must not be too large!
	 * @param rnd should be the simulation's random number generator to always give the same results
	 */
	public static void generateWallObstacles (Environment environment, int count, int length, MersenneTwisterFast rnd) {
		if(length > environment.getHeight() || length > environment.getWidth() || length <= 0) {
			return;
		}
		int rest = count;
		while(rest > 0) {
			boolean horizontal = rnd.nextBoolean();
			if(horizontal) {
				// place a horizontal wall
				int leftWallPointX = rnd.nextInt(environment.getWidth() - length + 1);
				int wallY = rnd.nextInt(environment.getHeight());
				for(int i = 0; i < length; i++) {
					// check if this position is already occupied
					if((environment.hasTypeAt(leftWallPointX + i, wallY, Knowledge.EMPTY)) &&
						(!environment.inHive(new Int2D (leftWallPointX + i, wallY), 
						new Int2D (environment.getWidth()/2, environment.getHeight()/2)))) {
						environment.set(leftWallPointX + i, wallY, Knowledge.OBSTACLE);
					}
						
					else {
						i++; // jump over it so that no dead regions can exist
					}
				}
				rest--;
			}
			else {
				// place a vertical wall
				int topWallPointY = rnd.nextInt(environment.getHeight() - length + 1);
				int wallX = rnd.nextInt(environment.getWidth());
				for(int i = 0; i < length; i++) {
					if((environment.hasTypeAt(wallX, topWallPointY + i, Knowledge.EMPTY)) &&
						(!environment.inHive(new Int2D (wallX, topWallPointY + i), 
						new Int2D (environment.getWidth()/2, environment.getHeight()/2)))) {
						environment.set(wallX, topWallPointY + i, Knowledge.OBSTACLE);
					}
						
					else {
						i++;
					}
				}
				rest--;
			}
		}
	} 

}
