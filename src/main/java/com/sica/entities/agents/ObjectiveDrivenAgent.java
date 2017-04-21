package com.sica.entities.agents;

import java.awt.Point;
import java.util.List;
import java.util.PriorityQueue;

import com.sica.behaviour.Objective;
import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.TypeConversions;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.searching.AStar;

import sim.field.grid.Grid2D;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class ObjectiveDrivenAgent extends Entity{
	private static final long serialVersionUID = -3670233969349917087L;

	
	private PriorityQueue<Objective> objectives;
	
	private KnowledgeMapInterface knowledgeMap;
	
	public static AStar pathFinder;
	protected List<Point> actualPath;
	
	public ObjectiveDrivenAgent(EntityType type, HashMapKnowledgeMap knowledgeMap) {
		super(type);
		this.objectives = new PriorityQueue<Objective>();
		this.knowledgeMap = knowledgeMap;
		this.actualPath = null;
		if (pathFinder == null) {
			pathFinder = new AStar();
		}
	}

	@Override
	public void doStep(SimulationState simState) {
		if (!objectives.isEmpty()) {
			Objective current = objectives.peek();
			if (!current.isFinished(this, simState))
				current.step(this, simState);
			else
				objectives.poll();
		}
	}
	
	/**
	 * Check nearby cells for obstacles, and add them to our 
	 * own knowledge to avoid them later
	 * @param state
	 */
	public void observeEnvironment(final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		
		IntBag xCoords = new IntBag();
		IntBag yCoords = new IntBag();
		simState.environment.getRadialNeighbors(
				location.getX(), location.getY(),
				simState.getConfig().getRadioView(), SimulationConfig.ENV_MODE,
				true, Knowledge.OBSTACLE,
				xCoords, yCoords);
		
		for(int i = 0; i < xCoords.numObjs; i++)
			knowledgeMap.addKnowledge(new Int2D(xCoords.get(i), yCoords.get(i)), Knowledge.OBSTACLE);
		
	}
	
	
	
	public void addObjective(Objective o) {
		this.objectives.add(o);
	}
	
	public List<Point> getActualPath() {
		return actualPath;
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
				TypeConversions.int2DtoPoint(beginPos),
				TypeConversions.int2DtoPoint(destination),
				knowledgeMap.toAStarMap());
	}
	
	public KnowledgeMapInterface getKnowledgeMap() {
		return knowledgeMap;
	}

}
