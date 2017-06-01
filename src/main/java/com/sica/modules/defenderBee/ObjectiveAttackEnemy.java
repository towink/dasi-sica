package com.sica.modules.defenderBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.Entity;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Bag;
import sim.util.Int2D;

/**
 * Objective that makes the agent go towards a position, look for an enemy, and
 * attack if one is found
 * 
 * @author Daniel
 * @author David
 *
 */
public class ObjectiveAttackEnemy extends Objective {

	@Override
	public int getPriority() {
		return this.priority;
	}

	private boolean isFinished = false;
	private int priority;
	
	/**
	 * Give a priority to the objective if we want it to be executed before others
	 * @param where
	 * @param priority
	 */
	public ObjectiveAttackEnemy(Int2D where, int priority) {
		addTaskLast(new TaskCheckIfWorthPursuing());
		addTaskLast(new TaskGetToPosition(where));
		addTaskLast(new TaskAttack());
		this.priority = priority;
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return isFinished;
	}
	
	/**
	 * Check if this objective is worth pursuing. Look around you, and see how many 
	 * other bees surround you. If there are a lot, just hope that someone else will to the job for you,
	 * or take initiative and go kill the enemies
	 * 
	 * @author Daniel
	 *
	 */
	private class TaskCheckIfWorthPursuing extends TaskOneShot {

		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			if (!worthIt)
				isFinished = true;
		}

		private boolean worthIt = false;
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			int defCount = simState.entities.getNumberOf(a.getType());
			float prob = 3.0f / (float) defCount; //three bees should go after them
			if (simState.random.nextFloat() < prob)
				worthIt = true;
		}
		
	}
	
	/**
	 * Go and try attacking the enemies. This agent can die in the process
	 * 
	 * @author David
	 *
	 */
	private class TaskAttack extends TaskOneShot {

		private boolean killed = false;
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			//get all agents at this location
			Bag entityBag = simState.entities.getObjectsAtLocationOfObject(a);
			//find enemies and kill them
			for (Object o: entityBag) {
				Entity entity = (Entity) o;
				if (Entity.isEnemy(entity)) {
					entity.die(simState);
					if (simState.random.nextFloat() < SimulationConfig.config().getDefenderAttackDeathChance())
						a.die(simState);
					break;
				}
			}
		}
		
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			if (!killed) {	//keep looking just in case
				obj.addTaskLast(new TaskScan4Enemies());
			} else {
				obj.addTaskFirst(new TaskFinishObjective(a.getHome()));
			}
		}

	}
	
	
	/**
	 * Check around to see if there are still enemies around you
	 * 
	 * @author David
	 * @author Daniel
	 *
	 */
	private class TaskScan4Enemies extends TaskOneShot {

		private Int2D enemyPos;
		

		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			//just scan for enemies, if none are found, reevaluate your trench
			Int2D location = simState.entities.getObjectLocation(a);
			//TODO maybe the radius, mode, etc are agent independent?
			Bag entityBag = simState.entities.getRadialNeighbors(location.x, location.y, SimulationConfig.config().getRadioView(), SimulationConfig.ENV_MODE, true);

			//look through all agents to see if you find an enemy
			for (Object o: entityBag) {
				Entity entity = (Entity) o;
				if (Entity.isEnemy(entity)) {
					enemyPos = simState.entities.getObjectLocation(entity);
					a.computePath(simState, enemyPos);
					break;
				}
			}
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			if (enemyPos != null) {
				obj.addTaskLast(new TaskGetToPosition(enemyPos));
				obj.addTaskLast(new TaskAttack());
			}
			else {
				obj.addTaskFirst(new TaskFinishObjective(a.getHome()));
			}
		}
	}
	
	
	/**
	 * Go to a position then mark the objective as finished so that objectives with less priority can keep 
	 * executing
	 * 
	 * @author Daniel
	 *
	 */
	private class TaskFinishObjective extends TaskGetToPosition {

		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			isFinished = true;
		}

		public TaskFinishObjective(Int2D destination) {
			super(destination);
		}
	}

}
