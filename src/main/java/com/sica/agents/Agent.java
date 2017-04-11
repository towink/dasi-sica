package com.sica.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.sica.SimulationController;
import com.util.searching.AStar;
import com.util.searching.Map;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.grid.Grid2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.Bag;
import sim.util.Int2D;
import sim.util.IntBag;

public abstract class Agent extends OvalPortrayal2D implements Steppable {

	private static final long serialVersionUID = -1449354141004958564L;
	
	protected AStar pathFinding;
	protected LinkedList<Point> actualPath;
	protected Map map;
	protected Point objective;
	
	public Agent () {
		objective = new Point();
		this.map = new Map(SimulationController.GRID_WIDTH, SimulationController.GRID_HEIGHT);
		pathFinding = new AStar(map);
	}
	
	public Agent (Map map) {
		this.map = map;
		pathFinding = new AStar(map);
		objective = new Point();
	}

	public void calculatePath(Point actualPosition) {
		actualPath = pathFinding.findPath(actualPosition, getObjective());
	}
	
	public void step( final SimState state ) {
		look (state);
	}
	
	private void look (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.bees.getObjectLocation(this);
		IntBag obstacleBag = simulation.obstacles.getRadialNeighbors(location.getX(), location.getY(), simulation.getRadioView(), Grid2D.TOROIDAL, true);
		// update the knowledge of the map
		
	}
	
	
	// getters and setter
	public Point getObjective() {
		return objective;
	}

	public void setObjective(int x, int y) {
		objective.setLocation(x, y);
	}
	
}
