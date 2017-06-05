package com.sica.modules.eater;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskMoveRandomly;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;
import sim.util.IntBag;

public class ObjectiveSearchForFlowers extends Objective {
	public ObjectiveSearchForFlowers() {
		this.addTaskLast(new TaskExplore());
	}
	
	@Override
	public int getPriority() {
		return Objective.VERY_LOW_PRIORITY;
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		return false;
	}

	private class TaskExplore extends TaskOneShot {
		private Int2D objective = null;
		
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			IntBag xCoords = new IntBag(), yCoords = new IntBag();
			Int2D loc = simState.entities.getObjectLocation(a);
			simState.environment.getRadialNeighbors(loc.x, loc.y, 5, 
					SimulationConfig.ENV_MODE, true, Knowledge.FLOWER, xCoords, yCoords);
			if (!xCoords.isEmpty())
				objective = new Int2D(xCoords.pop(), yCoords.pop());
		}
		
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskLast(new TaskMoveRandomly(5));
			obj.addTaskLast(new TaskExplore());
			if (objective != null) {
				ObjectiveDrivenAgent oda = (ObjectiveDrivenAgent) a;
				oda.addObjective(new ObjectiveEatFlowerAt(objective));
			}
		}
	}
}

	