package com.sica.entities.unmovable;

import com.sica.entities.Entity;
import com.sica.environment.EnvironmentModeller;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

public class EnvironmentSpawner extends Entity {
	private static final long serialVersionUID = -1459407476985662028L;

	public EnvironmentSpawner () {
		super(EntityType.ENVIRONMENT_SPAWNER);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doStep(SimulationState simState) {
		EnvironmentModeller.randomlyGenerateFlowers(
				simState.environment,
				simState.config.getNumFlowers(),
				simState.config.getMinAlimentFlower(),
				simState.config.getMaxAlimentFlower(),
				simState.random);
		
		// put some obstacles around
		if(SimulationConfig.config().getObstacleType() == SimulationConfig.WALL_OBSTACLES) {
			EnvironmentModeller.generateWallObstacles(simState.environment, simState.config.getNumberOfWalls(), simState.config.getWallLength(), simState.random);
		}
		else {
			EnvironmentModeller.generateRandomObstacles(simState.environment, simState.config.getPercentageObstacle(), simState.random);
		}
	}

}
