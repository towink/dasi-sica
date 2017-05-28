package com.sica.behaviour.common;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.modules.workerBee.TaskObserveEnvironmentObstacleFlower;
import com.sica.simulation.SimulationState;
import com.util.data.IterableSet;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class TaskGoToClosePlaceWithKnowledge extends TaskOneShot {

	private Knowledge knowledge;
	private boolean force;
	private Int2D decision, currPos;
	private float chance;
	
	/**
	 * Create a task that moves the agent towards the closest position (not necessarily if chance < 1.0)
	 * @param knowledge
	 * @param currPos
	 * @param force
	 * @param chance chance of choosing the closest place. 
	 */
	public TaskGoToClosePlaceWithKnowledge(Knowledge knowledge, Int2D currPos, boolean force, float chance) {
		this.knowledge = knowledge;
		this.currPos = currPos;
		this.force = force;
		this.chance = chance;
	}
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		IterableSet<Int2D> choices = a.getKnowledgeMap().getKnowledgeOf(knowledge);
		if (choices.isEmpty() && this.force) {
			throw new IllegalStateException("No position with knowledge " + this.knowledge + " known");
		}
		double distance = Float.MAX_VALUE;
		for (Int2D pos: choices) {
			if (decision == null) //at least get one
				decision = pos;
			if (currPos.distance(pos) < distance && simState.random.nextFloat() < chance) {
				distance = currPos.distance(pos);
				decision = pos;
			}
		}
	}
	
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		if (decision != null) {
			if (!decision.equals(currPos)) {
				obj.addTaskFirst(new TaskGetToPosition(decision));	
			} else { //something is failing
				obj.addTaskFirst(new TaskObserveEnvironmentObstacleFlower()); 
				obj.addTaskFirst(new TaskGetToPosition(a.getHome()));
			}
		}
	}

}