package com.sica.modules.queenBee;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.agents.Agent;
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

}
