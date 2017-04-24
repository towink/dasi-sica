package com.sica.entities;

import com.sica.behaviour.ObjectiveExplore;
import com.sica.entities.agents.AgentFactory;
import com.sica.entities.agents.DroolsBee;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.entities.agents.WorkerBee;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;

public class EntityPlacer {

	
	public static void generateBees(SparseGrid2D entities, Schedule schedule, int numBees) {
		Entity agent;
		
		for (int x = 0; x < 10; x++) {
			agent = AgentFactory.getAgent(AgentFactory.WORKER_BEE);
			((ObjectiveDrivenWorkerBee) agent).addObjective(new ObjectiveExplore());
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
}
