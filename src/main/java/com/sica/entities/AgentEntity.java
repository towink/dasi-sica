package com.sica.entities;

import sim.engine.Steppable;
import sim.portrayal.simple.OvalPortrayal2D;

public abstract class AgentEntity extends OvalPortrayal2D implements Steppable {
	private static final long serialVersionUID = -1449354141004958564L;
	
	public static enum AgentEntityType {UNKNOWN, WORKER, DROOLS};
	
	private static int uaidGenerator = 0;	//static variable to count the number of agents created
	private int uaid;						//unique agent identifier
	private AgentEntityType type = AgentEntityType.UNKNOWN;
	//create a new uaid when instantiating a new agent
	{
		this.uaid = uaidGenerator;
		AgentEntity.uaidGenerator++;
	}
	
	/**
	 * Main constructor of the agent class. Pass the type of
	 * agent you are creating. If it does not exist, add it
	 * to the enum Agent.AgentType.
	 * @param type
	 */
	public AgentEntity(AgentEntityType type) {
		this.type = type;
	}
	
	/**
	 * Get this agent's unique agent identifier
	 * @return a number representing this agent's ID. 
	 */
	public int getUAID() {
		return this.uaid;
	}
	
	/**
	 * @return the AgentType of this agent
	 */
	public AgentEntityType getType() {
		return this.type;
	}
	
}
