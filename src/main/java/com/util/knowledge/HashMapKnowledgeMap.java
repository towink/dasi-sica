package com.util.knowledge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import sim.util.Int2D;

public class HashMapKnowledgeMap extends GenericKnowledgeMap {
	
	private HashMap<Int2D, Knowledge> positionKnowledge;
	private HashMap<Knowledge, HashSet<Int2D>> knowledgePosition;
	private HashMap<Knowledge, Boolean> knowledgeUpdatedTable;
	private boolean hasNewKnowledge;
	
	public HashMapKnowledgeMap() {
		this.init();
	}
	
	
	private void init() {
		this.positionKnowledge = new HashMap<Int2D, Knowledge>();
		this.knowledgePosition = new HashMap<Knowledge, HashSet<Int2D>>();
		this.knowledgeUpdatedTable = new HashMap<Knowledge, Boolean>();
		this.hasNewKnowledge = false;
	}

	@Override
	public boolean addKnowledge(Int2D where, Knowledge knowledge) {
		if (this.positionKnowledge.containsKey(where))
			if (this.positionKnowledge.get(where) != knowledge) {
				throw new IllegalStateException("Cannot store different knowledges about the same position. Current: " + this.positionKnowledge.get(where) + " New:" + knowledge);
			}
			else {
				return false;	//knowledge already present
			}
		
		this.positionKnowledge.put(where, knowledge);
		//if this is the first time adding this knowledge, we need to create its hashset
		if (this.knowledgePosition.containsKey(knowledge)) {
			this.knowledgePosition.get(knowledge).add(where);
		} 
		else {
			HashSet<Int2D> hs = new HashSet<Int2D>();
			hs.add(where);
			this.knowledgePosition.put(knowledge, hs);
		}
		
		this.knowledgeUpdatedTable.put(knowledge, true);
		this.hasNewKnowledge = true;
		return true;
	}	

	@Override
	public void removeKnowledge(Knowledge knowledge) {
		if (this.knowledgePosition.containsKey(knowledge)) {
			for (Int2D i2d: this.knowledgePosition.remove(knowledge)) {
				this.positionKnowledge.remove(i2d);
			}
		}
	}

	@Override
	public Knowledge removeKnowledge(Int2D where) {
		if (this.positionKnowledge.containsKey(where)) {
			Knowledge knowledge = this.positionKnowledge.remove(where);
			this.knowledgePosition.get(knowledge).remove(where);
			return knowledge;
		}
		return null;
	}

	@Override
	public void removeAllKnowledge() {
		this.init();
	}

	@Override
	public Knowledge getKnowledgeAt(Int2D where) {
		return this.positionKnowledge.getOrDefault(where, Knowledge.UNKNOWN);
	}
	
	@Override
	public Knowledge getKnowledgeAt(int x, int y) {
		return this.getKnowledgeAt(new Int2D(x, y));
	}

	@Override
	public Collection<Int2D> getKnowledgeOf(Knowledge knowledge) {
		return Collections.unmodifiableCollection(this.knowledgePosition.getOrDefault(knowledge, new HashSet<Int2D>()));
	}

	@Override
	public Collection<Entry<Int2D, Knowledge>> getAllKnowledge() {
		return Collections.unmodifiableCollection(this.positionKnowledge.entrySet());
	}
	
	@Override
	public boolean peekNewKnowledge() {
		return this.hasNewKnowledge;
	}

	@Override
	public boolean peekNewKnowledge(Knowledge knowledge) {
		if (!this.knowledgeUpdatedTable.containsKey(knowledge)) {
			return false;
		}
		return this.knowledgeUpdatedTable.get(knowledge);
	}


	@Override
	public boolean pollNewKnowledge() {
		boolean tmp = this.hasNewKnowledge;
		this.hasNewKnowledge = false;
		//also clear specific values
		for (Entry<Knowledge, Boolean> e: this.knowledgeUpdatedTable.entrySet()) {
			e.setValue(false);
		}
			
		return tmp;
	}

	@Override
	public String toString () {
		String value = "Knowledge {";
		value += "\n\tHives: " + this.knowledgePosition.getOrDefault(Knowledge.HIVE, new HashSet<Int2D>()).size();
		value += "\n\tFlowers: " + this.knowledgePosition.getOrDefault(Knowledge.FLOWER, new HashSet<Int2D>()).size();
		value += "\n\tObstacles: " + this.knowledgePosition.getOrDefault(Knowledge.OBSTACLE, new HashSet<Int2D>()).size();
		value += "\n}";
		return value;
	}



	
}
