package com.sica.behaviour.Objectives;

import com.sica.behaviour.Tasks.TaskBroadcastKnowledge;
import com.sica.behaviour.Tasks.TaskGetToPosition;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.data.IterableSet;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveCollect extends Objective {

	public ObjectiveCollect() {
		super();
		addTask(new TaskDecideWhereToGo());
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
		private Int2D decision;
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
			IterableSet<Int2D> flowerPositions = bee.getKnowledgeMap().getKnowledgeOf(Knowledge.FLOWER);
			if(flowerPositions.isEmpty()) {
				throw new IllegalStateException("no flower pos known");
			}
			decision = flowerPositions.iterator().next();
			//TODO make randomness great again
			//int i = simState.random.nextInt(flowerPositions.size());
			//decision = (Int2D) flowerPositions.toArray()[i];
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			addTask(new TaskGetToPosition(decision));
			addTask(new TaskGrabAlimentFromCurrentPos());
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
			obj.addTask(new TaskObserveEnvironment());
			// only go back to hive if we really grabbed aliment
			if(bee.getCarriesAliment()) {
				obj.addTask(new TaskGetToPosition(a.getHome()));
				obj.addTask(new TaskLeaveAlimentInHive());
				obj.addTask(new TaskBroadcastKnowledge());
			}
			obj.addTask(new TaskDecideWhereToGo());
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
	
	private class TaskObserveEnvironment extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			a.observeEnvironment(simState, Knowledge.OBSTACLE);
			a.observeEnvironment(simState, Knowledge.FLOWER);
		}
	}

}
