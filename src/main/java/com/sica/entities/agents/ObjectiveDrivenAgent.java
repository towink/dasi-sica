package com.sica.entities.agents;

import java.util.List;
import java.util.PriorityQueue;

import com.sica.behaviour.Objective;
import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.searching.AStar;

import sim.util.Int2D;
import sim.util.IntBag;

/**
 * Agent whose behavior is controlled by objectives.
 * @author Tobias
 *
 */
public abstract class ObjectiveDrivenAgent extends Entity{
	private static final long serialVersionUID = -3670233969349917087L;

	protected PriorityQueue<Objective> objectives;
	protected KnowledgeMapInterface knowledge;
	protected List<Int2D> actualPath;
	public static AStar pathFinder;
	
	/**
	 * Creates an agent with a initially empty knowledge and without any objectives.
	 * @param type The entity type of the agent to be created.
	 */
	public ObjectiveDrivenAgent(EntityType type) {
		super(type);
		this.objectives = new PriorityQueue<Objective>();
		this.knowledge = new HashMapKnowledgeMap();
		this.actualPath = null;
		if (pathFinder == null) {
			pathFinder = new AStar(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		}
	}
	
	/**
	 * Called on each step in the MASON simulation. It is not intended that subclasses
	 * overwrite this method.
	 * This function manages the execution and termination of the agent's objectives.
	 * @param simState
	 */
	@Override
	public final void doStep(SimulationState simState) {
		if (!objectives.isEmpty()) {
			Objective current = objectives.peek();
			if (!current.isFinished(this, simState)) {
				// objective is not finished
				current.step(this, simState);
			}
			else {
				// objective is finished
				current.onFinished(this, simState);
				objectives.poll();
			}
		}
		// if there are no objectives do not do anything
	}
	
	/**
	 * 
	 * @param receptor
	 */
	public void sendKnowledge(ObjectiveDrivenAgent receptor) {
		receptor.receiveKnowledge(knowledge);
	}
	
	/**
	 * 
	 * @param knowledge
	 */
	public void receiveKnowledge(KnowledgeMapInterface kMap) {
		knowledge.updateKnowledge(kMap);
	}
	
	/**
	 * Check nearby cells for the given type and add them to the knowledge map.
	 * Does not update meta data!
	 * @param state
	 */
	public void observeEnvironment(final SimulationState simState, Knowledge type) {
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
		
		for(int i = 0; i < xCoords.numObjs; i++) {
			Int2D pos = new Int2D(xCoords.get(i), yCoords.get(i));
			if(Knowledge.isType(knowledge.getKnowledgeAt(pos).toInt(), Knowledge.UNKNOWN)) {
				// agent does not know anything yet about this position - add new knowledge
				knowledge.addKnowledge(pos, type);
			}
			else if(!Knowledge.isType(knowledge.getKnowledgeAt(pos).toInt(), type)) {
				// agent has another knowledge at that position - update
				knowledge.removeKnowledge(pos);
				knowledge.addKnowledge(pos, type);
			}
		}
	}
	
	/**
	 * Adds an objective to the queue of objectives of this agent.
	 * @param o The objective to be added
	 */
	public void addObjective(Objective o) {
		this.objectives.add(o);
	}
	
	
	/**
	 * Sets the actualPath variable to a shortest path from the current position
	 * to the destination based on the current knowledge
	 * @param state
	 * @param destination
	 */
	public void computePath(final SimulationState state, Int2D destination) {
		Int2D beginPos = state.entities.getObjectLocation(this);
		actualPath = ObjectiveDrivenAgent.pathFinder.findPath(
				beginPos,
				destination,
				knowledge);
	}
	
	/**
	 * 
	 * @return
	 */
	public KnowledgeMapInterface getKnowledgeMap() {
		return knowledge;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Int2D> getActualPath() {
		return actualPath;
	}

}
