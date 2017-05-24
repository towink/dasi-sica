package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.entities.Entity.EntityType;
import com.sica.modules.defenderBee.DefenderBee;
import com.sica.modules.defenderBee.ObjectiveDrivenDefenderBee;
import com.sica.modules.defenderBee.ObjectiveExploreTrench;
import com.sica.modules.enemy.ObjectiveDrivenEnemy;
import com.sica.modules.enemy.ObjectiveEnemyExplore;
import com.sica.modules.queenBee.QueenDrools;
import com.sica.modules.simpleEnemy.SimpleEnemy;
import com.sica.modules.workerBee.ObjectiveDrivenWorkerBee;
import com.sica.modules.workerBee.ObjectiveFindUnexploredTerrain;

public class AgentFactory {	
	/**
	 * Create all agents of the simulation.
	 * WORKER to worker bee
	 * DEFENDER to defender bee
	 * QUEEN to queen bee
	 * ENEMY to enemy
	 * @param agentType
	 * @return The agent requested or null if it does not exist
	 */
	public static Entity getAgent(EntityType type) {
		switch(type) {
		case OBJECTIVE_DRIVEN_WORKER:
			ObjectiveDrivenWorkerBee worker = new ObjectiveDrivenWorkerBee();
			worker.addObjective(new ObjectiveFindUnexploredTerrain());
			return worker;
		case OBJECTIVE_DRIVEN_ENEMY:
			ObjectiveDrivenEnemy enemy = new ObjectiveDrivenEnemy();
			enemy.addObjective(new ObjectiveEnemyExplore());
			return enemy;
		case OBJECTIVE_DRIVEN_DEFENDER:
			ObjectiveDrivenDefenderBee defender = new ObjectiveDrivenDefenderBee();
			defender.addObjective(new ObjectiveExploreTrench());
			return defender;
		case QUEEN:
			return new QueenDrools();
		case SIMPLE_ENEMY:
			return new SimpleEnemy();
		case DEFENDER_BEE:
			return new DefenderBee();
		default:
			throw new IllegalStateException("Cannot create the type of agent: " + type);
		
		}
	}
}
