package com.sica.agents;

import java.awt.Point;
import java.util.LinkedList;

import com.util.searching.AStar;
import com.util.searching.Map;

import sim.engine.Steppable;
import sim.portrayal.simple.OvalPortrayal2D;

public abstract class Agent extends OvalPortrayal2D implements Steppable {

	private static final long serialVersionUID = -1449354141004958564L;
	
	protected AStar pathFinding;
	protected LinkedList<Point> actualPath;
	protected Map map;
	protected int[] objective;
	
	public Agent () {
		objective = new int[2];
	}
	
	public Agent (Map map) {
		this.map = map;
		pathFinding = new AStar(map);
		objective = new int [2];
	}

	public void calculatePath(int [] actualPosition) {
		actualPath = pathFinding.findPath(actualPosition, getObjective());
	}
	
	// getters and setter
	public int[] getObjective() {
		return objective;
	}

	public void setObjective(int x, int y) {
		objective[0] = x;
		objective[1] = y;
	}
	
}
