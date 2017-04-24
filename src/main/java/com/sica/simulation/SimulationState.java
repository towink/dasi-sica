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
	
	public SimulationState(long seed) { 
		super(seed);
		config = SimulationConfig.config();
	}

	@Override
	public void start() {
		super.start();

		environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		entities = new SparseGrid2D(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		EnvironmentModeller.generateHive(environment, SimulationConfig.HIVE_WIDTH, SimulationConfig.HIVE_HEIGHT);
		EnvironmentModeller.generateFlowers(environment, SimulationConfig.NORMAL_FLOWER_WIDTH, SimulationConfig.NORMAL_FLOWER_HEIGHT);
		
		if(SimulationConfig.WALL_OBSTACLES) {
			EnvironmentModeller.generateWallObstacles(environment, config.numberOfWalls, config.wallLength, this.random);
		}
		else {
			EnvironmentModeller.generateRandomObstacles(environment, config.percentageObstacle, this.random);
		}
		
		EntityPlacer.generateBees(entities, schedule, config.numWorkers);
	}
	

	// Getters and Setters
	
	public SimulationConfig getConfig() {
		return this.config;
	}
	
}
