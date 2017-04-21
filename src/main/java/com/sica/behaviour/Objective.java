package com.sica.behaviour;

import java.util.LinkedList;
import java.util.Queue;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

public abstract class Objective implements Comparable<Objective>{

	protected Queue<Task> taskQueue;
	
	/**
	 * Create an objective, composed of a queue of tasks to be executed in order
	 * @param tl
	 */
	public Objective() {
		this.taskQueue = new LinkedList<Task>();
	}
	
	/**
	 * Make the given agent follow this objective, one step at a time,
	 * by executing the current task over the agent
	 * @param a
	 * @param simState
	 */
	public void step(ObjectiveDrivenAgent a, SimulationState simState) {
		if (!this.isFinished(a, simState))
			taskQueue.peek().interactWith(a, simState);
	}

	/**
	 * @return true if all tasks in this objective have been completed
	 */
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		this.removeFinishedTasks(a, simState);
		return this.taskQueue.isEmpty();
	}

	/**
	 * removes all finished tasks from the queue
	 * @param a
	 * @param simState
	 */
	private void removeFinishedTasks(ObjectiveDrivenAgent a, SimulationState simState) {
		while (!this.taskQueue.isEmpty() && this.taskQueue.peek().isFinished(a, simState))
			this.taskQueue.poll().endTask(a, this, simState);
	}
	
	/**
	 * Tasks in this objective can call this method when they are finished
	 * @param t
	 */
	public void taskFinishedCallback(Task t) {};
	
	/**
	 * Adds a task to this objective
	 * @param t
	 */
	public void addTask(Task t) {
		taskQueue.add(t);
	}
	
	/**
	 * @return the priority of the objective
	 */
	public abstract int getPriority();
	
	@Override
	public int compareTo(Objective other) {
		return Integer.compare(this.getPriority(), other.getPriority());
	}
	
}
