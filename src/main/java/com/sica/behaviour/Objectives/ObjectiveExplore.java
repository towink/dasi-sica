package com.sica.behaviour.Objectives;

import com.sica.behaviour.Tasks.TaskBroadcastKnowledge;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.movement.Direction;

public class ObjectiveExplore extends Objective {

	public ObjectiveExplore() {
		taskQueue.add(new TaskMoveRandomly());
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
		public void interactWithOneShot(Agent a, SimulationState simState) {
			a.observeEnvironment(simState, Knowledge.OBSTACLE);
			a.observeEnvironment(simState, Knowledge.FLOWER);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
			if(bee.getKnowledgeMap().pollNewKnowledge()) {
				obj.addTask(new TaskBroadcastKnowledge());
			}
			addTask(new TaskMoveRandomly());
		}
	}
	
	private class TaskMoveRandomly extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			// randomly pick a direction and move
			Direction randomDir = Direction.values()[simState.random.nextInt(Direction.values().length)];
			a.moveInDirection(randomDir, simState, SimulationConfig.ENV_MODE);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			addTask(new TaskObserveFlowersObstacles());
		}
	}

}
