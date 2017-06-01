package com.sica.entities.agents;

import java.util.List;
import java.util.PriorityQueue;

import com.sica.behaviour.Objective;
import com.sica.simulation.SimulationState;
import sim.util.Int2D;

/**
 * Agent whose behavior is controlled by objectives.
 * 
 * @author Tobias
 *
 */
public abstract class ObjectiveDrivenAgent extends Agent {
	private static final long serialVersionUID = -3670233969349917087L;

	protected PriorityQueue<Objective> objectives;
	/**
	 * Creates an agent with a initially empty knowledge and without any objectives.
	 * @param type The entity type of the agent to be created.
	 */
	public ObjectiveDrivenAgent(EntityType type) {
		super(type);
		this.objectives = new PriorityQueue<Objective>();
	}
	
	/**
	 * Called on each step in the MASON simulation. It is not intended that subclasses
	 * overwrite this method.
	 * This function manages the execution and termination of the agent's objectives.
	 * @param simState
	 */
	@Override
	public final void agentDoStep(SimulationState simState) {
		if (!objectives.isEmpty()) {
			Objective current = objectives.peek();
			if (!current.isFinished(this, simState)) {
				// objective is not finished
				current.step(this, simState);
			}
			else {
				// objective is finished
				objectives.poll();
				current.onFinished(this, simState);
			}
		}
		// if there are no objectives do not do anything,
		// just wait until we are assigned one
	}
	
	/**
	 * Adds an objective to the queue of objectives of this agent.
	 * @param o The objective to be added
	 */
	public void addObjective(Objective o) {
		this.objectives.add(o);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Int2D> getActualPath() {
		return actualPath;
	}

}
