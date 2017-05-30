package com.sica.modules.workerBee;

import com.sica.behaviour.Objective;
import com.sica.behaviour.TaskOneShot;
import com.sica.behaviour.common.TaskBroadcastKnowledgeToSameType;
import com.sica.behaviour.common.TaskGetToPosition;
import com.sica.behaviour.common.TaskMoveRandomly;
import com.sica.behaviour.common.TaskMoveTowardsPosition;
import com.sica.behaviour.common.TaskObserveEnvironment;
import com.sica.behaviour.common.TaskWarnEnemyDetected;
import com.sica.entities.Entity.EntityType;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

public class ObjectiveExplore extends Objective {

	private Int2D objective;
	private boolean warning = false;
	
	public ObjectiveExplore(Int2D where) {
		this.addTaskLast(new TaskMoveThenObserve(where));
		this.objective = where;
	}
	
	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		return bee.thinksKnowsFlowers(SimulationConfig.config().getFlowerThresholdWorker()) 
				|| !a.canMoveTo(objective, simState, SimulationConfig.ENV_MODE) 
				|| simState.entities.getObjectLocation(a).equals(objective) && !warning;
	}
	
	@Override
	public void onFinished(Agent a, SimulationState simState) {
		// go collecting when we are done exploring!
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) a;
		bee.addObjective(new ObjectiveCollect());
	}
	
	// --- TASKS SPECIFIC TO OBJECTIVE EXPLORE ---

	private class TaskObserveFlowersObstacles extends TaskOneShot {
		@Override
		public void interactWithOneShot(Agent a, SimulationState simState) {}
		
		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.FLOWER));
			obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.OBSTACLE));
			/*if(a.getKnowledgeMap().pollNewKnowledge()) {
				obj.addTaskLast(new TaskBroadcastKnowledgeToSameType());
			}*/
			addTaskLast(new TaskMoveThenObserve(objective));
		}
	}
	
	private class TaskMoveThenObserve extends TaskMoveTowardsPosition {

		/*public TaskMoveThenObserve(Int2D n) {
			super(SimulationConfig.config().getWorkerMovesBeforeUpdating());
		}*/
		
		//make this class inherit from TaskMoveTowardsPosition and uncomment this for quicker detecting food sources
		public TaskMoveThenObserve(Int2D destination) {
			super(destination, SimulationConfig.config().getWorkerMovesBeforeUpdating());
		}

		@Override
		public void endTask(Agent a, Objective obj, SimulationState simState) {
			Int2D pos = simState.entities.getObjectLocation(a);
			Bag enemyBag = simState.entities.getRadialEntities(simState, pos, EntityType.ENEMY);
			if (enemyBag.isEmpty()) {//if there are no enemies, keep goin'
				warning = false;
				addTaskLast(new TaskObserveFlowersObstacles());
			} else {	//go back home if dangers are nearby and repeat
				//System.out.println("Enemy detected at: " + pos + obj.getTaskListLength());
				warning = true;
				addTaskLast(new TaskGetToPosition(a.getHome()));
				addTaskLast(new TaskWarnEnemyDetected(pos));
				addTaskLast(new TaskMoveThenObserve(objective));
			}
		}
	}

}
