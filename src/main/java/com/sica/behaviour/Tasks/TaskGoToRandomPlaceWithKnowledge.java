package com.sica.behaviour.Tasks;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class TaskGoToRandomPlaceWithKnowledge extends TaskOneShot {

	private Knowledge knowledge;
	private boolean force;
	private Int2D decision;
	
	/**
	 * Create a task which makes the agent go to a random
	 * known place of the given knowledge
	 * set force=true to throw an exception if the agent doesn't know where to go,
	 * otherwise it will just ignore this task
	 * @param knowledge
	 * @param force
	 */
	public TaskGoToRandomPlaceWithKnowledge(Knowledge knowledge, boolean force) {
		this.knowledge = knowledge;
		this.force = force;
	}
	
	@Override
	public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
		decision = a.getKnowledgeMap().getRandomPositionOfKnowledge(knowledge, simState.random);
		if (decision == null && this.force) {
			throw new IllegalStateException("No position with knowledge " + this.knowledge + " known");
		}
	}
	
	@Override
	public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
		if (decision != null)
			obj.addTaskFirst(new TaskGetToPosition(decision));
	}

}


/**

/**

	// TODO what is the criterion to decide where to go next? at the moment random ...
	private class TaskDecideWhereToGo extends TaskOneShot {
		private Int2D decision;
		@Override
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			decision = a.getKnowledgeMap().getRandomPositionOfKnowledge(Knowledge.FLOWER, simState.random);
			if (decision == null) {
				throw new IllegalStateException("no flower pos known");
			}
		}
		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
			addTask(new TaskGetToPosition(decision));
			addTask(new TaskGrabAlimentFromCurrentPos());
		}
	}
*/
