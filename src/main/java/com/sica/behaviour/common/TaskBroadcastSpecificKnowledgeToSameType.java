package com.sica.behaviour.common;

import com.sica.behaviour.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.modules.workerBee.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class TaskBroadcastSpecificKnowledgeToSameType extends TaskOneShot {
	
	private Int2D pos;
	private Knowledge knowledge;
	
	public TaskBroadcastSpecificKnowledgeToSameType(Int2D pos, Knowledge knowledge) {
		this.pos = pos;
		this.knowledge = knowledge;
	}

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		a.broadcastKnowledgeToType(simState,  bee.getType(), pos, knowledge);
	}

}
