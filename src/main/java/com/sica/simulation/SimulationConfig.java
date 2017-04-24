package com.sica.simulation;

import sim.field.grid.Grid2D;

/**
 * Singleton class holding model parameters (both constant and non-constant)
 * @author Tobias
 *
 */
public class SimulationConfig {
	
	// constant parameters
	
	// Grid's size
	public static final int GRID_HEIGHT = 100;
	public static final int GRID_WIDTH = 100;

	// Hive's size
	public static final int HIVE_HEIGHT = 10;
	public static final int HIVE_WIDTH = 10;
	
	// Flowers's size
	public static final int NORMAL_FLOWER_HEIGHT = 5;
	public static final int NORMAL_FLOWER_WIDTH = 5;
	
	// Environment mode (bounded, unbounded, toroidal)
	public static final int ENV_MODE = Grid2D.BOUNDED;
	
	// type of obstacles
	// TODO decide if manipulable in GUI, are there other types of obstacles?
	public static final boolean WALL_OBSTACLES = false;
	
	
	// PROTECTED simulation variables so only the controller can modify them
	// ?? What is the controller?
	protected int numBees = 1000;
	protected int numWorkers = 250;
	protected int numFlowers = 1;
	protected float groupingAffinity = 0.95f;
	protected int radioView = 5;
	protected float percentageObstacle = 0.1f;
	protected int numberOfWalls = 20;
	protected int wallLength = 30;
	
	
	/*
	 * If this object is registered in the BeeGUI via getSimulationInspectedObject,
	 * then the following properties will appear in the 'Model' tab of the console
	 * window.
	 * 
	 * TODO Is there a way to avoid that all these parameters are displayed in an
	 * arbitrary order in the GUI?
	 */
	
	// ------------------------------
	// parameters to set up the world
	// ------------------------------
	
	
	// bees (deprecated)
	public int getNumBees() {
		return numBees;
	}
	
	public void setNumBees(int numBees) {
		if(numBees >= 0)
			this.numBees = numBees;
	}
	
	// worker bees
	public int getNumWorkers() {
		return numWorkers;
	}
	
	public void setNumWorkers(int numWorkers) {
		if(numWorkers >= 0)
			this.numWorkers = numWorkers;
	}
	
	// flowers
	public int getNumFlowers() {
		return numFlowers;
	}
	
	public void setNumFlowers(int numFlowers) {
		if(numFlowers >= 0)
			this.numFlowers = numFlowers;
	}
	
	// obstacles
	
	// random
	public float getPercentageObstacle() {
		return percentageObstacle;
	}
	
	public void setPercentageObstacle(float percentageObstacle) {
		if(percentageObstacle >= 0.0f && percentageObstacle <= 1.0f)
			this.percentageObstacle = percentageObstacle;
	}
	
	// walls
	public int getNumberOfWalls() {
		return numberOfWalls;
	}
	
	public void setNumberOfWalls(int numberOfWalls) {
		if(numberOfWalls >= 0)
			this.numberOfWalls = numberOfWalls;
	}
	
	public int getWallLength() {
		return wallLength;
	}
	
	public void setWallLength(int wallLength) {
		if(wallLength >= 1)
			this.wallLength = wallLength;
	}
	
	// ------------------------------------------------
	// parameters than can be changed during simulation
	// ------------------------------------------------
	
	// grouping affinity (deprecated?)
	public void setGroupingAffinity(float groupingAffinity) {
		this.groupingAffinity = groupingAffinity;
	}
	
	public float getGroupingAffinity() {
		return groupingAffinity;
	}
	
	public Object domGroupingAffinity(){
		return new sim.util.Interval(0.5, 1.0);
	}
	
	// view radius of bees
	public int getRadioView() {
		return radioView;
	}
	
	public void setRadioView(int radioView) {
		this.radioView = radioView;
	}
	
	
	
	
	
	// singleton stuff
	private static SimulationConfig instance;
	private SimulationConfig() {}
	public static SimulationConfig config() {
		if (instance == null) {
			instance = new SimulationConfig();
		}
		return instance;
	}
	
}
