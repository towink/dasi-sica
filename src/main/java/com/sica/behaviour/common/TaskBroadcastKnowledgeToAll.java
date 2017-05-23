package com.sica.behaviour.common;

import com.sica.behaviour.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

/**
 * Broadcast knowledge to all nearby agents
 * @author Daniel
 *
 */
public class TaskBroadcastKnowledgeToAll extends TaskOneShot {

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		a.broadcastKnowledgeToAll(simState);
	}

}
