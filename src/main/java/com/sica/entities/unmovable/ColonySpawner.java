package com.sica.entities.unmovable;

import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ColonySpawner extends Entity {
	private static final long serialVersionUID = -3977351065123585649L;
	
	
	public ColonySpawner() {
		super(EntityType.BEE_SPAWNER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doStep(SimulationState simState) {
		Int2D colonyPosition = simState.entities.getObjectLocation(this);
		//set the hive block in the center
		simState.environment.set(colonyPosition, Knowledge.HIVE);
		//generate the starting bees
		EntityPlacer.generateWorkers(simState.entities, simState.schedule, SimulationConfig.config().getNumWorkers(), colonyPosition);
		EntityPlacer.generateQueen(simState.entities, simState.schedule, colonyPosition);
	}

}
