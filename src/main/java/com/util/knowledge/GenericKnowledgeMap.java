package com.util.knowledge;

import java.util.HashMap;
import java.util.Map.Entry;

import sim.util.Int2D;

public abstract class GenericKnowledgeMap implements KnowledgeMapInterface {
	
	protected HashMap<Knowledge, Boolean> knowledgeUpdatedTable;
	protected boolean hasNewKnowledge;
	
	protected void init() {
		this.knowledgeUpdatedTable = new HashMap<Knowledge, Boolean>();
		this.hasNewKnowledge = false;
	}
	
	@Override
	public boolean addKnowledge(Int2D where, Knowledge knowledge) {
		boolean added = this.doAddKnowledge(where, knowledge); 
		if (added) {
			this.knowledgeUpdatedTable.put(knowledge, true);
			this.hasNewKnowledge = true;
		}
		return added;
	}
	
	protected abstract boolean doAddKnowledge(Int2D where, Knowledge knowledge);
	
	@Override
	public boolean addKnowledge(KnowledgeMapInterface kMap) {
		boolean result = false;
		
		for (Entry<Int2D, Knowledge> e: kMap.getAllKnowledge()) {
			result |= this.addKnowledge(e.getKey(), e.getValue());
		}

		return result;
	}
	
	@Override
	public boolean updateKnowledge(Int2D where, Knowledge knowledge) {
		Knowledge remknowledge = removeKnowledge(where);
		this.addKnowledge(where, knowledge);
		return remknowledge != knowledge;
	}
	
	@Override
	public boolean updateKnowledge(KnowledgeMapInterface kMap) {
		boolean result = false;
		for (Entry<Int2D, Knowledge> e: kMap.getAllKnowledge()) {
			result |= this.updateKnowledge(e.getKey(), e.getValue());
		}
		return result;
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
	

}
