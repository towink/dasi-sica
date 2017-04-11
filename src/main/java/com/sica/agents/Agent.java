package com.sica.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.sica.SimulationController;
import com.util.searching.AStar;
import com.util.searching.Map;
import com.util.searching.Map.Type;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends OvalPortrayal2D implements Steppable {

	private static final long serialVersionUID = -1449354141004958564L;
	
	public Point home;
	
	protected AStar pathFinding;
	protected LinkedList<Point> actualPath;
	protected Map map;
	protected Point objective;
	
	public Agent () {
		objective = new Point();
		this.map = new Map(SimulationController.GRID_WIDTH, SimulationController.GRID_HEIGHT);
		pathFinding = new AStar(map);
		setHome(new Point (SimulationController.GRID_WIDTH/2, SimulationController.GRID_HEIGHT/2));
	}
	
	public Agent (Map map) {
		this.map = map;
		pathFinding = new AStar(map);
		objective = new Point();
		setHome(new Point (SimulationController.GRID_WIDTH/2, SimulationController.GRID_HEIGHT/2));
	}

	public void calculatePath(Point actualPosition) {
		actualPath = pathFinding.findPath(actualPosition, getObjective());
	}
	
	public void step( final SimState state ) {
		lookObstacles (state);
	}
	
	private void lookObstacles (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.bees.getObjectLocation(this);
		Bag obstacleBag = simulation.obstacles.getRadialNeighbors(location.getX(), location.getY(), simulation.getRadioView(), Grid2D.TOROIDAL, true);
		
		boolean changed = false;
		for (Object obstacle: obstacleBag) {
			Int2D loc = simulation.obstacles.getObjectLocation(obstacle);
			changed |= map.modifyMap(loc.getX(), loc.getY(), Type.OBSTACLE);
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
