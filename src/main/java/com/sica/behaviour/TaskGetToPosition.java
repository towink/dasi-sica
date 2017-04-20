package com.sica.behaviour;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;
import com.util.movement.Direction;
import sim.field.grid.Grid2D;
import sim.util.Int2D;

public class TaskGetToPosition implements Task {

	private Int2D destination;

	public TaskGetToPosition(Int2D destination) {
		this.destination = destination;
	}

	@Override
	public void interactWith(ObjectiveDrivenAgent a, SimulationState simState) {
		a.move(Direction.values()[simState.random.nextInt(4)], simState, Grid2D.TOROIDAL);
	}

	@Override
	public void endTask(ObjectiveDrivenAgent a, SimulationState simState) {
		//System.out.printf("Reached destination");
	}

	@Override
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		return simState.entities.getObjectLocation(a).equals(this.destination);
	}

}
