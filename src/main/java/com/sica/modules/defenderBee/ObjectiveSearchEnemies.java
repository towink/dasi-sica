package com.sica.modules.defenderBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.entities.Entity;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;


import sim.util.Bag;
import sim.util.Int2D;

/**
 * Look for enemies, and if any are found, add an objective to go hunt them
 * 
 * @author Daniel
 * @author David
 *
 */
public class ObjectiveSearchEnemies extends Objective {
	@Override
	public int getPriority() {
		return Objective.DEFAULT_PRIORITY;
	}

	private boolean foundEnemy = false;
	private boolean isFinished = false;
	private Int2D enemyPos;

	public ObjectiveSearchEnemies() {
		addTaskLast(new TaskScan4Enemies());
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return foundEnemy || isFinished;
	}

	@Override
	public void onFinished(Agent a, SimulationState simState) {
		if(foundEnemy) {
			//add an objetive with higher priority to pursue
			((ObjectiveDrivenAgent)a).addObjective(new ObjectiveAttackEnemy(enemyPos, Objective.HIGH_PRIORITY));
		} 
		((ObjectiveDrivenAgent)a).addObjective(new ObjectiveExploreTrench());
		
	}
	
	
	//TASKS

	private class TaskScan4Enemies extends TaskOneShot {
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
					foundEnemy = true;
					break;
				}
			}
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			isFinished = true; //finish the objective and do something else
		}
	}
}
