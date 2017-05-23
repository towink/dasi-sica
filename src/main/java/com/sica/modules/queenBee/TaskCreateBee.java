package com.sica.modules.queenBee;

import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;


public class TaskCreateBee extends TaskOneShot {
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		QueenDrools bee = (QueenDrools) a;
		simState.environment.setMetadataAt(bee.getLocation(), bee.getAvailableFood() - simState.getConfig().getCost2Create());
		bee.resetCount();
		bee.createBee(simState);
	}


}