package com.sica.behaviour.common;

import com.sica.behaviour.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.modules.workerBee.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class TaskBroadcastForgetKnowledgeToSameType extends TaskOneShot {

	private Int2D pos;
	
	public TaskBroadcastForgetKnowledgeToSameType(Int2D pos) {
		this.pos = pos;
	}
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		a.broadcastForgetKnowledgeToType(simState,  bee.getType(), pos);
	}

}
