package com.sica.modules.queenBee;

import com.sica.behaviour.Tasks.Task;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

public class TaskWait2Create extends Task {

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