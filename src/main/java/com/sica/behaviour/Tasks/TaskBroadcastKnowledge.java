package com.sica.behaviour.Tasks;

import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationState;

public class TaskBroadcastKnowledge extends TaskOneShot {

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		a.broadcastKnowledgeToType(simState,  bee.getType());
	}

}
