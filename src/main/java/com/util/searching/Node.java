package com.util.searching;

import sim.util.Int2D;

public class Node{

	public Node parentNode;
	public Node finalNode;
	public int totalCost;
	public int gCost;
	
	private Int2D pos;
	
	public Node (Node parentNode, Node finalNode, Int2D pos, int cost) {
		setParentNode(parentNode);
		setFinalNode(finalNode);
		setgCost(cost);
		this.pos = new Int2D(pos.x, pos.y);
		
		
		if (finalNode != null) {
			setTotalCost(getgCost() + calculateCost());
		}
	}

	public int calculateCost () {
		// Manhattan distance
		return Math.abs(this.pos.x - getFinalNode().getX()) + Math.abs(this.pos.y - getFinalNode().getY());
	}
	
	public boolean equals (Node node) {
		if (getPosition().equals(node.getPosition())) {
			return true;
		}
		return false;
	}
	
	// getters and setters
	public final Int2D getPosition () {
		return this.pos;
	}
	
	public int getX() {
		return pos.x;
	}
	
	public int getY() {
		return pos.y;
	}
	
	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	public Node getFinalNode() {
		return finalNode;
	}

	public void setFinalNode(Node finalNode) {
		this.finalNode = finalNode;
	}

	public int getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}
}

