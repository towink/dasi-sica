package com.sica.entities.agents;

import java.util.Collection;
import java.util.List;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.movement.Direction;
import com.util.movement.MovementFunctions;
import com.util.searching.AStar;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends Entity {
	private static final long serialVersionUID = -3612132049378487984L;
	
	protected List<Int2D> actualPath;
	protected KnowledgeMapInterface knowledge;
	
	
	public Agent (EntityType type) {
		super(type);
		this.knowledge = new HashMapKnowledgeMap();
		this.knowledge.addKnowledge(new Int2D(SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2), Knowledge.HIVE);	
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
	public boolean move(Direction dir, SimulationState simState, int mode) {
		Int2D origin = simState.entities.getObjectLocation(this);
		Int2D destination = dir.getMovementOf(
				origin,
				mode,
				SimulationConfig.GRID_WIDTH,
				SimulationConfig.GRID_HEIGHT);
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
		Int2D fitted = MovementFunctions.fitToGrid(p, mode, SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
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
	/////////////////////////////////
	
	

	
	/**
	 * Gets this agents home (probably a hive)
	 * @return A random hive in this agent knowledge
	 */
	public Int2D getHome() {
		Collection<Int2D> knownHomes = knowledge.getKnowledgeOf(Knowledge.HIVE);
		if (!knownHomes.isEmpty()) { //if we know of a home, return the first in the list
			return knowledge.getKnowledgeOf(Knowledge.HIVE).iterator().next();
		} else {
			//could return the center of the map, but we can better troubleshoot if bees start going to the top left corner
			return new Int2D(0, 0);
		}
	}

}
