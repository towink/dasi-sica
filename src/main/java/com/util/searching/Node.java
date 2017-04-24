package com.util.searching;

import sim.util.Int2D;

public class Node {
	
	public enum DistanceMode {
		MANHATTAN, STRAIGHT
	}

	private Node parent;
	private int totalCost;
	private int baseCost;
	private int toDestinationCost;
	
	private Int2D pos;
	
	public Node (Int2D pos, Int2D destination, int baseCost, Node parent, DistanceMode mode) {
		this.parent = parent;
		this.baseCost = baseCost;
		this.pos = pos;
		this.toDestinationCost = this.calculateCost(mode, destination);
		this.totalCost = this.baseCost + this.toDestinationCost;
	}
	
	/**
	 * Returns an estimate of the cost from this node's position to the given destination
	 * @param mode: the way of measuring distance
	 * @param destination
	 * @return
	 */
	private int calculateCost (DistanceMode mode, Int2D destination) {
		switch(mode) {
		case MANHATTAN:
			return Math.abs(this.pos.x - destination.x) + Math.abs(this.pos.y - destination.y);
		case STRAIGHT:
			int dx = this.pos.x - destination.x;
			int dy = this.pos.y - destination.y;
			return (int) Math.sqrt(dx*dx + dy*dy);
		}
		return 0;
	}
	
	public NodeIterator iterator () {
		return new NodeIterator(this);
	}
	
	// getters and setters
	/**
	 * @return this node's position
	 */
	public final Int2D getPosition () {
		return this.pos;
	}
	
	/**
	 * @return this node's x coordinate
	 */
	public int getX() {
		return this.pos.x;
	}
	
	/**
	 * @return this node's y coordinate
	 */
	public int getY() {
		return this.pos.y;
	}
	
	/** 
	 * @return the node from which this one spawned. Useful for backtracking the path
	 */
	public Node getParentNode() {
		return this.parent;
	}

	/**
	 * @return the total cost to get to this node from the begining plus the cost of getting to the end from this node
	 */
	public int getTotalCost() {
		return this.totalCost;
	}
	
	/**
	 * @return the cost from the start to this node
	 */
	public int getBaseCost() {
		return this.baseCost;
	}

	/**
	 * @return the cost from this node to the destination
	 */
	public int getToDestinationCost() {
		return this.toDestinationCost;
	}

}

