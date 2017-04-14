package com.sica.entities.agents;

import java.awt.Point;
import java.util.List;

import com.sica.entities.AgentEntity;
import com.sica.environment.EnvironmentTypes;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationController;
import com.util.searching.AStar;
import com.util.searching.Map;
import com.util.searching.Map.Type;

import sim.engine.SimState;
import sim.field.grid.Grid2D;
import sim.util.Int2D;
import sim.util.IntBag;

public class Agent extends AgentEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3612132049378487984L;

	public Point home;
	
	protected AStar pathFinding;
	protected List<Point> actualPath;
	protected Map map;
	protected Point objective;
	
	public Agent (AgentEntityType type) {
		super(type);
		objective = new Point();
		this.map = new Map(SimulationConfig.GRID_WIDTH, SimulationConfig.GRID_HEIGHT);
		pathFinding = new AStar(map);
		setHome(new Point (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
	}
	
	public Agent (AgentEntityType type, Map map) {
		super(type);
		this.map = map;
		pathFinding = new AStar(map);
		objective = new Point();
		setHome(new Point (SimulationConfig.GRID_WIDTH/2, SimulationConfig.GRID_HEIGHT/2));
	}

	public void calculatePath(Point actualPosition) {
		actualPath = pathFinding.findPath(actualPosition, getObjective());
	}
	
	public void step( final SimState state ) {
		lookObstacles (state);
	}
	
	/**
	 * Check nearby cells for obstacles, and add them to our 
	 * own knowledge to avoid them later
	 * @param state
	 */
	private void lookObstacles (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.entities.getObjectLocation(this);
		
		IntBag xCoords = new IntBag(), yCoords = new IntBag();
		simulation.environment.getRadialNeighbors(location.getX(), location.getY(), simulation.getConfig().getRadioView(), Grid2D.TOROIDAL, true, EnvironmentTypes.OBSTACLE, xCoords, yCoords);
		
		boolean changed = false;
		for (int i = 0; i < xCoords.numObjs; i++) {
			changed |= map.modifyMap(xCoords.get(i), yCoords.get(i), Type.OBSTACLE);
		}
		
		if (changed) {
			pathFinding.updateMap(map);
			if ((getObjective() != null) && (actualPath != null)) {
				calculatePath(new Point (location.getX(), location.getY()));
			}
		}
	}
	
	
	// getters and setter
	public Point getObjective() {
		return objective;
	}

	public void setObjective(int x, int y) {
		objective.setLocation(x, y);
	}
	
	public void setObjective(Point objective) {
		this.objective.setLocation(objective.x, objective.y);
	}

	public Point getHome() {
		return home;
	}

	public void setHome(Point home) {
		this.home = home;
	}
}
