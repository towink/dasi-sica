package com.sica.simulation;

import sim.field.grid.Grid2D;

public class SimulationConfig {
	// Grid's size
	public static final int GRID_HEIGHT = 100;
	public static final int GRID_WIDTH = 100;

	// Hive's size
	public static final int HIVE_HEIGHT = 10;
	public static final int HIVE_WIDTH = 10;
	
	//Flowers's size
	public static final int NORMAL_FLOWER_HEIGHT = 5;
	public static final int NORMAL_FLOWER_WIDTH = 5;
	
	// env mode (bounded, unbounded, toroidal)
	public static final int ENV_MODE = Grid2D.BOUNDED;

	// PROTECTED simulation variables so only the controller can modify them
	
	// agent parameters
	protected int numBees = 1000;
	protected float groupingAffinity = 0.95f;
	protected int radioView = 5;
	
	// environment parameters
	protected int numFlowers = 1;
	protected int minAlimentFlower = 5;
	protected int maxAlimentFlower = 20;
	protected float percentageObstacle = 0.1f;
	protected int numberOfWalls = 20;
	protected int wallLength = 20;
	
	// PUBLIC getters so everyone can see the configuration
	public int getNumBees() {
		return numBees;
	}
	
	public int getNumFlowers() {
		return numFlowers;
	}
	
	public float getGroupingAffinity() {
		return groupingAffinity;
	}
	
	public int getRadioView() {
		return radioView;
	}
	
	public float getPercentageObstacle() {
		return percentageObstacle;
	}
	
	public int getNumberOfWalls() {
		return numberOfWalls;
	}
	
	public int getWallLength() {
		return wallLength;
	}
	
	public int getMinAlimentFlower() {
		return minAlimentFlower;
	}
	
	public int getMaxAlimentFlower() {
		return maxAlimentFlower;
	}
}
