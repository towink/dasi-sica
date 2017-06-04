package com.sica.modules.queenBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

/**
 * Objective that represents the queens intention to avoid enemies.
 * 
 * We consider this objective to be finished when there are no enemies in range.
 * The objective is reached by randomly choosing a location to which the agent escapes.
 * 
 * @author Tobias
 *
 */
public class ObjectiveReturnToHive extends Objective {

	public ObjectiveReturnToHive() {
		Int2D hivePos = new Int2D(
			SimulationConfig.GRID_WIDTH / 2, 
			SimulationConfig.GRID_HEIGHT / 2
		);
		addTaskFirst(new TaskGetToPosition(hivePos));
	}
	
	@Override
	public int getPriority() {
		return HIGH_PRIORITY;
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return simState.entities.getObjectLocation(a).equals(a.getHome());
	}
	

}
