package com.sica.modules.queenBee;

import com.sica.behaviour.Objective;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

public class ObjectiveCreateBee extends Objective {

	@Override
	public int getPriority() {
		return Objective.LOW_PRIORITY;
	}

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

}
