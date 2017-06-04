package com.sica.modules.enemy;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskMoveTowardsPosition;
import com.sica.behaviour.common.TaskSleep;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;
import com.util.data.IterableSet;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;


/**
 * Attack the hive, destroying it so that bees cannot exchange food
 * 
 * @author Tobias
 *
 */
public class ObjectiveAttackHive extends Objective {
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		ObjectiveDrivenAgent oda = ((ObjectiveDrivenAgent) a); 
		oda.addObjective(new ObjectiveEnemyExplore());
	}

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
			addTaskLast(new TaskEnemyAttackTowardsPosition(hivePos, 1));
		}
	}
	
	private class TaskEnemyAttackTowardsPosition extends TaskMoveTowardsPosition {
		public TaskEnemyAttackTowardsPosition(Int2D destination, int maxSteps) {
			super(destination, maxSteps);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			if (simState.entities.getObjectLocation(a).equals(destination)) {
				addTaskLast(new TaskAttack());
			} else {
				obj.addTaskLast(new TaskSleep(5));
				obj.addTaskLast(new TaskEnemyAttackTowardsPosition(destination, 1));
			}
		}
	}
	
	private class TaskAttack extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			Int2D loc = simState.entities.getObjectLocation(a);
			System.out.println("Attacked at " + loc);
			simState.environment.set(loc, Knowledge.EMPTY);
			a.getKnowledgeMap().removeKnowledge(loc);
			// we managed to attack, so the objective can be regarded as finished
			isFinished = true;
		}
	}
}
