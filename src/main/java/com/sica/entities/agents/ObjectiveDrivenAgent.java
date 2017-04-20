package com.sica.entities.agents;

import java.util.PriorityQueue;

import com.sica.behaviour.Objective;
import com.sica.entities.Entity;
import com.sica.simulation.SimulationState;

public abstract class ObjectiveDrivenAgent extends Entity{
	private static final long serialVersionUID = -3670233969349917087L;

	private PriorityQueue<Objective> objectives;
	
	public ObjectiveDrivenAgent(EntityType type) {
		super(type);
		this.objectives = new PriorityQueue<Objective>();
	}

	@Override
	public void doStep(SimulationState simState) {
		if (!objectives.isEmpty()) {
			Objective current = objectives.peek();
			if (!current.isFinished(this, simState))
				current.step(this, simState);
			else
				objectives.poll();
		}
	}
	
	
	public void addObjective(Objective o) {
		this.objectives.add(o);
	}

}
