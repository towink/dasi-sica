package com.sica.behaviour;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

/**
 * Abstract class implementing Task which can be used to create tasks that represent one single action that is done in
 * one step of the Mason simulation.
 * 
 * @author Tobias
 *
 */
public abstract class TaskOneShot extends Task {
	
	/**
	 * Implement the task in this method.
	 * @param a
	 * @param simState
	 */
	public abstract void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState);
	
	private boolean finished = false;

	@Override
	public final void interactWith(ObjectiveDrivenAgent a, SimulationState simState) {
		interactWithOneShot(a, simState);
		finished = true;
	}

	@Override
	public final boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		return finished;
	}

}
