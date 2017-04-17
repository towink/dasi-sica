package com.sica.entities.agents;

import java.awt.Point;
import java.util.List;

import com.sica.entities.Entity;
import com.sica.environment.EnvironmentTypes;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Sites;
import com.util.searching.AStar;
import com.util.searching.Map;
import com.util.searching.Map.Type;

import sim.field.grid.Grid2D;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3612132049378487984L;
	
	protected static AStar pathFinder;
	protected List<Point> actualPath;
	protected Map map;
	protected Sites knowledge;
	protected Point objective;
	
	public Agent (EntityType type) {
		super(type);
		objective = new Point();
		this.map = new Map(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		knowledge = new Sites();
		knowledge.insert("hive", new Point (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
		knowledge.updated();
		if (pathFinder == null) {
			pathFinder = new AStar();
		}
	}
	
	public Agent (EntityType type, Map map) {
		super(type);
		this.map = map;
		knowledge = new Sites();
		objective = new Point();
		knowledge.insert("hive", new Point (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
		knowledge.updated();
		if (pathFinder == null) {
			pathFinder = new AStar();
		}
	}

	public void calculatePath(Point actualPosition) {
		actualPath = pathFinder.findPath(actualPosition, getObjective(), map);
	}
	
	public void doStep( final SimulationState state ) {
		lookObstacles (state);
	}
	
	/**
	 * Check nearby cells for obstacles, and add them to our 
	 * own knowledge to avoid them later
	 * @param state
	 */
	private void lookObstacles (final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		
		IntBag xCoords = new IntBag(), yCoords = new IntBag();
		simState.environment.getRadialNeighbors(location.getX(), location.getY(), simState.getConfig().getRadioView(), Grid2D.TOROIDAL, true, EnvironmentTypes.OBSTACLE, xCoords, yCoords);
		
		boolean changed = false;
		boolean auxChanged;
		for (int i = 0; i < xCoords.numObjs; i++) {
			auxChanged = map.modifyMap(xCoords.get(i), yCoords.get(i), Type.OBSTACLE);
			if (auxChanged) {
				knowledge.insert("obstacle", new Point (xCoords.get(i), yCoords.get(i)));
			}
			changed |= auxChanged;
		}
		
		if (changed) {
			if ((getObjective() != null) && (actualPath != null)) {
				calculatePath(new Point (location.getX(), location.getY()));
			}
		}
	}
	
	
	public void sendKnowledge (Agent receptor) {
		
	}
	
	public void receiveKnowledge (Sites knowledge) {
		
	}
	
	// getters and setter
	public Point getObjective() {
		return objective;
	}

	public void setObjective(int x, int y) {
		objective.setLocation(x, y);
	}
	
	public void setObjective(Point objective) {
		if (objective != null) {
			this.objective.setLocation(objective.x, objective.y);
		}
		else {
			this.objective = null;
		}
	}

	public Point getHome() {
		return knowledge.get("hive").get(0);
	}

}
