package com.sica.entities.unmovable;

import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ColonySpawner extends Entity {
	private static final long serialVersionUID = -3977351065123585649L;
	
	SimulationState simState;
	
	public ColonySpawner() {
		super(EntityType.BEE_SPAWNER);
	}

	@Override
	public void doStep(SimulationState simState) {
		Int2D colonyPosition = simState.entities.getObjectLocation(this);
		//set the hive block in the center
		simState.environment.set(colonyPosition, Knowledge.HIVE);
		//generate the starting bees
		EntityPlacer.deployEntities(EntityType.OBJECTIVE_DRIVEN_WORKER, simState.entities, colonyPosition, simState.schedule, SimulationConfig.config().getNumWorkers());
		EntityPlacer.deployEntity(EntityType.QUEEN, simState.entities, colonyPosition, simState.schedule);
		EntityPlacer.deployEntities(EntityType.OBJECTIVE_DRIVEN_DEFENDER, simState.entities, colonyPosition, simState.schedule, SimulationConfig.config().getNumWorkers());
		this.simState = simState;
	}
	
	public int getWorkers() {
		return this.simState.entities.getNumberOf(Entity.EntityType.OBJECTIVE_DRIVEN_WORKER);
	}
	
	public int getDefenders() {
		return this.simState.entities.getNumberOf(Entity.EntityType.DEFENDER_BEE);
	}	

}
