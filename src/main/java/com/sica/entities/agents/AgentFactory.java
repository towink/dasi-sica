package com.sica.entities.agents;

import com.sica.behaviour.Objectives.ObjectiveExplore;
import com.sica.entities.Entity;
import com.sica.entities.Entity.EntityType;

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
			ObjectiveDrivenWorkerBee agent = new ObjectiveDrivenWorkerBee();
			agent.addObjective(new ObjectiveExplore());
			return agent;
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
