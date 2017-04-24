package com.util.searching;

import java.util.Iterator;

public class NodeIterator implements Iterator<Node>{

	private Node node;
	
	public NodeIterator(Node node) {
		this.node = node;
	}
	
	@Override
	public boolean hasNext() {
		if (node.getParentNode() != null) {
			return true;
		}
		
		return false;
	}

	@Override
	public Node next() {
		Node aux = node;
		node = node.getParentNode();
		return aux;
	}

}
