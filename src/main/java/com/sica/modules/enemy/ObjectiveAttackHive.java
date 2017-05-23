package com.sica.modules.enemy;

import com.sica.behaviour.Objectives.Objective;
import com.sica.behaviour.Tasks.TaskGetToPosition;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.data.IterableSet;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveAttackHive extends Objective {
	
	public ObjectiveAttackHive() {
		addTaskLast(new TaskPrepareToAttack());
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return isFinished;
	}
	
	private boolean isFinished = false;
	
	// TASKS

	private class TaskPrepareToAttack extends TaskOneShot {

		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			IterableSet<Int2D> hives = a.getKnowledgeMap().getKnowledgeOf(Knowledge.HIVE);
			//go to the first known hive
			Int2D hivePos = hives.iterator().next();
			// they will not have the sleep delays when on this task!
			addTaskLast(new TaskGetToPosition(hivePos));
			addTaskLast(new TaskAttack());
			
		}
	}
	
	private class TaskAttack extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			System.out.println("Attacked at " + simState.entities.getObjectLocation(a));
			simState.environment.set(simState.entities.getObjectLocation(a), Knowledge.EMPTY);
			// we managed to attack, so the objective can be regarded as finished
			isFinished = true;
		}
	}
}
