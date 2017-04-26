package com.sica.behaviour;

import java.util.LinkedList;
import java.util.Queue;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

/**
 * This class is intended to represent the goals of an ObjectiveDrivenAgent in a descriptive manner.
 * 
 * This means that an objective should rather represent the 'what?' than the 'how?'.
 * 
 * In an objective, tasks can be registered. If an agent is pursuing an objective, it will behave as defined
 * in these tasks. However, once the objective is achieved, the agent will stop executing the corresponding tasks.
 * 
 * Hints on implementing objectives:
 * 
 * You can put your objective classes which are specific to a certain type of agent (like Worker, Defender, ...)
 * as an inner classes into them. This has the following advantages:
 * - conceptually nice because the objective without the agent does not make sense
 * - objective has full control over agent, i.e. can access private members
 * If you do this, you do not necessarily have to use the parameter 'ObjectiveDrivenAgent a' in isFinished, etc.
 * 
 * The ObjectiveDrivenAgent has a priority queue mechanism for selecting the objective it will follow next. If you
 * want to use this mechanism, you will have to overwrite the getPriority method of this class.
 * 
 * @author Tobias
 * @author Daniel
 *
 */
public abstract class Objective implements Comparable<Objective> {

	protected Queue<Task> taskQueue;
	
	/**
	 * Create an objective, composed of an (initially empty) queue of tasks to be executed in order
	 */
	public Objective() {
		this.taskQueue = new LinkedList<Task>();
	}
	
	/**
	 * This method represents the 'what?' question, i.e. which state the agent/simulation has to reach in order to
	 * consider the objective as completed.
	 * @param a
	 * @param simState
	 * @return true if the agent has achieved this objective
	 */
	public abstract boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState);
	
	/**
	 * Can be overwritten to do stuff when the objective is completed.
	 * @param a
	 * @param simState
	 */
	public void onFinished(ObjectiveDrivenAgent a, SimulationState simState) {}
	
	/**
	 * Overwrite if you wish to use the ObjectiveDrivenAgent's priority mechanism.
	 * @return the priority of this objective
	 */
	public int getPriority() {
		return 0;
	}
	
	/**
	 * Make the given agent follow this objective, one step at a time,
	 * by executing the current task over the agent
	 * @param a
	 * @param simState
	 */
	public final void step(ObjectiveDrivenAgent a, SimulationState simState) {
		// remove tasks that are finished and call their endTask function
		this.removeFinishedTasks(a, simState);
		// execute next task in queue
		if (!this.isFinished(a, simState))
			taskQueue.peek().interactWith(a, simState);
	}

	/**
	 * removes all finished tasks from the queue and calls their endTasks funtions.
	 * @param a
	 * @param simState
	 */
	private void removeFinishedTasks(ObjectiveDrivenAgent a, SimulationState simState) {
		while (!this.taskQueue.isEmpty() && this.taskQueue.peek().isFinished(a, simState))
			this.taskQueue.poll().endTask(a, this, simState);
	}
	
	/**
	 * Adds a task to this objective.
	 * @param t
	 */
	public void addTask(Task t) {
		taskQueue.add(t);
	}
	
	@Override
	public final int compareTo(Objective other) {
		return Integer.compare(this.getPriority(), other.getPriority());
	}
	
}
