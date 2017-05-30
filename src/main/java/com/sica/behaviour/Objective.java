package com.sica.behaviour;

import java.util.Deque;
import java.util.LinkedList;

import com.sica.entities.agents.Agent;
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

	protected Deque<Task> taskQueue;
	private Task currentTask;
	
	
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
	public abstract boolean isFinished(Agent a, SimulationState simState);
	
	/**
	 * Can be overwritten to do stuff when the objective is completed.
	 * @param a
	 * @param simState
	 */
	public void onFinished(Agent a, SimulationState simState) {}
	
	/**
	 * Overwrite if you wish to use the ObjectiveDrivenAgent's priority mechanism.
	 * This should NOT change after creating the objective. If you wish to change it,
	 * finish this objective and then add a new one with the new priority,
	 * as ObjectiveDrivenAgent won't reorder its objectives after the first insertion.
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
	public final void step(Agent a, SimulationState simState) {
		//if we have no current task
		if (currentTask == null) {
			if (taskQueue.isEmpty()) {
				throw new IllegalStateException("This objective has no tasks and it is not finished either! Fix it!");
			} else {
				currentTask = taskQueue.pollFirst();//get a new task
				currentTask.startTask(a, simState);
			}
		}
		
		if (!currentTask.isFinished(a, simState)) {	//if we've not finished the task, interact with it
			currentTask.interactWith(a, simState);
		} else {									//otherwise end it
			//do it this way to ensure the exception	
			//at addTask is not triggered if the task
			//spawns new ones
			Task task = currentTask;
			currentTask = null;
			task.endTask(a, this, simState);
		} 
	}
	
	/**
	 * Adds a task to this objective after all the others
	 * @param t
	 */
	public void addTaskLast(Task t) {
		taskQueue.addLast(t);
	}
	
	/**
	 * Adds a task to this objetive befor all the others
	 * @param t
	 */
	public void addTaskFirst(Task t) {
		if (currentTask != null)
			throw new IllegalStateException("Can't add a task while another is still being executed!!");
		taskQueue.addFirst(t);
	}
	
	@Override
	public final int compareTo(Objective other) {
		return Integer.compare(this.getPriority(), other.getPriority());
	}
	
	
	/**
	 * Getter just out of curiosity
	 * @return
	 */
	public int getTaskListLength() {
		return this.taskQueue.size();
	}
	
}
