package com.sica.simulation;

import java.awt.Color;

import sim.field.grid.Grid2D;

/**
 * Singleton class holding model parameters (both constant and non-constant)
 * @author Tobias
 *
 */
	
public class SimulationConfig {
	// TODO the object registered in BeeGUI via getSimulationInspectedObject should actually be our SimulationState
	// object

	
	// singleton stuff
	private static SimulationConfig instance;
	private SimulationConfig() {}
	// Note that this is not called 'getConfig' so that it does not appear in GUI
	public static SimulationConfig config() {
		if (instance == null) {
			instance = new SimulationConfig();
		}
		return instance;
	}
	
	// constant parameters - we consider them not interesting enough to be able to be changed in the GUI
	
	// Grid's size
	public static final int GRID_HEIGHT = 100;
	public static final int GRID_WIDTH = 100;

	// Hive's size
	public static final int HIVE_HEIGHT = 10;
	public static final int HIVE_WIDTH = 10;
	
	// Environment mode (bounded, unbounded, toroidal)
	public static final int ENV_MODE = Grid2D.BOUNDED;
	
	// sometimes it is convenient to change the background color to black, for example
	public static final Color BACKGROUND_COLOR = Color.BLACK;
	
	
	// PROTECTED simulation variables so only the controller can modify them
	// ?? What is the controller? Why exactly are they protected??
	
	// --- agent parameters ---
	
	// general
	protected int radioView = 10;
	protected int time4Season = 1000;			// they are "step units"
	protected int time2Die = 4;					// they are "season units"
	
	// worker
	protected int numWorkers = 10;
	protected float groupingAffinity = 0.95f;
	protected int flowerThreshold = 2;
	protected int workerMovesBeforeUpdating = 2; //number of times a worker bee moves randomly before scanning

	//defender
	protected float percentageDefender = 30f; 		
	
	//Queen
	protected int time2Create = 10;
	protected int cost2Create = 5;
	
	//enemies
	protected int time4Enemies = 2;			//they are "season units"
	protected int enemies4Season = 30;
	
	// --- environment parameters ---
	
	// obstacles
	protected int numFlowers = 20;
	protected int minAlimentFlower = 5;
	protected int maxAlimentFlower = 20;
	
	// obstacles
	protected float percentageObstacle = 0.1f;
	protected int numberOfWalls = 20;
	protected int wallLength = 20;
	public static final int WALL_OBSTACLES = 0;
	public static final int RANDOM_OBSTACLES = 1;
	protected int obstacleType = 0;
	
	
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
	
	// type
	public int getObstacleType() {
		return obstacleType;
	}
	
	public void setObstacleType(int obstacleType) {
		this.obstacleType = obstacleType;
	}
	
	public Object domObstacleType() {
		String[] myStringArray = {"0", "1"};
		return myStringArray;
	}
	
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
	
	// TODO maybe also wall thickness ...
	
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
	
	// number of flowers worker needs to know to go collecting
	public int getFlowerThresholdWorker() {
		return flowerThreshold;
	}
	
	public void setFlowerThresholdWorker(int flowerThreshold) {
		this.flowerThreshold = flowerThreshold;
	}

	// aliment of flowers
	public int getMinAlimentFlower() {
		return minAlimentFlower;
	}
	
	public void setMinAlimentFlower(int minAlimentFlower) {
		if(minAlimentFlower > 0) {
			this.minAlimentFlower = minAlimentFlower;
		}
	}
	
	public int getMaxAlimentFlower() {
		return maxAlimentFlower;
	}
	
	public void setMaxAlimentFlower(int maxAlimentFlower) {
		if(maxAlimentFlower > 0) {
			this.maxAlimentFlower = maxAlimentFlower;
		}
	}
	
	public int getTime2Create() {
		return time2Create;
	}
	
	public void setTime2Create(int time2Create) {
		if (time2Create > 0) {
			this.time2Create = time2Create;
		}
	}
	
	public int getCost2Create() {
		return cost2Create;
	}
	
	public void setCost2Create(int cost2Create) {
		if (cost2Create > 1) {
			this.cost2Create = cost2Create;
		}
		else {
			this.cost2Create = 1;
		}
	}
	
	public int getTime4Season() {
		return this.time4Season;
	}
	
	public void setTime4Season(int time4Season) {
		if (time4Season > 100) {
			this.time4Season = time4Season;
		}
		else {
			this.time4Season = 100;
		}
	}
	
	public int getTime2Die() {
		return this.time2Die;
	}
	
	public void setTime2Die(int time2Die) {
		if (time2Die > 0) {
			this.time2Die = time2Die;
		}
	}
	
	public void setPercentageDefender (float percentageDefender) {
		if (percentageDefender > 0f && percentageDefender < 100f) {
			this.percentageDefender = percentageDefender;
		}
	}
	
	public float getPercentageDefender () {
		return this.percentageDefender;
	}
	
	public Object domPercentageDefender(){
		return new sim.util.Interval(0.001, 99.999);
	}
	
	public int getTime4Enemies () {
		return this.time4Enemies;
	}
	
	public void setTime4Enemies (int time4Enemies) {
		if (time4Enemies > 0) {
			this.time4Enemies = time4Enemies;
		}
	}
	
	public void setEnemies4Season (int enemies4Season) {
		if (enemies4Season > 0) {
			this.enemies4Season = enemies4Season;
		}
	}
	
	public int getEnemies4Season () {
		return this.enemies4Season;
	}
	
	public void setWorkerMovesBeforeUpdating(int workerMovesBeforeUpdating) {
		this.workerMovesBeforeUpdating = workerMovesBeforeUpdating;
	}
	
	public int getWorkerMovesBeforeUpdating() {
		return this.workerMovesBeforeUpdating;
	}
}
