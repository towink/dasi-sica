package com.sica.entities;

import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Bag;
import sim.util.Int2D;

public abstract class Entity implements Steppable {
	private static final long serialVersionUID = -1449354141004958564L;
	
	// TODO clean this up
	public static enum EntityType {UNKNOWN, 
		WORKER, DROOLS, //legacy bees, remove in the future
		OBJECTIVE_DRIVEN_WORKER, QUEEN, BEE_SPAWNER, ENVIRONMENT_SPAWNER}; 
	

	private static int uaidGenerator = 0;	//static variable to count the number of agents created
	private int uaid;						//unique agent identifier
	private EntityType type = EntityType.UNKNOWN;
	private boolean initialized = false;
	//create a new uaid when instantiating a new agent
	{
		this.uaid = uaidGenerator;
		Entity.uaidGenerator++;
	}
	
	/**
	 * Main constructor of the agent class. Pass the type of
	 * agent you are creating and where you are creating it. 
	 * If it does not exist, add it
	 * to the enum Agent.AgentType.
	 * @param type
	 */
	public Entity(EntityType type) {
		this.type = type;
	}

	
	@Override
	public void step(SimState arg0) {
		if (!this.initialized) {
			this.setUp((SimulationState) arg0);
			this.initialized = true;
		}
		this.doStep((SimulationState) arg0);
	}
	
	/**
	 * Called instead of step but with the SimState object already
	 * casted into our own version SimulationState
	 * @param simState
	 */
	public abstract void doStep(SimulationState simState);
	
	
	/**
	 * Called ONCE on its first step, just before doStep
	 * Override if needed
	 * @param simState
	 */
	public void setUp(SimulationState simState) {}
	
	
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
	public EntityType getType() {
		return this.type;
	}
	
	
	/**
	 * Gets all neighboring entities.
	 * DOES INCLUDE ITSELF!
	 * @param simState
	 */
	public Bag getNeighboringEntities(SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		return simState.entities.getRadialNeighbors(
				location.getX(),
				location.getY(),
				simState.getConfig().getRadioView(),
				SimulationConfig.ENV_MODE,
				false);
	}
}
