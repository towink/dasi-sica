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
	protected int[] objetive;
	
	public Agent () {
		objetive = new int[2];
	}
	
	public Agent (Map map) {
		this.map = map;
		pathFinding = new AStar(map);
		objetive = new int [2];
	}

	public void calculatePath(int [] actualPosition) {
		actualPath = pathFinding.findPath(actualPosition, getObjetive());
	}
	
	// getters and setter
	public int[] getObjetive() {
		return objetive;
	}

	public void setObjetive(int x, int y) {
		objetive[0] = x;
		objetive[1] = y;
	}
	
}
