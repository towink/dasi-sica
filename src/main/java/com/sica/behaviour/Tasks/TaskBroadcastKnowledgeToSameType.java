package com.sica.behaviour.Tasks;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationState;

/**
 * Broadcast knowledge to all agents of the same type
 * @author Tobias, Daniel
 *
 */
public class TaskBroadcastKnowledgeToSameType extends TaskOneShot {

	@Override
	public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		a.broadcastKnowledgeToType(simState,  bee.getType());
	}

}
