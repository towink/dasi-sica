package com.util.knowledge;

import java.util.Collection;
import java.util.Map.Entry;

import sim.util.Int2D;

/**
 * Interface for defining a knowledge map for an agent, which will be 
 * comprised of positions along with what the agent knows is at that 
 * position.
 * ¡¡Please note that MULTIPLE KNOWLEDGE AT THE SAME POSITION IS NOT
 * GUARANTEED!!!
 * @author Daniel
 *
 */
public interface KnowledgeMapInterface {
	
	/**
	 * Adds knowledge of the environment in a certain position
	 * @param where
	 * @param knowledge
	 * @return true if knowledge was added (i.e: it didn't exist before)
	 */
	public boolean addKnowledge(Int2D where, Knowledge knowledge);
	
	/**
	 * Adds all knowledge contained in the given knowledge map
	 * @param kMap
	 * @return true if knowledge was added
	 */
	public boolean addKnowledge(KnowledgeMapInterface kMap);
	
	/**
	 * Removes this knowledge from all places where it is found
	 * @param knowledge
	 */
	public void removeKnowledge(Knowledge knowledge);
	
	/**
	 * Remove all knowledge at the specified place
	 * @param where
	 */
	public void removeKnowledge(Int2D where);
	
	/**
	 * Removes all knowledge contained in this object
	 */
	public void removeAllKnowledge();
	
	/**
	 * @param where
	 * @return the knowledge stored in this map at the given position
	 */
	public Knowledge getKnowledgeAt(Int2D where);
	
	/**
	 * @param knowledge
	 * @return All the positions where the given knowledge is found
	 */
	public Collection<Int2D> getKnowledgeOf(Knowledge knowledge);
	
	/**
	 * @return all the knowledge within this map
	 */
	public Collection<Entry<Int2D, Knowledge>> getAllKnowledge();
	
	/**
	 * @return true if any knowledge changed since the last time pollNewKnowledge was called
	 */
	public boolean peekNewKnowledge();
	
	/**
	 * @return true if the knowledge specified changed since the last time pollNewKnowledge was called
	 */
	public boolean peekNewKnowledge(Knowledge knowledge);
	
	/**
	 * @return true if the knowledge changed since the last time pollNewKnowledge was called
	 * This function also resets the knowledge, setting it all as already known
	 */
	public boolean pollNewKnowledge();
	
}
