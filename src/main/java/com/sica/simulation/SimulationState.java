package com.sica.simulation;

import com.sica.entities.EntityStorage;
import com.sica.entities.unmovable.ColonySpawner;
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
	
	public int aliment;

	public Environment environment = Environment.getNewEnvironment(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	public EntityStorage entities = new EntityStorage(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	
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
		entities = new EntityStorage(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		
		// make the environment
		entities.addScheduledOnceEntityAt(new EnvironmentSpawner(), new Int2D (), schedule);
		// let the bees out!
		entities.addScheduledOnceEntityAt(new ColonySpawner(), new Int2D (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2), schedule);
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
