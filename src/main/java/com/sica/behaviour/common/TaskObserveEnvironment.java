package com.sica.behaviour.common;

import com.sica.behaviour.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

public class TaskObserveEnvironment extends TaskOneShot {
	
	private Knowledge knowledge;
	public TaskObserveEnvironment(Knowledge knowledge) {
		this.knowledge = knowledge;
	}
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		a.observeEnvironment(simState, this.knowledge);
	}
}