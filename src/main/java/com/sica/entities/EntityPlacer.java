package com.sica.entities;

import com.sica.entities.agents.AgentFactory;
import com.sica.entities.agents.DroolsBee;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.entities.agents.WorkerBee;
import com.sica.simulation.SimulationConfig;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class EntityPlacer {

	@Deprecated
	public static void generateBees(SparseGrid2D entities, Schedule schedule, int numBees, Int2D home) {
		Entity agent;
		
		for (int x = 0; x < 0; x++) {
			agent = AgentFactory.getAgent(AgentFactory.WORKER_BEE, home);
			entities.setObjectLocation(agent, 50, 50);
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
			entities.setObjectLocation(agent, entities.getWidth()/2, entities.getHeight()/2);
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
			agent = AgentFactory.getAgent(AgentFactory.WORKER_BEE, home);
			// maybe the next 2 lines should be included in the factory method somehow??
			entities.setObjectLocation(agent, home.x, home.y);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
	}
	
	/**
	 * Inserts defenders in the grid.
	 * @param entities
	 * @param schedule
	 * @param numDefenders
	 */
	public static void generateDefenders(SparseGrid2D entities, Schedule schedule, int numDefenders) {
		throw new NotImplementedException();
	}
	
	// TODO methods for enemies, queen ...
	
	
}
