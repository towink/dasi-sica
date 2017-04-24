package com.sica.simulation;

import com.sica.entities.EntityPlacer;
import com.sica.environment.Environment;
import com.sica.environment.EnvironmentModeller;

import ec.util.MersenneTwisterFast;
import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;

public class SimulationState extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;
	
	// I want to access the random generator at many places all over the code
	// is it a good idea to make it global?
	//public static MersenneTwisterFast random;
	
	SimulationConfig config;

	public Environment environment = new Environment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	public SparseGrid2D entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	
	public SimulationState(long seed)
	{ 
		super(seed);
		config = new SimulationConfig();
		//SimulationState.random = this.random;
	}

	@Override
	public void start()
	{
		super.start();

		environment = new Environment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		// place the hive
		EnvironmentModeller.generateHive(environment, SimulationConfig.HIVE_WIDTH, SimulationConfig.HIVE_HEIGHT);
		
		// let some flowers grow
		//EnvironmentModeller.generateFlowers(environment, SimulationConfig.NORMAL_FLOWER_WIDTH, SimulationConfig.NORMAL_FLOWER_HEIGHT);
		EnvironmentModeller.randomlyGenerateFlowers(
				environment,
				20,
				config.getMinAlimentFlower(),
				config.getMaxAlimentFlower(),
				random);
		
		// put some obstacles around
		//EnvironmentModeller.generateRandomObstacles(environment, config.percentageObstacle, this.random);
		EnvironmentModeller.generateWallObstacles(
				environment,
				config.getNumberOfWalls(),
				config.getWallLength(),
				random);
		
		// let the bees out!
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

	public void setNumFlowers(int numFlowers) {
		if (numFlowers > 0) {
			this.config.numFlowers = numFlowers;
		}
	}
	
	public void setGroupingAffinity(float groupingAffinity) {
		this.config.groupingAffinity = groupingAffinity;
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
