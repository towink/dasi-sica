package com.sica.behaviour;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationState;

public class TaskBroadcastKnowledge extends TaskOneShot {

	@Override
	public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		bee.broadcastKnowledge(simState);
	}

}
