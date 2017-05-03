package com.sica.behaviour.Tasks;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

public class TaskObserveEnvironment extends TaskOneShot {
	
	private Knowledge knowledge;
	public TaskObserveEnvironment(Knowledge knowledge) {
		this.knowledge = knowledge;
	}
	
	@Override
	public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
		a.observeEnvironment(simState, this.knowledge);
	}
}