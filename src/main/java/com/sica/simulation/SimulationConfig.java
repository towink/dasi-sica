package com.sica.simulation;

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

	// PROTECTED simulation variables so only the controller can modify them
	protected int numBees = 1000;
	protected int numFlowers = 1;
	protected float groupingAffinity = 0.95f;
	protected int radioView = 5;
	protected float percentageObstacle = 0.1f;
	
	
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
}
