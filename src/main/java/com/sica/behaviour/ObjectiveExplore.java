package com.sica.behaviour;

import java.util.Random;

import com.sica.simulation.SimulationConfig;

import sim.util.Int2D;

/**
 * Represents the exploring activity of a worker bee.
 * At the moment just randomly picks points in the world and tries to
 * get to them, never finishes.
 * @author Tobias
 *
 */
public class ObjectiveExplore extends Objective {
	
	private Int2D randomPoint() {
		Random r = new Random();
		int x = r.nextInt(SimulationConfig.GRID_WIDTH - 1);
		int y = r.nextInt(SimulationConfig.GRID_HEIGHT - 1);
		Int2D res = new Int2D(x, y);
		return res;
	}

	
	public ObjectiveExplore() {
		
		// for testing, randomly choose a location where we want to get to initially	
		// enqueue the new corresponding task
		taskQueue.add(new TaskGetToPosition(randomPoint()));
	}
	
	@Override
	public void taskFinishedCallback(Task t) {
		
		taskQueue.add(new TaskGetToPosition(randomPoint()));
		
	}

	@Override
	public int getPriority() {
		return 0;
	}

}
