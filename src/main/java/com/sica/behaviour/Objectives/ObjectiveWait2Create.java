package com.sica.behaviour.Objectives;

import com.sica.behaviour.Tasks.Task;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.QueenDrools;
import com.sica.simulation.SimulationState;

public class ObjectiveWait2Create extends Objective {

	public ObjectiveWait2Create() {
		super();
		addTaskFirst(new TaskWait2Create());
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		if (!taskQueue.isEmpty()) {
			return taskQueue.peek().isFinished(a, simState);
		}
		
			return true;
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		QueenDrools bee = (QueenDrools) a;
		bee.waiting = false;
	}


	// TASKS TO THIS OBJECTIVE
	private class TaskWait2Create extends Task {

		@Override
		public void interactWith(Agent a, SimulationState simState) {
			QueenDrools bee = (QueenDrools) a;
			bee.increaseCount();
		}
		
		@Override
		public boolean isFinished(Agent a, SimulationState simState) {
			QueenDrools bee = (QueenDrools) a;
			return (simState.getConfig().getTime2Create() <= bee.getCount()) && 
					(bee.getAvailableFood() >= simState.getConfig().getCost2Create());
		}

	}

}
