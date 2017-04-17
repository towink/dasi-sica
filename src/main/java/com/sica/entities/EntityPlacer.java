package com.sica.entities;

import com.sica.entities.agents.DroolsBee;
import com.sica.entities.agents.WorkerBee;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;

public class EntityPlacer {

	
	public static void generateBees(SparseGrid2D entities, Schedule schedule, int numBees) {
		// TODO: Here, we can implement factory pattern
		
		Entity agent;
		
		for (int x = 0; x < 100; x++) {
			agent = new DroolsBee();
			entities.setObjectLocation(agent, 20,20);
			//schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		/*
		 * Test A*
		 * First: Create a map with a lot of obstacles
		 * Second: Put a random objetive where agents want to go
		 * Third: Every agent calculate A*
		 * 
		 * In addition, we check how long it takes to run
		 */
		
		/*Map map = new Map(GRID_WIDTH, GRID_HEIGHT);
		for (int i = 0; i < GRID_HEIGHT - 1; i++) {
			map.modifyMap(20, i, Type.OBSTACLE);
		}
		
		for (int i = 21; i < GRID_WIDTH/2+30; i++) {
			map.modifyMap(i, GRID_HEIGHT/2 + 20, Type.OBSTACLE);
		}*/
		
		//long start = System.currentTimeMillis();
		
		for(int x = 0; x < numBees; x++)
		{
			agent = new WorkerBee();
			
			/*// Testing A* {
			agent.setObjective(5, 5);
			Point aux = new Point (GRID_WIDTH/2, GRID_HEIGHT/2);
			
			agent.calculatePath(aux);
			// } testing A**/
			
			entities.setObjectLocation(agent, entities.getWidth()/2, entities.getHeight()/2);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		//System.out.println("Time of calculate " + controller.getNumBees() + " A*: " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
		
	}
}
