package com.util.searching;

import java.awt.Point;

public class Node{

	public Node parentNode;
	public Node finalNode;
	public int totalCost;
	public int gCost;
	
	private int [] pos;
	
	public Node (Node parentNode, Node finalNode, int[] pos, int cost) {
		setParentNode(parentNode);
		setFinalNode(finalNode);
		setgCost(cost);
		this.pos = new int [2];
		this.pos[0] = pos[0];
		this.pos[1] = pos[1];
		
		
		if (finalNode != null) {
			setTotalCost(getgCost() + calculateCost());
		}
	}

	public int calculateCost () {
		// Manhattan distance
		return Math.abs(this.pos[0] - getFinalNode().getX()) + Math.abs(this.pos[1] - getFinalNode().getY());
	}
	
	public boolean equals (Node node) {
		if (getPosition().equals(node.getPosition())) {
			return true;
		}
		return false;
	}
	
	// getters and setters
	public Point getPosition () {
		Point aux = new Point();
		aux.setLocation(this.pos[0], this.pos[1]);
		return aux;
	}
	
	public int getX() {
		return pos[0];
	}
	
	public int getY() {
		return pos[1];
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

