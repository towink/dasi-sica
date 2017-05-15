package com.sica.behaviour.Tasks;

import com.sica.entities.agents.Agent;
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
public class TaskMoveTowardsPosition extends TaskFiniteTimes {

	private Int2D destination;

	public TaskMoveTowardsPosition(Int2D destination, int maxSteps) {
		super(maxSteps);
		this.destination = destination;
	}
	

	@Override
	public void interactOneTime(Agent a, SimulationState simState) {
		if (!a.followCurrentPath(simState))
			a.computePath(simState, destination);
	}
	


	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		// if the destination (for some strange reason) is an obstacle then
		// we say that this objective is immediately finished
		return super.isFinished(a, simState) || simState.entities.getObjectLocation(a).equals(this.destination)
				|| !a.canMoveTo(destination, simState, SimulationConfig.ENV_MODE);
	}



}
