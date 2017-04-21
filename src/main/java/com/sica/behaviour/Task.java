package com.sica.behaviour;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

public interface Task {
	
	/**
	 * Make the agent perform this task. All logic needed for the execution should be stored within the task, as the
	 * agent might decide to change objectives and other tasks could modify its state before this one is completed
	 * @param a
	 * @param simState
	 */
	public void interactWith(ObjectiveDrivenAgent a, SimulationState simState);
	
	/**
	 * Called by the Objective when the task is finished. Might be useful to set up stuff
	 * @param a
	 * @param obj A reference to the objective this task is part of
	 */
	public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState);
	
	/**
	 * @return true if the task has finished executing
	 */
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState);
	
}
