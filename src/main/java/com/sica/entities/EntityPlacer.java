package com.sica.entities;

import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.AgentFactory;
import com.sica.entities.agents.DroolsBee;
import com.sica.entities.agents.QueenDrools;
import com.sica.entities.agents.WorkerBee;
import com.sica.simulation.SimulationConfig;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class EntityPlacer {

	@Deprecated
	public static void generateBees(SparseGrid2D entities, Schedule schedule, int numBees, Int2D home) {
		Entity agent;
		
		for (int x = 0; x < 0; x++) {
			agent = AgentFactory.getAgent(EntityType.OBJECTIVE_DRIVEN_WORKER);
			entities.setObjectLocation(agent, home.x, home.y);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		for (int x = 0; x < 0; x++) {
			agent = new DroolsBee();
			entities.setObjectLocation(agent, 20,20);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		
		for(int x = 0; x < numBees; x++)
		{
			agent = new WorkerBee();		
			entities.setObjectLocation(agent, home.x, home.y);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
	}
	
	/**
	 * Places worker bees in the middle of the grid.
	 * @param entities The grid holding the bees
	 * @param schedule
	 * @param numWorkers Number of workers to be inserted.
	 */
	public static void generateWorkers(SparseGrid2D entities, Schedule schedule, int numWorkers, Int2D home) {
		Entity agent;
		for (int x = 0; x < numWorkers; x++) {
			agent = AgentFactory.getAgent(EntityType.OBJECTIVE_DRIVEN_WORKER);
			// maybe the next 2 lines should be included in the factory method somehow??
			entities.setObjectLocation(agent, home);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
	}
	
	/**
	 * Places worker bees in the middle of the grid.
	 * @param entities The grid holding the bees
	 * @param schedule
	 * @param numWorkers Number of workers to be inserted.
	 */
	public static void generateWorkersAfter(SparseGrid2D entities, Schedule schedule, int numWorkers, Int2D home) {
		Entity agent;
		for (int x = 0; x < numWorkers; x++) {
			agent = AgentFactory.getAgent(EntityType.OBJECTIVE_DRIVEN_WORKER);
			// maybe the next 2 lines should be included in the factory method somehow??
			entities.setObjectLocation(agent, home);
			schedule.scheduleRepeating(schedule.getTime(), 0, agent, 1);
		}
	}
	
	/**
	 * Inserts defenders in the grid.
	 * @param entities
	 * @param schedule
	 * @param numDefenders
	 * @param home
	 */
	public static void generateDefenders(SparseGrid2D entities, Schedule schedule, int numDefenders, Int2D home) {
		Entity agent;
		for (int x = 0; x < numDefenders; x++) {
			agent = AgentFactory.getAgent(EntityType.DEFENDER_BEE);
			// maybe the next 2 lines should be included in the factory method somehow??
			entities.setObjectLocation(agent, home);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
	}
	
	/**
	 * Inserts defenders in the grid.
	 * @param entities
	 * @param schedule
	 * @param home
	 */
	public static void generateQueen(SparseGrid2D entities, Schedule schedule, Int2D home) {
		if (queenInHive(entities, home)){
			return ;
		}
		
		Entity agent = AgentFactory.getAgent(EntityType.QUEEN);
		entities.setObjectLocation(agent, home);
		schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
	}
	
	private static boolean queenInHive (SparseGrid2D entities, Int2D hivePos) {
		boolean result = false;
		int minX = (int) (hivePos.getX() - SimulationConfig.HIVE_WIDTH/2);
		int minY = (int) (hivePos.getY() - SimulationConfig.HIVE_HEIGHT/2);
		int maxX = (int) (hivePos.getX() + SimulationConfig.HIVE_WIDTH/2);
		int maxY = (int) (hivePos.getY() + SimulationConfig.HIVE_HEIGHT/2);
		
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				Bag items = entities.getObjectsAtLocation(x, y);
				if (items != null) {
					for (Object a: items) {
						if (a.getClass() == QueenDrools.class) {
							result = true;
							break;
						}
					}
				}
			}
		}
		
		return result;
	}

	public static void generateEnemies(EntityStorage entities, Schedule schedule, int numEnemies, Int2D objectLocation) {
		Entity agent;
		for (int x = 0; x < numEnemies; x++) {
			agent = AgentFactory.getAgent(EntityType.SIMPLE_ENEMY);
			// maybe the next 2 lines should be included in the factory method somehow??
			entities.setObjectLocation(agent, objectLocation);
			schedule.scheduleRepeating(schedule.getTime(), 0, agent, 1);
		}
	}
	
	// TODO methods for enemies
	
	
}
