package com.sica.simulation;

import com.sica.entities.EntityStorage;
import com.sica.entities.Entity.EntityType;
import com.sica.entities.unmovable.ColonySpawner;
import com.sica.entities.unmovable.EnemySpawner;
import com.sica.entities.unmovable.ListEnemySpawner;
import com.sica.entities.unmovable.EnvironmentSpawner;
import com.sica.entities.unmovable.Season;
import com.sica.entities.unmovable.Season.SeasonTypes;
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
		EnemySpawner spawn1 = new EnemySpawner(new Int2D (0,0), EntityType.ENEMY, 4, 10);
		EnemySpawner spawn2 = new EnemySpawner(new Int2D (0, SimulationConfig.GRID_HEIGHT - 1), EntityType.ENEMY, 2, 5);
		EnemySpawner spawn3 = new EnemySpawner(new Int2D (SimulationConfig.GRID_WIDTH - 1, SimulationConfig.GRID_HEIGHT - 1), EntityType.ENEMY, 1, 3);
		EnemySpawner spawn4 = new EnemySpawner(new Int2D (SimulationConfig.GRID_WIDTH - 1, 0), EntityType.ENEMY, 4, 5);
		
		ListEnemySpawner.getListSpawner().putSpawner(spawn1);
		ListEnemySpawner.getListSpawner().putSpawner(spawn2);
		ListEnemySpawner.getListSpawner().putSpawner(spawn3);
		ListEnemySpawner.getListSpawner().putSpawner(spawn4);
		
		for (EnemySpawner spawner : ListEnemySpawner.getListSpawner().getSpawners()) {
			entities.addScheduledRepeatingEntityAt(spawner, spawner.getPosition(), schedule);
		}
		
		// add the seasonController
		entities.addScheduledRepeatingEntityAt(new Season(SeasonTypes.SPRING), new Int2D(), schedule);
	}
	
	
	// Getters and Setters
	
	public SimulationConfig getConfig() {
		return this.config;
	}
	
	public EntityStorage getEntities() {
		return this.entities;
	}
	
}
