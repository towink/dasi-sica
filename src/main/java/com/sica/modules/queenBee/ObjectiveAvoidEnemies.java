package com.sica.modules.queenBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskAvoidEnemies;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;

/**
 * Objective that represents the queens intention to avoid enemies.
 * 
 * We consider this objective to be finished when there are no enemies in range.
 * The objective is reached by randomly choosing a location to which the agent escapes.
 * 
 * @author Tobias
 *
 */
public class ObjectiveAvoidEnemies extends Objective {

	private boolean isFinished = false;
	
	public ObjectiveAvoidEnemies() {
		addTaskFirst(new TaskQueenAvoidEnemies());
	}
	
	@Override
	public int getPriority() {
		return VERY_HIGH_PRIORITY;
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return isFinished;
	}
	
	// Tasks specific to this objective
	
	private class TaskQueenAvoidEnemies extends TaskAvoidEnemies {
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			//if there are enemies safespot wont be null
			if (safeSpot != null) {
				obj.addTaskFirst(new TaskGetToPosition(safeSpot));
				obj.addTaskLast(new TaskQueenDecideWhatToDo());
			} else { //something went wrong, try again
				isFinished = true;
			}
		}
	}
	
	private class TaskQueenDecideWhatToDo extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			if(((QueenDrools)a).enemiesClose(simState)) {
				obj.addTaskFirst(new TaskQueenAvoidEnemies());
			} else {
				isFinished = true;
			}
		}
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		((QueenDrools)a).onObjectiveAvoidEnemies = false;
	}

}
