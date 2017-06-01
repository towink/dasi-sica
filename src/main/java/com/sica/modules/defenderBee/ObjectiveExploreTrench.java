package com.sica.modules.defenderBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.movement.PositioningFunctions;
import sim.util.Int2D;

public class ObjectiveExploreTrench extends Objective {
	@Override
	public int getPriority() {
		return 10;
	}

	private Int2D objective;
	private boolean isFinished = false;

	public ObjectiveExploreTrench() {
		objective = null;
		addTaskLast(new TaskFindTrench());
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		/*return simState.entities.getObjectLocation(a).equals(objective) 
				|| !a.canMoveTo(objective, simState, SimulationConfig.ENV_MODE);*/
		return isFinished;
	}


	@Override
	public void onFinished(Agent a, SimulationState simState) {
		((ObjectiveDrivenAgent)a).addObjective(new ObjectiveSearchEnemies());
	}


	// TASKS

	private class TaskFindTrench extends TaskOneShot {
		private Int2D hive;
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {
			hive = a.getKnowledgeMap().getRandomPositionOfKnowledge(Knowledge.HIVE, simState.random);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			//decide which hive you are defending, if we don't have a hive just die in peace
			if (hive == null) 
				a.die(simState);
			//calculate a point which is as far as possible from the swarm centroid, while
			//still being close to the hive we are defending
			//make sure it is not on a known obstacle as well!!
			else{
				objective = PositioningFunctions.findValidPosition(hive, 5, a.getKnowledgeMap(), simState.random);
				a.computePath(simState, objective);
				obj.addTaskLast(new TaskMove2Trench(objective));
				//obj.addTaskLast(new TaskGetToPosition(objective)); This task doesn't work in this case 
			}
		}
	}

	private class TaskMove2Trench extends TaskGetToPosition {
		public TaskMove2Trench(Int2D destination) {
			super(destination);
		}
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			// if we reached the current position we finish
			isFinished = true;
			/*
			if(simState.entities.getObjectLocation(a).equals(objective) 
					|| !a.canMoveTo(objective, simState, SimulationConfig.ENV_MODE)) 
				isFinished = true;
			else
				obj.addTaskLast(new TaskMove2Trench(objective, 1));*/	
	
		}
	}


}
