package com.sica.modules.defenderBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.Entity;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;

import sim.util.Bag;
import sim.util.Int2D;

public class ObjectiveAttackEnemy extends Objective {
	private boolean isFinished = false;
	private Int2D enemyPos;
	
	public ObjectiveAttackEnemy(Int2D where) {
		this.enemyPos = where;
		addTaskLast(new TaskGetToPosition(enemyPos));
		addTaskLast(new TaskAttack());		
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return isFinished;
	}

	@Override
	public void onFinished(Agent a, SimulationState simState) {
		//Search for enemies again
		((ObjectiveDrivenAgent)a).addObjective(new ObjectiveSearchEnemies());
	}
	//TASKS
	
	private class TaskAttack extends TaskOneShot {
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			Bag entityBag = simState.entities.getObjectsAtLocationOfObject(a);
			//find enemies and kill them
			for (Object o: entityBag) {
				Entity entity = (Entity) o;
				if (Entity.isEnemy(entity)) {
					entity.die(simState);
				}
			}
			isFinished = true;
		}
	}
}
