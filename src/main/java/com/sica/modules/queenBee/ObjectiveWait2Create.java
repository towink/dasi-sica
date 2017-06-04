package com.sica.modules.queenBee;

import com.sica.behaviour.Objective;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

public class ObjectiveWait2Create extends Objective {

	@Override
	public int getPriority() {
		return Objective.VERY_LOW_PRIORITY;
	}

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

}
