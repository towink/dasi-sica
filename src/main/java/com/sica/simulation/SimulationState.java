package com.sica.simulation;

import com.sica.entities.EntityPlacer;
import com.sica.environment.Environment;
import com.sica.environment.EnvironmentModeller;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

public class SimulationState extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;
	
	SimulationConfig config;

	public Environment environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	public SparseGrid2D entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	
	public SimulationState(long seed)
	{ 
		super(seed);
		config = SimulationConfig.getConfig();
	}

	@Override
	public void start()
	{
		super.start();

		environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		EnvironmentModeller.generateHive(environment, SimulationConfig.HIVE_WIDTH, SimulationConfig.HIVE_HEIGHT);
		EnvironmentModeller.generateFlowers(environment, SimulationConfig.NORMAL_FLOWER_WIDTH, SimulationConfig.NORMAL_FLOWER_HEIGHT);
		//EnvironmentModeller.generateRandomObstacles(environment, config.percentageObstacle, this.random);
		EnvironmentModeller.generateWallObstacles(environment, config.numberOfWalls, config.wallLength, this.random);
		
		EntityPlacer.generateBees(entities, schedule, config.getNumBees());
	}
	

	// Getters and Setters
	public SimulationConfig getConfig() {
		return this.config;
	}
	
	public void setNumBees(int numBees) {
		if (numBees > 0) {
			this.config.numBees = numBees;
		}	
	}
	
	public int getNumBees() {
		return this.config.numBees;
	}

	public void setNumFlowers(int numFlowers) {
		if (numFlowers > 0) {
			this.config.numFlowers = numFlowers;
		}
	}
	
	public void setGroupingAffinity(float groupingAffinity) {
		this.config.groupingAffinity = groupingAffinity;
	}
	
	public float getGroupingAffinity() {
		return this.config.groupingAffinity;
	}
	
	public Object domGroupingAffinity(){
		return new sim.util.Interval(0.5, 1.0);
	}

	public void setRadioView(int radioView) {
		if (radioView > 0) {
			this.config.radioView = radioView;
		}
	}

	public void setPercentageObstacle(int percentageObstacle) {
		if (percentageObstacle > 0) {
			this.config.percentageObstacle = percentageObstacle;
		}
		if (percentageObstacle > 100) {
			this.config.percentageObstacle = 100;
		}
	}

}
