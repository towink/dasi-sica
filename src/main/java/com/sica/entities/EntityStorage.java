package com.sica.entities;

import java.util.EnumMap;

import com.sica.entities.Entity.EntityType;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class EntityStorage extends SparseGrid2D {

	private static final long serialVersionUID = 8716191153250328728L;
	
	private EnumMap<EntityType, Integer> agentQuantity;

	public EntityStorage(int width, int height) {
		super(width, height);
		this.agentQuantity = new EnumMap<EntityType, Integer>(EntityType.class);
	}

	/**
	 * Add an entity which will only fire once its step method
	 * @param entity
	 * @param location
	 * @param schedule
	 */
	public void addScheduledOnceEntityAt(Entity entity, Int2D location, Schedule schedule) {
		this.setObjectLocation(entity, location);
		if (schedule.getTime() > 0.0)
			schedule.scheduleOnce(schedule.getTime(), 0, entity);
		else
			schedule.scheduleOnce(Schedule.EPOCH, 0, entity);
		this.agentQuantity.put(entity.getType(), 1 + this.agentQuantity.getOrDefault(entity.getType(), 0));
	}
	
	/**
	 * Add an entity which will repeatedly fire its step method,
	 * once each step
	 * @param entity
	 * @param location
	 * @param schedule
	 */
	public void addScheduledRepeatingEntityAt(Entity entity, Int2D location, Schedule schedule) {
		this.setObjectLocation(entity, location);
		if (schedule.getTime() > 0.0)
			schedule.scheduleRepeating(schedule.getTime(), 0, entity);
		else
			schedule.scheduleRepeating(Schedule.EPOCH, 0, entity);
		this.agentQuantity.put(entity.getType(), 1 + this.agentQuantity.getOrDefault(entity.getType(), 0));
	}
	
	
	@Override
	public Object remove(Object arg0) {
		//should always be of type entity but just in case
		if (arg0 != null && arg0 instanceof Entity) {
			Entity e = (Entity) arg0;
			//this should never break unless entities are being added from other than addScheduled(...). If an exception is thrown here 
			//put a breakpoint, check the type of entity, and check that it is not added from somewhere else
			EntityType type = e.getType();
			int quantity = this.agentQuantity.get(type);
			this.agentQuantity.put(type, quantity - 1);
		}
		return super.remove(arg0);
	}
	
	/**
	 * @param type
	 * @return the number of entities with the given type
	 */
	public int getNumberOf(EntityType type) {
		return this.agentQuantity.getOrDefault(type, 0);
	}
}
