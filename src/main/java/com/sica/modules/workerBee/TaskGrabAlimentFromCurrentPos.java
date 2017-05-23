package com.sica.modules.workerBee;

import com.sica.behaviour.Objectives.Objective;
import com.sica.behaviour.Tasks.TaskBroadcastKnowledgeToSameType;
import com.sica.behaviour.Tasks.TaskGetToPosition;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class TaskGrabAlimentFromCurrentPos extends TaskOneShot {
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		Int2D pos = simState.entities.getObjectLocation(a);
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
		if(bee.getCarriesAliment()) {
			obj.addTaskLast(new TaskGetToPosition(a.getHome()));
			obj.addTaskLast(new TaskLeaveAlimentInHive());
			obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
		}
		obj.addTaskLast(new TaskDecideWhereToGo());
	}
}