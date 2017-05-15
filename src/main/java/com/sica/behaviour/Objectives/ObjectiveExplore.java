package com.sica.behaviour.Objectives;

import com.sica.behaviour.Tasks.TaskBroadcastKnowledgeToSameType;
import com.sica.behaviour.Tasks.TaskMoveRandomly;
import com.sica.behaviour.Tasks.TaskObserveEnvironment;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

public class ObjectiveExplore extends Objective {

	public ObjectiveExplore() {
		this.addTaskLast(new TaskMoveThenObserve());
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		return bee.thinksKnowsFlowers(SimulationConfig.config().getFlowerThresholdWorker());
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		// go collecting when we are done exploring!
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		bee.addObjective(new ObjectiveCollect());
	}
	
	// --- TASKS SPECIFIC TO OBJECTIVE EXPLORE ---

	private class TaskObserveFlowersObstacles extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {}
		
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.FLOWER));
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.OBSTACLE));
			if(a.getKnowledgeMap().pollNewKnowledge()) {
				obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
			}
			addTaskLast(new TaskMoveThenObserve());
		}
	}
	
	private class TaskMoveThenObserve extends TaskMoveRandomly {
		public TaskMoveThenObserve() {
			super(SimulationConfig.config().getWorkerMovesBeforeUpdating());
		}

		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			addTaskLast(new TaskObserveFlowersObstacles());
		}
	}

}
