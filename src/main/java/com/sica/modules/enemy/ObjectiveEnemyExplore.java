package com.sica.modules.enemy;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskMoveTowardsPosition;
import com.sica.behaviour.common.TaskObserveEnvironment;
import com.sica.behaviour.common.TaskSleep;
import com.sica.entities.Entity;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

/**
 * Explore the world looking for a colony
 * 
 * @author Tobias
 *
 */
public class ObjectiveEnemyExplore extends Objective {

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return !a.getKnowledgeMap().getKnowledgeOf(Knowledge.HIVE).isEmpty();
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		((ObjectiveDrivenAgent)a).addObjective(new ObjectiveAttackHive());
	}
	
	private Int2D currDest;
	
	public ObjectiveEnemyExplore() {
		currDest = null;
		addTaskLast(new TaskDecideWhereToGo());
	}
	
	// TASKS
	
	private class TaskDecideWhereToGo extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			currDest = new Int2D(
					simState.random.nextInt(SimulationConfig.GRID_WIDTH), 
					simState.random.nextInt(SimulationConfig.GRID_HEIGHT));
			a.computePath(simState, currDest);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskLast(new TaskEnemyMoveTowardsPosition(currDest, 1));
		}
	}
	
	private class TaskEnemyMoveTowardsPosition extends TaskMoveTowardsPosition {
		public TaskEnemyMoveTowardsPosition(Int2D destination, int maxSteps) {
			super(destination, maxSteps);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskLast(new TaskAttackBeesOnSamePosition());
			obj.addTaskLast(new TaskObserveEnvironment(Knowledge.HIVE));
			// enemies move slower, so they sleep a while after each step they move
			obj.addTaskLast(new TaskSleep(10 - simState.getConfig().getEnemySpeed()));
			// if we reached the current position we decide a new one
			if(simState.entities.getObjectLocation(a).equals(currDest) 
					|| !a.canMoveTo(currDest, simState, SimulationConfig.ENV_MODE)) {
				obj.addTaskLast(new TaskDecideWhereToGo());
			}
			// otherwise we keep moving to the current position
			else {
				obj.addTaskLast(new TaskEnemyMoveTowardsPosition(currDest, 1));
			}
		}
	}
	
	private class TaskAttackBeesOnSamePosition extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			Bag bees = simState.entities.getObjectsAtLocation(simState.entities.getObjectLocation(a));
			for (Object o: bees) {
				Entity bee = (Entity) o;
				if (Entity.isBee(bee)) {
					bee.die(simState);
					a.die(simState);
					return;
				}
			}
		}
	}
	
}
