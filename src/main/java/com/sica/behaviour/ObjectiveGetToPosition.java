package com.sica.behaviour;

import sim.util.Int2D;

public class ObjectiveGetToPosition extends Objective {

	private Int2D destination;
	
	public ObjectiveGetToPosition(Int2D destination) {
		this.destination = destination;
		this.taskQueue.add(new TaskGetToPosition(this.destination));
	}


	@Override
	public int getPriority() {
		return 0;
	}

}
