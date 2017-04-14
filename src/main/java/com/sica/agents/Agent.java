package com.sica.agents;

import sim.engine.Steppable;
import sim.portrayal.simple.OvalPortrayal2D;

public abstract class Agent extends OvalPortrayal2D implements Steppable {

	private static final long serialVersionUID = -1449354141004958564L;
	
	private static int uaidGenerator = 0;	//static variable to count the number of agents created
	private int uaid;						//unique agent identifier 
	//create a new uaid when instantiating a new agent
	{
		this.uaid = uaidGenerator;
		Agent.uaidGenerator++;
	}
	
	/**
	 * Get this agent's unique agent identifier
	 * @return a number representing this agent's ID. 
	 */
	public int getUAID() {
		return this.uaid;
	}
	
}
