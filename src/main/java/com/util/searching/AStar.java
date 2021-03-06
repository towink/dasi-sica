package com.util.searching;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;

import sim.util.Int2D;

public class AStar {
	private static final int IMPASSABLE = 10000;
	
	//keep this comparator here in case we want to make other metrics such as Best First Search
	private static final Comparator<Node> nodeComparator = new Comparator<Node>() {

		@Override
		public int compare(Node arg0, Node arg1) {
			return Integer.compare(arg0.getTotalCost(), arg1.getTotalCost());
		}
		
	};

	/**
	 * Method to find the best path between the initial position and the final position
	 * This is the A_STAR implementation
	 * @param initialPos
	 * @param finalPos
	 * @return A linked list with the path or null if there is not a path
	 */
	public static List<Int2D> findPath (Int2D initialPos, Int2D finalPos, KnowledgeMapInterface map, int width, int height) {
		//initialize the open list queue and map
		PriorityQueue<Node> openList = new PriorityQueue<Node>(nodeComparator);
		Map aState = new Map(width, height);
		
		//add the opening node
		AStar.addAndOpenNewNode(openList, new Node (initialPos, finalPos, 0, null, Node.DistanceMode.MANHATTAN), aState);
		//while we still have nodes to explore
		while (openList.size() > 0) {
			//get the first node in the queue according to its comparator's metric
			//i.e: the most promising
			Node actualNode = openList.poll();
			//If we've reached our destination, return the path to it
			if (actualNode.getPosition().equals(finalPos)) {
				return createPath(actualNode);
			}
			//otherwise open all valid adjacent nodes and keep looking
			openAdjacentNodes(openList, actualNode, finalPos, map, aState);
		}

		// There is not path between initialPos and finalPos
		return null;
	}
	
	/**
	 * Adds the given node to the opened nodes list, and
	 * sets it as opened on the given map.
	 * NOTE: NO CHECKS ARE MADE TO SEE IF IT IS ALREADY OPENED,
	 * DO IT BEFOREHAND!!
	 * @param node
	 * @param map
	 */
	private static void addAndOpenNewNode(PriorityQueue<Node> openList, Node node, Map aState) {
		openList.add(node);
		aState.setVisited(node.getX(), node.getY());
	}
	
	/**
	 * Creates a path by backtracking through parent nodes
	 * @param node
	 * @return
	 */
	private static List<Int2D> createPath (Node node) {
		LinkedList<Int2D> path = new LinkedList<Int2D>();
		/*while (node != null) {
			path.add(0, node.getPosition());
			node = node.getParentNode();
		}*/

		Iterator<Node> iterator = node.iterator();
		while (iterator.hasNext()) {
			Node aux = iterator.next();
			path.add(0, aux.getPosition());
		}
		
		return path;
	}

	/**
	 * This function opens all the adjacent nodes to the one given,
	 * unless they have already been visited
	 * @param node
	 * @param finalPos
	 * @param map
	 */
	private static void openAdjacentNodes (PriorityQueue<Node> openList, Node node, Int2D finalPos, KnowledgeMapInterface map, Map aState) {
		int x = node.getX();
		int y = node.getY();
		
		if (x > 0) {							//left
			openNode(openList, x - 1, y, node, finalPos, map, aState);
		}
		if (x < aState.getWidth() - 1) {		//right
			openNode(openList, x + 1, y, node, finalPos, map, aState);
		}
		if (y > 0) {							//up
			openNode(openList, x, y - 1, node, finalPos, map, aState);
			if (x > 0) {						//up left
				openNode(openList, x - 1, y - 1, node, finalPos, map, aState);
			}
			if (x < aState.getWidth() - 1) { 	//up right
				openNode(openList, x + 1, y - 1, node, finalPos, map, aState);
			}
		}
		if (y < aState.getHeight() - 1) { 		//down
			openNode(openList, x, y + 1, node, finalPos, map, aState);
			if (x > 0) {						//down left
				openNode(openList, x, y + 1, node, finalPos, map, aState);
			}
			if (x < aState.getWidth() - 1) { 	//down right
				openNode(openList, x, y + 1, node, finalPos, map, aState);
			}
		}
	}

	/**
	 * Opens a node if it has not been opened yet
	 * @param x: coordinate of the new node
	 * @param y: coordinate of the new node
	 * @param parent: parent node
	 * @param finalPos: where we are going
	 * @param map: the map containing useful information
	 */
	private static void openNode (PriorityQueue<Node> openList, int x, int y, Node parent, Int2D finalPos, KnowledgeMapInterface map, Map aState) {
		if (!aState.isVisited(x, y)) {
			int movementCost = getCost(map.getKnowledgeAt(new Int2D(x, y)));
			if (movementCost < IMPASSABLE) {
				addAndOpenNewNode(openList, 
					new Node (new Int2D(x, y), finalPos, movementCost + parent.getBaseCost(), parent, Node.DistanceMode.MANHATTAN), 
					aState);
			} else {
				aState.setVisited(x, y);
			}
		}
	}
	
	/**
	 * Gets the cost of moving to a tile 
	 * of the specified knowledge
	 * @param knowledge
	 * @return
	 */
	public static int getCost (Knowledge knowledge) {
		switch (knowledge) {
		case OBSTACLE:
			return IMPASSABLE;
		case EMPTY:
			return 1;
		case FLOWER:
			return 1;
		case BEE:
			return 2;
		case ENEMY:
			return IMPASSABLE;
		case UNKNOWN:
			return 1;
		case HIVE:
			return 1;
		default:
			break;
		}
		
		throw new IllegalArgumentException("Cost from unexistent knowledge being calculated: " + knowledge.toString());
	}
}

