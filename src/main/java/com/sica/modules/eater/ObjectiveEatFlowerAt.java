package com.sica.modules.eater;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveEatFlowerAt extends Objective {
	private boolean isFinished = false;

	public ObjectiveEatFlowerAt(Int2D position) {
		this.addTaskLast(new TaskGetToPosition(position));
		this.addTaskLast(new TaskEatFlower());
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return isFinished;
	}
	
	private class TaskEatFlower extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			Int2D pos = simState.entities.getObjectLocation(a);
			if (simState.environment.hasTypeAt(pos, Knowledge.FLOWER))
				simState.environment.set(pos, Knowledge.UNKNOWN);
			isFinished = true;
		}
	}
}
