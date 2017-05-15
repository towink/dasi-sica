package com.sica.entities;

import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.AgentFactory;
import sim.engine.Schedule;
import sim.util.Int2D;

public class EntityPlacer {
	
	/**
	 * creates and puts an entity in the specified position
	 * at the current schedule time, or EPOCH if it is not yet initialized
	 * @param type
	 * @param entities
	 * @param where
	 * @param schedule
	 */
	public static void deployEntity(EntityType type, EntityStorage entities, Int2D where, Schedule schedule) {
		Entity e = AgentFactory.getAgent(type);
		entities.addScheduledRepeatingEntityAt(e, where, schedule);
	}
	
	/**
	 * creates and puts a number of entities at the given location and current
	 * schedule time, or EPOCH if it is not yet initialized
	 * @param type
	 * @param entities
	 * @param where
	 * @param schedule
	 * @param count
	 */
	public static void deployEntities(EntityType type, EntityStorage entities, Int2D where, Schedule schedule, int count) {
		for (int x = 0; x < count; x++) {
			Entity e = AgentFactory.getAgent(type);
			entities.addScheduledRepeatingEntityAt(e, where, schedule);
		}
	}
	
}
