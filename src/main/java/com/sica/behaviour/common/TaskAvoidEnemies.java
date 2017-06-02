package com.sica.behaviour.common;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.movement.PositioningFunctions;

import sim.util.Bag;
import sim.util.Int2D;

public class TaskAvoidEnemies extends TaskOneShot{

	protected Int2D safeSpot;

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		Int2D pos = simState.entities.getObjectLocation(a);
		Bag enemies = simState.entities.getRadialEntities(simState, pos, EntityType.ENEMY);
		
		if (enemies.isEmpty())
			return;
		
		Int2D enemyPos = PositioningFunctions.getSwarmCentroid(simState, enemies, false, null);
		int dx = enemyPos.x - pos.x;
		int dy = enemyPos.y - pos.y;
		
		safeSpot = new Int2D(-dx*5 + pos.x, -dy*5 + pos.y);
		safeSpot = PositioningFunctions.findValidPosition(safeSpot, 10, enemyPos, a.getKnowledgeMap(), simState.random);
		System.out.println("safe spot: " + safeSpot);
	}
	
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		//if there are enemies safespot wont be null
		if (safeSpot != null)
			obj.addTaskFirst(new TaskGetToPosition(safeSpot));
	}

}
