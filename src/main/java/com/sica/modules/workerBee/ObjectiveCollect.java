package com.sica.modules.workerBee;


import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

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
		bee.addObjective(new ObjectiveFindUnexploredTerrain());
	}


}
