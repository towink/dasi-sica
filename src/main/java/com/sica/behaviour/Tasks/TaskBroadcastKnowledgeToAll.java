package com.sica.behaviour.Tasks;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

/**
 * Broadcast knowledge to all nearby agents
 * @author Daniel
 *
 */
public class TaskBroadcastKnowledgeToAll extends TaskOneShot {

	@Override
	public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
		a.broadcastKnowledgeToAll(simState);
	}

}
