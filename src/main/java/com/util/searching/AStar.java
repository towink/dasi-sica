package com.util.searching;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;

public class AStar {
	private LinkedList<Node> openList;
	private HashSet<Point> closedList;
	private Map map;

	public AStar (Map map) {
		this.map = map;
		openList = new LinkedList<Node>();
		closedList = new HashSet<Point>();
	}

	/**
	 * Method to find the best path between the initial position and the final position
	 * @param initialPos
	 * @param finalPos
	 * @return A linked list with the path or null if there is not a path
	 */
	public LinkedList<Point> findPath (int [] initialPos, int [] finalPos) {
		if (map == null) {
			return null;
		}

		openList.clear();
		closedList.clear();
		map.resetVisited();

		Node initialNode = new Node (null, null, initialPos, 0);
		Node finalNode = new Node (null, null, finalPos, 0);
		putNode(initialNode);

		while (openList.size() > 0) {
			Node actualNode = openList.get(openList.size() - 1);

			// Checking if the actual node is the final node
			if (actualNode.equals(finalNode)) {
				return createPath(actualNode);
			}

			openList.remove(actualNode);
			int index = 0;
			LinkedList<Node> adjacentNodes = findAdjacentNodes(actualNode, finalNode);

			while (adjacentNodes.size() > index) {
				if (!closedList.contains(adjacentNodes.get(index).getPosition())) {
					if (openList.contains(adjacentNodes.get(index))) {
						if (adjacentNodes.get(index).getgCost() >= actualNode.getgCost()) {
							index++;
							continue;
						}
					}
					putNode(adjacentNodes.get(index));
				}
				index++;
			}
			closedList.add(actualNode.getPosition());
		}

		// There is not path between initialPos and finalPos
		return null;
	}

	private LinkedList<Point> createPath (Node node) {
		LinkedList<Point> path = new LinkedList<Point>();
		while (node != null) {
			path.add(0, node.getPosition());
			node = node.getParentNode();
		}

		return path;
	}

	private LinkedList<Node> findAdjacentNodes (Node node, Node finalNode) {
		LinkedList<Node> adjacentNodes = new LinkedList<Node>();
		int x = node.getX();
		int y = node.getY();
		int [] aux = new int [2];
		// left
		if (x > 0) {
			aux[0] = node.getX() - 1;
			aux[1] = node.getY();
			checkAdjacentNode(aux, adjacentNodes, node, finalNode);
		}
		// right
		if (x < map.getWidth() - 1) {
			aux[0] = node.getX() + 1;
			aux[1] = node.getY();
			checkAdjacentNode(aux, adjacentNodes, node, finalNode);
		}

		// up
		if (y > 0) {
			aux[0] = node.getX();
			aux[1] = node.getY() - 1;
			checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			// up left
			if (x > 0) {
				aux[0] = node.getX() - 1;
				checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			}
			// up right
			if (x < map.getWidth() - 1) {
				aux[0] = node.getX() + 1;
				checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			}
		}

		// down
		if (y < map.getHeight() - 1) {
			aux[0] = node.getX();
			aux[1] = node.getY() + 1;
			checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			// down left
			if (x > 0) {
				aux[0] = node.getX() - 1;
				checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			}
			// down right
			if (x < map.getWidth() - 1) {
				aux[0] = node.getX() + 1;
				checkAdjacentNode(aux, adjacentNodes, node, finalNode);
			}
		}


		return adjacentNodes;
	}

	private void checkAdjacentNode (int [] pos, LinkedList<Node> nodes, Node node, Node finalNode) {
		int cost;
		if (!map.isVisited(pos[0], pos[1])) {
			cost = map.getCost(pos[0], pos[1]);
			if (cost < Map.IMPASSABLE) {
				map.setVisited(pos[0], pos[1], true);
				nodes.add(new Node (node, finalNode, pos, cost + node.getgCost()));
			}
		}
	}

	/**
	 * Method to put a node in the open list
	 * @param node
	 */
	private void putNode (Node node) {
		int index = 0;
		while ((openList.size() > index) && (node.getTotalCost() < openList.get(index).getTotalCost())){
			index++;
		}
		openList.add(index, node);
	}

	private void updateMap (Map map) {
		this.map = map;
	}
}

