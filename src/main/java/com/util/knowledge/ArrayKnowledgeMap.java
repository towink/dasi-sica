package com.util.knowledge;

import java.util.Collection;
import java.util.Map.Entry;

import sim.util.Int2D;

public class ArrayKnowledgeMap extends GenericKnowledgeMap {
	
	private Knowledge[][] knowledgeMap;
	private int width;
	private int height;
	
	/**
	 * Creates a knowledge map using an array. This is extremely fast for position
	 * lookups and writes, but very slow for other purposes
	 * @param width
	 * @param height
	 */
	public ArrayKnowledgeMap(int width, int height) {
		this.width = width;
		this.height = height;
		this.setUpKnowledgeMap();
	}
	
	private void setUpKnowledgeMap() {
		this.knowledgeMap = new Knowledge[this.width][this.height];
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				this.knowledgeMap[i][j] = Knowledge.UNKNOWN;
	}

	@Override
	public boolean addKnowledge(Int2D where, Knowledge knowledge) {
		Knowledge current = this.getKnowledgeAt(where);
		if (current != Knowledge.UNKNOWN) {
			if (current != knowledge) {
				throw new IllegalStateException("Cannot store different knowledges about the same position. Current: " + current + " New:" + knowledge);
			} else {
				return false;
			}
		}
		this.knowledgeMap[where.x][where.y] = knowledge;
		return true;
	}


	@Override
	public void removeKnowledge(Knowledge knowledge) {
		for (int i = 0; i < this.width; i++)
			for (int j = 0; j < this.height; j++)
				if (this.knowledgeMap[i][j] == knowledge)
					this.knowledgeMap[i][j] = Knowledge.UNKNOWN;
	}

	@Override
	public Knowledge removeKnowledge(Int2D where) {
		Knowledge last = this.knowledgeMap[where.x][where.y];
		this.knowledgeMap[where.x][where.y] = Knowledge.UNKNOWN;
		return last;
	}

	@Override
	public void removeAllKnowledge() {
		this.setUpKnowledgeMap();
	}

	@Override
	public Knowledge getKnowledgeAt(Int2D where) {
		return this.knowledgeMap[where.x][where.y];
	}

	@Override
	public Knowledge getKnowledgeAt(int x, int y) {
		return this.knowledgeMap[x][y];
	}

	@Override
	public Collection<Int2D> getKnowledgeOf(Knowledge knowledge) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Entry<Int2D, Knowledge>> getAllKnowledge() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean peekNewKnowledge() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean peekNewKnowledge(Knowledge knowledge) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pollNewKnowledge() {
		// TODO Auto-generated method stub
		return false;
	}

}
