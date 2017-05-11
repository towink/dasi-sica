package com.sica.behaviour.Objectives;

import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.QueenDrools;
import com.sica.simulation.SimulationState;

public class ObjectiveCreateBee extends Objective {

	public ObjectiveCreateBee() {
		super();
		addTaskFirst(new TaskCreateBee());
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		if (!taskQueue.isEmpty()) {
			return taskQueue.peek().isFinished(a, simState);
		}
		return true;
	}
	
	
	// TASKS TO THIS OBJECTIVE
	private class TaskCreateBee extends TaskOneShot {
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			QueenDrools bee = (QueenDrools) a;
			simState.environment.setMetadataAt(bee.getLocation(), bee.getAvailableFood() - simState.getConfig().getCost2Create());
			bee.resetCount();
			bee.createBee(simState);
		}


	}

}
