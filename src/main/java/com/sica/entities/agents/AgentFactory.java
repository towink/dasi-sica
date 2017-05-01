package com.sica.entities.agents;

import com.sica.behaviour.Objectives.ObjectiveExplore;
import com.sica.entities.Entity;

import sim.util.Int2D;

public class AgentFactory {

	public static final String WORKER_BEE = "WORKER";
	public static final String DEFENDER_BEE = "DEFENDER";
	public static final String QUEEN_BEE = "QUEEN";
	public static final String ENEMY = "ENEMY";
	
	/**
	 * Create all agents of the simulation.
	 * WORKER to worker bee
	 * DEFENDER to defender bee
	 * QUEEN to queen bee
	 * ENEMY to enemy
	 * @param agentType
	 * @return The agent requested or null if it does not exist
	 */
	public static Entity getAgent(String agentType, Int2D home) {
		agentType = agentType.toUpperCase();
		if (agentType == null) {
			return null;
		}
		
		if (agentType.equals("WORKER")) {
			ObjectiveDrivenWorkerBee agent = new ObjectiveDrivenWorkerBee(home);
			agent.addObjective(new ObjectiveExplore());
			return agent;
		}
		else if (agentType.equals("DEFENDER")) {
			System.out.println("DEFENDER: It has not yet been implemented, returning null");
			return null;
		}
		else if (agentType.equals("QUEEN")) {
			System.out.println("QUEEN: It has not yet been implemented, returning null");
			return null;
		}
		else if (agentType.equals("ENEMY")) {
			System.out.println("ENEMY: It has not yet been implemented, returning null");
			return null;
		}
		
		return null;
	}
}
