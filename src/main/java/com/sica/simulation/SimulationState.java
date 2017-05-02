package com.sica.simulation;

import com.sica.entities.EntityStorage;
import com.sica.entities.unmovable.ColonySpawner;
import com.sica.entities.unmovable.EnemySpawner;
import com.sica.entities.unmovable.EnvironmentSpawner;
import com.sica.environment.Environment;
import sim.engine.SimState;
import sim.util.Int2D;

public class SimulationState extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;
	
	// I want to access the random generator at many places all over the code
	// is it a good idea to make it global?
	//public static MersenneTwisterFast random;
	
	public SimulationConfig config;

	public Environment environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	public EntityStorage entities = new EntityStorage(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	
	public SimulationState(long seed) { 
		super(seed);
		config = SimulationConfig.config();
	}

	@Override
	public void start() {
		super.start();

		environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		entities = new EntityStorage(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		// make the environment (the environmentspawner doesnt care where it is at)
		entities.addScheduledOnceEntityAt(new EnvironmentSpawner(), new Int2D (), schedule);
		// let the bees out!
		entities.addScheduledOnceEntityAt(new ColonySpawner(), new Int2D (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2), schedule);
		// add also some enemies
		entities.addScheduledOnceEntityAt(new EnemySpawner(), new Int2D(0, 0), schedule);
	}
	
	
	// Getters and Setters
	
	public SimulationConfig getConfig() {
		return this.config;
	}
	
	public EntityStorage getEntities() {
		return this.entities;
	}
	
}
