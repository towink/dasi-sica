package com.sica.simulation;

import com.sica.entities.EntityPlacer;
import com.sica.environment.Environment;
import com.sica.environment.EnvironmentModeller;

import sim.engine.SimState;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class SimulationState extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;
	
	// I want to access the random generator at many places all over the code
	// is it a good idea to make it global?
	//public static MersenneTwisterFast random;
	
	SimulationConfig config;
	
	public int aliment;

	public Environment environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	public SparseGrid2D entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	
	public SimulationState(long seed) { 
		super(seed);
		config = SimulationConfig.config();
		config.sim = this;
		aliment = 0;
	}

	@Override
	public void start() {
		super.start();

		environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		// place the hive
		EnvironmentModeller.generateHive(environment, SimulationConfig.HIVE_WIDTH, SimulationConfig.HIVE_HEIGHT);
		
		// let some flowers grow
		//EnvironmentModeller.generateFlowers(environment, SimulationConfig.NORMAL_FLOWER_WIDTH, SimulationConfig.NORMAL_FLOWER_HEIGHT);
		EnvironmentModeller.randomlyGenerateFlowers(
				environment,
				config.getNumFlowers(),
				config.getMinAlimentFlower(),
				config.getMaxAlimentFlower(),
				random);
		
		// put some obstacles around
		if(SimulationConfig.config().getObstacleType() == SimulationConfig.WALL_OBSTACLES) {
			EnvironmentModeller.generateWallObstacles(environment, config.numberOfWalls, config.wallLength, this.random);
		}
		else {
			EnvironmentModeller.generateRandomObstacles(environment, config.percentageObstacle, this.random);
		}
		
		// let the bees out!
		EntityPlacer.generateBees(entities, schedule, config.numBees, 
									new Int2D (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
		EntityPlacer.generateWorkers(entities, schedule, config.numWorkers, 
									new Int2D (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
		EntityPlacer.generateQueen(entities, schedule, new Int2D (SimulationConfig.GRID_WIDTH/2, 
																	SimulationConfig.GRID_HEIGHT/2));
	}
	

	public void decreaseAliment (int value) {
		aliment -= value;
		if (aliment < 0) {
			aliment = 0;
		}
	}
	
	// Getters and Setters
	
	public SimulationConfig getConfig() {
		return this.config;
	}
	
}
