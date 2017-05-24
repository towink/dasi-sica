package com.sica.behaviour.common;

import com.sica.behaviour.TaskOneShot;
import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

public class TaskWarnEnemyDetected extends TaskOneShot {
	
	private Int2D where;
	
	public TaskWarnEnemyDetected(Int2D where) {
		this.where = where;
	}

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		Int2D pos = simState.entities.getObjectLocation(a);
		//Bag defendersBag = simState.entities.getRadialEntities(simState, pos, EntityType.DEFENDER_BEE);
		Bag defendersBag = simState.entities.getRadialEntities(simState, pos, EntityType.OBJECTIVE_DRIVEN_DEFENDER);
		if (!defendersBag.isEmpty()) {
			for (Object o: defendersBag) {
				((Agent) o).receiveBitOfKnowledge(this.where, Knowledge.ENEMY);
			}
		}
	}
	
}
