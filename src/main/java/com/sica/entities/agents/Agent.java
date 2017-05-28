package com.sica.entities.agents;

import java.util.List;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.movement.Direction;
import com.util.movement.PositioningFunctions;
import com.util.searching.AStar;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends Entity {
	private static final long serialVersionUID = -3612132049378487984L;
	
	protected List<Int2D> actualPath;			//actual path that the agent is using to get somewhere
	protected KnowledgeMapInterface knowledge;	//knowledge that this agent has about the environment
	protected Int2D home; 						//automatically set to where this agent spawned
	
	private int life = 4000;
	
	
	public Agent (EntityType type) {
		super(type);
		this.knowledge = new HashMapKnowledgeMap();
		this.life = SimulationConfig.config().getTime2Die() * SimulationConfig.config().getTime4Season() * 2;
		//this.knowledge = new ArrayKnowledgeMap(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	}
	
	@Override
	public void setUp(SimulationState simState) {
		this.observeEnvironment(simState, Knowledge.HIVE);
		this.home = simState.entities.getObjectLocation(this);
	}
	
	
	@Override
	public void doStep(SimulationState simState) {
		this.agentDoStep(simState);
		//unless it heals, it will eventually die
		this.damage(1);
		this.checkForDeath(simState);
	}
	
	public abstract void agentDoStep(SimulationState simState);
	
	/**
	 * Check if this agent's life is less than its time alive.
	 * If so, die
	 * @param simState
	 */
	private void checkForDeath(SimulationState simState) {
		//TODO: die this way if we are not enemies
		if (this.life < this.getTimesStepped() && this.getType() == EntityType.ENEMY) 
			this.die(simState);
	}
	
	/**
	 * Heal this agent 
	 * @param boost the life quantity to be added
	 */
	public void heal(int boost) {
		this.life += boost;
	}

	/**
	 * Damage this agent
	 * @param damage amount to be damaged for
	 */
	public void damage(int damage) {
		this.life -= damage;
	}
	
	/**
	 * @return true if this agent is supposed to be dead
	 */
	public boolean isDead() {
		return this.getTimesStepped() > this.life;
	}

	//////BEHAVIOR FUNCTIONS
	/**
	 * Sets the actualPath variable to a shortest path from the current position
	 * to the destination based on the current knowledge
	 * @param state
	 * @param destination
	 */
	public void computePath(final SimulationState state, Int2D destination) {
		Int2D beginPos = state.entities.getObjectLocation(this);
		actualPath = AStar.findPath(beginPos, destination, knowledge, SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT); 
	}
	
	/**
	 * Make this agent forget its current path
	 * (maybe it bumped into something)
	 */
	public void forgetPath() {
		actualPath = null;
	}
	
	/**
	 * Check nearby cells for the given type and add them to the knowledge map.
	 * Does not update meta data!
	 * @param state
	 * @return true if it saw new stuff
	 */
	public boolean observeEnvironment(final SimulationState simState, Knowledge type) {
		Int2D location = simState.entities.getObjectLocation(this);
		
		IntBag xCoords = new IntBag();
		IntBag yCoords = new IntBag();
		simState.environment.getRadialNeighbors(
				location.getX(), location.getY(),
				simState.getConfig().getRadioView(), // range to look in
				SimulationConfig.ENV_MODE, // mode of environment (bounded/unbound/toroidal)
				true, // ?
				type, // type of knowledge to look for (obstacles/flowers/...)
				xCoords, yCoords);
		
		boolean updated = false;
		for(int i = 0; i < xCoords.numObjs; i++) {
			Int2D pos = new Int2D(xCoords.get(i), yCoords.get(i));
			updated |= knowledge.updateKnowledge(pos, type);
		}
		return updated;
	}
	
	/**
	 * Move this entity in the specified direction with the specified grid mode
	 * @param dir
	 * @param simState
	 * @param mode
	 * @return true if the object was able to move, false otherwise
	 */
	public boolean moveInDirection(Direction dir, SimulationState simState, int mode) {
		Int2D destination = dir.getMovementOf(
				simState.entities.getObjectLocation(this),
				mode,
				SimulationConfig.GRID_WIDTH,
				SimulationConfig.GRID_HEIGHT);
		return this.moveTo(destination, simState, mode);
	}
	
	/**
	 * Follow this agent's internal path. The first position in the path is 
	 * removed, so that further calls to this function will move the agent
	 * further into the path. If for some reason the agent cannot move to
	 * a position, it will add the knowledge about that position to its internal
	 * knowledge, so that a later call to .calculatePath() will take it into account.
	 * @param simState
	 * @return true if there was a path within the agent, it was not empty,
	 * 			and the agent was able to move to the next place in it
	 */
	public boolean followCurrentPath(SimulationState simState) {
		if(actualPath != null && !actualPath.isEmpty())
			return this.moveTo(actualPath.remove(0), simState, SimulationConfig.ENV_MODE); 
		return false;
	}
	
	/**
	 * Move this entity to the specified location
	 * @param dir
	 * @param simState
	 * @param mode
	 * @return true if the object was able to move, false otherwise
	 */
	public boolean moveTo(Int2D destination, SimulationState simState, int mode) {
		if (this.canMoveTo(destination, simState, mode)) { //TODO this should be the only entry point to setObjectLocation!!
			simState.entities.setObjectLocation(this, destination);
			return true;
		} else {	//if we cannot move, add the knowledge so we don't fail again
			this.knowledge.updateKnowledge(destination, simState.environment.getKnowledgeAt(destination));
			return false;
		}
	}
	
	/**
	 * @param p
	 * @param simState
	 * @return true if p is a valid position for this object (i.e: it can move to it) 
	 */
	public boolean canMoveTo(Int2D p, SimulationState simState, int mode) {
		Int2D fitted = PositioningFunctions.fitToGrid(p, mode, SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		return simState.environment.getKnowledgeAt(fitted) != Knowledge.OBSTACLE;
	}
	/////////////////////////////
	
	
	//////KNOWLEDGE SHARING FUNCTIONS
	/**
	 * Sends this agent's knowledge to ALL OTHER AGENTS
	 * within reach
	 * @param simState
	 */
	public void broadcastKnowledgeToAll(SimulationState simState) {
		Bag beeBag = this.getNeighboringEntities(simState);
		
		for (Object a: beeBag) {
			if (a instanceof Agent && this != a)
				this.sendKnowledgeTo((Agent) a); 
		}
	}
	
	/**
	 * Sends this agent's knowledge to all entitites of the specified type
	 * within reach
	 * @param simState
	 * @param type
	 */
	public void broadcastKnowledgeToType(SimulationState simState, EntityType type) {
		Bag beeBag = this.getNeighboringEntities(simState);
		
		for (Object a: beeBag) {
			Entity ag = (Entity) a;
			if (ag.getType() != type || ag.getUAID() == this.getUAID()) {
				continue;
			}
			this.sendKnowledgeTo((Agent) a); 
		}
	}
	
	/**
	 * @param receptor the agent that will receive the knowledge
	 */
	public void sendKnowledgeTo (Agent receptor) {
	        receptor.receiveKnowledgeFrom(this);
	}

	/**
	 * This function could basically be copied into sendKnowledgeTo
	 * BUT, it might be useful to override it in some cases where we
	 * want to know when we are being sent knowledge, and maybe stop
	 * it or do something. i.e: if we are an enemy, we should not be
	 * able to learn from bees
	 * @param sender whoever is sending us knowledge
	 */
	public void receiveKnowledgeFrom (Agent sender) {
	        this.knowledge.updateKnowledge(sender.knowledge);
	}
	
	/**
	 * Used to share bits of knowledge
	 * @param where
	 * @param knowledge
	 */
	public void receiveBitOfKnowledge(Int2D where, Knowledge knowledge) { 
		this.knowledge.updateKnowledge(where, knowledge);
	}
	/////////////////////////////////
	
	

	
	/**
	 * Gets this agent's home (i.e: where this agent spawned)
	 * @return the point where this agent was spawned
	 */
	public Int2D getHome() {
		return this.home;
	}
	
	/**
	 * 
	 * @return
	 */
	public KnowledgeMapInterface getKnowledgeMap() {
		return knowledge;
	}


}
