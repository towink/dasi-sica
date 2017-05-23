package com.sica.behaviour;

import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

public abstract class TaskFiniteTimes extends Task {

	private int times;
	
	/**
	 * Create a task to be excuted n times
	 * @param n
	 */
	public TaskFiniteTimes(int n) {
		this.times = n;
	}
	
	@Override
	public void interactWith(Agent a, SimulationState simState) {
		this.interactOneTime(a, simState);
		times--;
	}
	
	/**
	 * Interact one time with this task
	 * @param a
	 * @param simState
	 */
	public abstract void interactOneTime(Agent a, SimulationState simState);

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return times == 0;
	}

}
