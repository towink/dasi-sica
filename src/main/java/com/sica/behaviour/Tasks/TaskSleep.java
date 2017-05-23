package com.sica.behaviour.Tasks;

import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

public class TaskSleep extends TaskFiniteTimes {

	public TaskSleep(int n) {
		super(n);
	}

	@Override
	public void interactOneTime(Agent a, SimulationState simState) {
		// just don't do anything :-)
	}

}
