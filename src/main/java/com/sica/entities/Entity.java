package com.sica.entities;

import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.movement.Direction;
import com.util.movement.MovementFunctions;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public abstract class Entity implements Steppable {
	private static final long serialVersionUID = -1449354141004958564L;
	
	// TODO clean this up
	public static enum EntityType {UNKNOWN, WORKER, DROOLS, OBJECTIVE_DRIVEN};
	
	private static int uaidGenerator = 0;	//static variable to count the number of agents created
	private int uaid;						//unique agent identifier
	private EntityType type = EntityType.UNKNOWN;
	//create a new uaid when instantiating a new agent
	{
		this.uaid = uaidGenerator;
		Entity.uaidGenerator++;
	}
	
	@Override
	public void step(SimState arg0) {
		this.doStep((SimulationState) arg0);
	}
	
	/**
	 * Called instead of step but with the SimState object already
	 * casted into our own version SimulationState
	 * @param simState
	 */
	public abstract void doStep(SimulationState simState);
	
	/**
	 * Main constructor of the agent class. Pass the type of
	 * agent you are creating. If it does not exist, add it
	 * to the enum Agent.AgentType.
	 * @param type
	 */
	public Entity(EntityType type) {
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
	public EntityType getType() {
		return this.type;
	}
	
	
	
	
	/**
	 * Move this entity in the specified direction with the specified grid mode
	 * @param dir
	 * @param simState
	 * @param mode
	 * @return true if the object was able to move, false otherwise
	 */
	public boolean move(Direction dir, SimulationState simState, int mode) {
		Int2D origin = simState.entities.getObjectLocation(this);
		Int2D destination = dir.getMovementOf(
				origin,
				mode,
				SimulationConfig.GRID_WIDTH,
				SimulationConfig.GRID_HEIGHT);
		if (this.canMoveTo(destination, simState, mode)) {
			simState.entities.setObjectLocation(this, destination);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @param p
	 * @param simState
	 * @return true if p is a valid position for this object (i.e: it can move to it) 
	 */
	public boolean canMoveTo(Int2D p, SimulationState simState, int mode) {
		Int2D fitted = MovementFunctions.fitToGrid(p, mode, SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		return !Knowledge.isType(simState.environment.get(fitted.x, fitted.y), Knowledge.OBSTACLE);
	}
}
