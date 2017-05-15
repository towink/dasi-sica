package com.sica.behaviour.Tasks;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.movement.PositioningFunctions;

import sim.util.Bag;
import sim.util.Int2D;

public class TaskAvoidEnemies extends TaskOneShot{

	private Int2D safeSpot;

	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {
		Int2D pos = simState.entities.getObjectLocation(a);
		Bag enemies = simState.entities.getRadialEntities(simState, pos, EntityType.SIMPLE_ENEMY);
		
		if (enemies.isEmpty())
			return;
		
		Int2D enemyPos = PositioningFunctions.getSwarmCentroid(simState, enemies, false, null);
		int dx = enemyPos.x - pos.x;
		int dy = enemyPos.y - pos.y;
		
		this.safeSpot = new Int2D(-dx*2 + pos.x, -dy*2 + pos.y);
		System.out.println("Vector: " + dx + "," + dy);
	}
	
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		//if there are enemies safespot wont be null
		if (safeSpot != null)
			obj.addTaskFirst(new TaskGetToPosition(safeSpot));
	}

}
