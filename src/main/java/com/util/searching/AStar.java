package com.util.searching;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
	private PriorityQueue<Node> openList;
	private HashSet<Point> closedList;
	private Map map;

	public AStar (Map map) {
		this.map = map;
		openList = new PriorityQueue<Node>(new Comparator<Node>() {

			@Override
			public int compare(Node arg0, Node arg1) {
				return Integer.compare(arg0.getTotalCost(), arg1.getTotalCost());
			}
			
		});
		closedList = new HashSet<Point>();
	}

	/**
	 * Method to find the best path between the initial position and the final position
	 * @param initialPos
	 * @param finalPos
	 * @return A linked list with the path or null if there is not a path
	 */
	public List<Point> findPath (Point initialPos, Point finalPos) {
		if (map == null) {
			return null;
		}

		openList.clear();
		closedList.clear();
		map.resetVisited();

		Node initialNode = new Node (null, null, initialPos, 0);
		Node finalNode = new Node (null, null, finalPos, 0);
		openList.add(initialNode);

		while (openList.size() > 0) {
			Node actualNode = openList.peek();

			// Checking if the actual node is the final node
			if (actualNode.equals(finalNode)) {
				return createPath(actualNode);
			}

			openList.poll();
			
			//add new nodes if they have not yet been visited
			//and either they are not yet opened or their gCost is less than the actual gCost
			for (Node n: findAdjacentNodes(actualNode, finalNode)) {
				if (!closedList.contains(n.getPosition()))
					if (!openList.contains(n) || n.getgCost() < actualNode.getgCost())
						openList.add(n);
			}
			
			closedList.add((Point) actualNode.getPosition().clone());
		}

		// There is not path between initialPos and finalPos
		return null;
	}

	private List<Point> createPath (Node node) {
		LinkedList<Point> path = new LinkedList<Point>();
		while (node != null) {
			path.add(0, (Point) node.getPosition().clone());
			node = node.getParentNode();
		}

		return path;
	}

	private List<Node> findAdjacentNodes (Node node, Node finalNode) {
		//start with a list of up to 9 elements
		ArrayList<Node> adjacentNodes = new ArrayList<Node>(9);
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

	private void checkAdjacentNode (int [] pos, List<Node> nodes, Node node, Node finalNode) {
		int cost;
		if (!map.isVisited(pos[0], pos[1])) {
			cost = map.getCost(pos[0], pos[1]);
			if (cost < Map.IMPASSABLE) {
				map.setVisited(pos[0], pos[1], true);
				nodes.add(new Node (node, finalNode, new Point (pos[0], pos[1]), cost + node.getgCost()));
			}
		}
	}

	public void updateMap (Map map) {
		this.map = map;
	}
}

