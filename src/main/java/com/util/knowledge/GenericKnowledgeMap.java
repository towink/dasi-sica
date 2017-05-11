package com.util.knowledge;

import java.util.Map.Entry;

import sim.util.Int2D;

public abstract class GenericKnowledgeMap implements KnowledgeMapInterface {
	
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
	
	

}
