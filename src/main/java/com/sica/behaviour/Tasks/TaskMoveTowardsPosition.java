package com.sica.behaviour.Tasks;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

/**
 * Move towards a position a number of steps.
 * Useful if you want to travel great distances and also for example
 * broadcast knowledge along the way
 * @author Daniel
 *
 */
public class TaskMoveTowardsPosition extends Task {

	private Int2D destination;
	private int maxSteps;
	private int currSteps;

	public TaskMoveTowardsPosition(Int2D destination, int maxSteps) {
		this.destination = destination;
		this.maxSteps = maxSteps;
		this.currSteps = 0;
	}
	

	@Override
	public void interactWith(ObjectiveDrivenAgent agent, SimulationState simState) {
		if (!agent.followCurrentPath(simState))
			agent.computePath(simState, destination);
		this.currSteps++;
	}

	@Override
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		// if the destination (for some strange reason) is an obstacle then
		// we say that this objective is immediately finished
		return simState.entities.getObjectLocation(a).equals(this.destination)
			|| !a.canMoveTo(destination, simState, SimulationConfig.ENV_MODE)
			|| this.currSteps >= this.maxSteps;
	}
}
