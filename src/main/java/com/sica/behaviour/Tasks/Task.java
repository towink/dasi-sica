package com.sica.behaviour.Tasks;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

/**
 * Objectives are made up of tasks. The tasks themselves can be regarded as sub-objectives as well because they also
 * dispose of the (descriptive) isFinished method. The main difference is that tasks encapsulate behavior while
 * objectives do not.
 * 
 * Hints on implementing tasks:
 * 
 * Consider to put your task classes as inner classes into the agent (if the task is specific for this agent) or even
 * into the objective it belongs to. In these cases, you will not need the parameters 'ObjectDrivenAgent a' and
 * 'Objective obj', respectively.
 * 
 * For fairly simple tasks that are always executed in one step of the Mason simulation, there is the convenience class
 * OneShotTask.
 * 
 * However, in order to enforce reusability, try to keep tasks as general as possible and declare them as public classes
 * so that other objectives and even other agent can make use of them (an example is TaskGetToPosition).
 * 
 * @author Tobias
 *
 */
public abstract class Task {
	
	/**
	 * Make the agent perform this task. All logic needed for the execution should be stored within the task, as the
	 * agent might decide to change objectives and other tasks could modify its state before this one is completed
	 * @param a
	 * @param simState
	 */
	public abstract void interactWith(Agent a, SimulationState simState);
	
	/**
	 * Called by the Objective when the task is finished. Might be useful to set up stuff
	 * @param a
	 * @param obj A reference to the objective this task is part of
	 */
	public void endTask(Agent a, Objective obj, SimulationState simState) {}
	
	/**
	 * @return true if the task has finished executing
	 */
	public abstract boolean isFinished(Agent a, SimulationState simState);
	
}
