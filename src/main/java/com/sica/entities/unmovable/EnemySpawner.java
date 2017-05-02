package com.sica.entities.unmovable;

import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationState;

public class EnemySpawner extends Entity {
	private static final long serialVersionUID = -3430485150816800473L;

	public EnemySpawner() {
		super(EntityType.ENEMY_SPAWNER);
	}

	@Override
	public void doStep(SimulationState simState) {
		EntityPlacer.generateEnemies(simState.entities, simState.schedule, 30, simState.entities.getObjectLocation(this));
	}

}
