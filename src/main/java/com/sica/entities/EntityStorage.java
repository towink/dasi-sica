package com.sica.entities;

import java.util.EnumMap;

import com.sica.entities.Entity.EntityType;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class EntityStorage extends SparseGrid2D {

	private static final long serialVersionUID = 8716191153250328728L;
	
	private EnumMap<EntityType, Integer> agentQuantity;
	
	private int order = 0;

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
			schedule.scheduleOnce(schedule.getTime(), order, entity);
		else
			schedule.scheduleOnce(Schedule.EPOCH, order, entity);
		this.agentQuantity.put(entity.getType(), 1 + this.agentQuantity.getOrDefault(entity.getType(), 0));
		order++;
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
			schedule.scheduleRepeating(schedule.getTime(), order, entity);
		else
			schedule.scheduleRepeating(Schedule.EPOCH, order, entity);
		this.agentQuantity.put(entity.getType(), 1 + this.agentQuantity.getOrDefault(entity.getType(), 0));
		order++;
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
	
	/**
	 * get all entities of the given type around the given position,
	 * or get all entities of all types if type == null 
	 * @param simState
	 * @param pos
	 * @param type
	 * @return
	 */
	public Bag getRadialEntities(SimulationState simState, Int2D pos, EntityType type) {
		Bag enemies = simState.entities.getRadialNeighbors(pos.getX(), pos.getY(), simState.config.getRadioView(), SimulationConfig.ENV_MODE, true);
		if (type == null)
			return enemies;
		
		Bag res = new Bag();
		//retrieve only the interesting entities
		for (Object o: enemies)
			if (((Entity) o).getType() == type)
				res.add(o);
		
		return res;
	}
}
