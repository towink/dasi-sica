package com.sica.behaviour.Objectives;


import com.sica.behaviour.Tasks.TaskBroadcastKnowledgeToSameType;
import com.sica.behaviour.Tasks.TaskGetToPosition;
import com.sica.behaviour.Tasks.TaskGoToRandomPlaceWithKnowledge;
import com.sica.behaviour.Tasks.TaskObserveEnvironment;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveCollect extends Objective {

	public ObjectiveCollect() {
		super();
		addTaskLast(new TaskDecideWhereToGo());
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		return !bee.thinksKnowsFlowers(SimulationConfig.config().getFlowerThresholdWorker());
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		// when collecting is finished, switch to exploring
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		bee.addObjective(new ObjectiveExplore());
	}
	
	// --- TASKS SPECIFIC TO OBJECTIVE COLLECT ---
	
	// TODO what is the criterion to decide where to go next? at the moment random ...
	private class TaskDecideWhereToGo extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {	}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskLast(new TaskGoToRandomPlaceWithKnowledge(Knowledge.FLOWER, true));
			obj.addTaskLast(new TaskGrabAlimentFromCurrentPos());
		}
	}
	
	private class TaskGrabAlimentFromCurrentPos extends TaskOneShot {
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
	
	private class TaskLeaveAlimentInHive extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
			if(bee.getCarriesAliment()) {
				Int2D location = simState.entities.getObjectLocation(a);
				if (simState.environment.hasTypeAt(location, Knowledge.HIVE)) {
					int aliment = simState.environment.getMetadataAt(location);
					simState.environment.setMetadataAt(location, aliment + 1);
				}
			}
		}
	}
	
	private class TaskObserveEnvironmentObstacleFlower extends TaskOneShot {
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.FLOWER));
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.OBSTACLE));
		}
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {}
	}

}
