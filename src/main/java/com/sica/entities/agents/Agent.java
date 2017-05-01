package com.sica.entities.agents;

import java.util.List;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.searching.AStar;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3612132049378487984L;
	
	protected List<Int2D> actualPath;
	protected KnowledgeMapInterface knowledge;
	protected Int2D objective;
	
	public Agent (EntityType type) {
		super(type);
		knowledge = new HashMapKnowledgeMap();
		objective = new Int2D();
		knowledge.addKnowledge(new Int2D(SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2), Knowledge.HIVE);
	}

	public void calculatePath(Int2D actualPosition) {
		actualPath = AStar.findPath(actualPosition, getObjective(), knowledge, SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
	}
	
	/*public void doStep( final SimulationState state ) {
		lookObstacles (state);
	}*/
	
	/**
	 * Check nearby cells for obstacles, and add them to our 
	 * own knowledge to avoid them later
	 * @param state
	 */
	private void lookObstacles (final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		
		IntBag xCoords = new IntBag(), yCoords = new IntBag();
		simState.environment.getRadialNeighbors(location.getX(), location.getY(), simState.getConfig().getRadioView(), Grid2D.TOROIDAL, true, Knowledge.OBSTACLE, xCoords, yCoords);
		
		boolean changed = false;
		for (int i = 0; i < xCoords.numObjs; i++)
			changed |= knowledge.addKnowledge(new Int2D(xCoords.get(i), yCoords.get(i)), Knowledge.OBSTACLE);
		
		if (changed) {
			if ((getObjective() != null) && (actualPath != null)) {
				calculatePath(new Int2D (location.getX(), location.getY()));
			}
		}
	}
	
	
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

	
	// getters and setter
	public Int2D getObjective() {
		return objective;
	}

	public void setObjective(int x, int y) {
		this.setObjective(new Int2D(x, y));
	}
	
	public void setObjective(Int2D objective) {
		if (objective != null) {
			this.objective = objective;
		}
		else {
			this.objective = null;
		}
	}

	@Override
	public Int2D getHome() {
		//gets the first element of the type HIVE
		return knowledge.getKnowledgeOf(Knowledge.HIVE).iterator().next();
	}

}
