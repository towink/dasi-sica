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
	// when you add a new entitytype, be sure to add it to isEnemy if it is a new type of enemy!
	public static enum EntityType {UNKNOWN, DROOLS,
		WORKER_BEE, QUEEN_BEE, BEE_SPAWNER, ENVIRONMENT_SPAWNER, 
		ENEMY, ENEMY_FLOWER, ENEMY_SPAWNER, DEFENDER_BEE, SEASON}; 
	

	private static int uaidGenerator = 0;	//static variable to count the number of agents created
	private int uaid;						//unique agent identifier
	private EntityType type = EntityType.UNKNOWN;
	private boolean initialized = false;
	private boolean removed = false;
	private int timesStepped = 0;
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
		if (this.removed)
			return;
		
		this.timesStepped++;
		
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
	 * Called ONCE on its first step, just before doStep.
	 * Override if needed
	 * @param simState
	 */
	public void setUp(SimulationState simState) {}
	
	
	/**
	 * Kill this agent (i.e: enemy kills bee or vice versa)
	 * by disabling its step() method
	 */
	public void die(SimulationState simState) {
		//System.out.println("Entity dead: " + this.getType());
		simState.entities.remove(this);
		this.removed = true;
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
	
	/**
	 * @param e
	 * @return true if this entity is an enemy of any type
	 */
	public static boolean isEnemy(Entity e) {
		return e.type == EntityType.ENEMY
				|| e.type == EntityType.ENEMY_FLOWER;//e.type == EntityType.SIMPLE_ENEMY; //|| e.type == EntityType.NEW_ENEMY_TYPE ...
	}
	
	/**
	 * @param e
	 * @return true if the given entity is a bee (of any type)
	 */
	public static boolean isBee(Entity e) {
		return e.type == EntityType.DEFENDER_BEE 
				|| e.type == EntityType.QUEEN_BEE
				|| e.type == EntityType.WORKER_BEE;
	}
	
	/**
	 * @return the number of times this entity has been stepped
	 */
	public int getTimesStepped() {
		return this.timesStepped;
	}
}
