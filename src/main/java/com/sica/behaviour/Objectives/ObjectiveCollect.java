package com.sica.behaviour.Objectives;

import java.util.Collection;

import com.sica.behaviour.Tasks.TaskBroadcastKnowledge;
import com.sica.behaviour.Tasks.TaskGetToPosition;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveCollect extends Objective {

	public ObjectiveCollect() {
		super();
		addTask(new TaskDecideWhereToGo());
	}
	
	@Override
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		return !bee.thinksKnowsFlowers(SimulationConfig.config().getFlowerThresholdWorker());
	}
	
	@Override
	public void onFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		// when collecting is finished, switch to exploring
		a.addObjective(new ObjectiveExplore());
	}
	
	// --- TASKS SPECIFIC TO OBJECTIVE COLLECT ---
	
	// TODO what is the criterion to decide where to go next? at the moment random ...
	private class TaskDecideWhereToGo extends TaskOneShot {
		private Int2D decision;
		@Override
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			Collection<Int2D> flowerPositions = a.getKnowledgeMap().getKnowledgeOf(Knowledge.FLOWER);
			if(flowerPositions.isEmpty()) {
				throw new IllegalStateException("no flower pos known");
			}
			int i = simState.random.nextInt(flowerPositions.size());
			decision = (Int2D) flowerPositions.toArray()[i];
		}
		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
			addTask(new TaskGetToPosition(decision));
			addTask(new TaskGrabAlimentFromCurrentPos());
		}
	}
	
	private class TaskGrabAlimentFromCurrentPos extends TaskOneShot {
		@Override
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
			Int2D pos = simState.entities.getObjectLocation(a);
			bee.setCarriesAliment (simState.environment.getAlimentAt(pos));
		}
		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
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
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
			if(bee.getCarriesAliment()) {
				simState.aliment++;
			}
		}
	}
	
	private class TaskObserveEnvironment extends TaskOneShot {
		@Override
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			a.observeEnvironment(simState, Knowledge.OBSTACLE);
			a.observeEnvironment(simState, Knowledge.FLOWER);
			a.observeEnvironment(simState, Knowledge.EMPTY);
		}
	}

}
