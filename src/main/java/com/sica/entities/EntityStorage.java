package com.sica.entities;

import sim.engine.Schedule;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class EntityStorage extends SparseGrid2D {
	private static final long serialVersionUID = 8716191153250328728L;

	public EntityStorage(int width, int height) {
		super(width, height);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Add an entity which will only fire once its step method
	 * @param entity
	 * @param location
	 * @param schedule
	 */
	public void addScheduledOnceEntityAt(Entity entity, Int2D location, Schedule schedule) {
		this.setObjectLocation(entity, location);
		schedule.scheduleOnce(Schedule.EPOCH, 0, entity);
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
		schedule.scheduleRepeating(Schedule.EPOCH, 0, entity, 1);
	}
}
