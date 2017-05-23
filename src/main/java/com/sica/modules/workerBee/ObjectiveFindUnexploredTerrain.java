package com.sica.modules.workerBee;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class ObjectiveFindUnexploredTerrain extends Objective {

	@Override
	public void onFinished(Agent a, SimulationState simState) {
		Int2D where = new Int2D(
				simState.random.nextInt(SimulationConfig.GRID_WIDTH),
				simState.random.nextInt(SimulationConfig.GRID_HEIGHT));
		
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		bee.addObjective(new ObjectiveExplore(where));
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return true;
	}

}
