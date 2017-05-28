package com.sica.modules.workerBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskBroadcastKnowledgeToSameType;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class TaskLeaveAlimentInHive extends TaskOneShot {
	
	boolean alimentLeft = false;
	
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		super.endTask(a, obj, simState);
		if (alimentLeft)
			obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
	}

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		if(bee.getCarriesAliment()) {
			Int2D location = simState.entities.getObjectLocation(a);
			if (simState.environment.hasTypeAt(location, Knowledge.HIVE)) {
				int aliment = simState.environment.getMetadataAt(location);
				simState.environment.setMetadataAt(location, aliment + 1);
				alimentLeft = true;
			}
		}
	}
}