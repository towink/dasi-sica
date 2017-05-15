package com.sica.behaviour.Tasks;

import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.movement.Direction;

public class TaskMoveRandomly extends TaskFiniteTimes {
	
	/**
	 * Move n times before ending the task
	 * @param n
	 */
	public TaskMoveRandomly(int n) {
		super(n);
	}

	@Override
	public void interactOneTime(Agent a, SimulationState simState) {
		Direction randomDir = Direction.values()[simState.random.nextInt(Direction.values().length)];
		a.moveInDirection(randomDir, simState, SimulationConfig.ENV_MODE);
	}
}
