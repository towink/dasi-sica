package com.sica.modules.workerBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskBroadcastForgetKnowledgeToSameType;
import com.sica.behaviour.common.TaskBroadcastSpecificKnowledgeToSameType;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class TaskGrabAlimentFromCurrentPos extends TaskOneShot {
	private Int2D pos;
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		pos = simState.entities.getObjectLocation(a);
		boolean gotAliment = simState.environment.getAlimentAt(pos);
		bee.setCarriesAliment (gotAliment);
		if (!gotAliment) {
			bee.getKnowledgeMap().updateKnowledge(pos, simState.environment.getKnowledgeAt(pos));
		}
	}
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		obj.addTaskLast(new TaskObserveEnvironmentObstacleFlower());	
		// only go back to hive if we really grabbed aliment
		//obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
		if(bee.getCarriesAliment()) {
			obj.addTaskLast(new TaskGetToPosition(a.getHome()));
			obj.addTaskLast(new TaskLeaveAlimentInHive());
			//obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
			obj.addTaskLast(new TaskBroadcastSpecificKnowledgeToSameType(pos, Knowledge.FLOWER));
		} else {
			obj.addTaskLast(new TaskBroadcastForgetKnowledgeToSameType(pos));
		}
		obj.addTaskLast(new TaskDecideWhereToGo());
	}
}