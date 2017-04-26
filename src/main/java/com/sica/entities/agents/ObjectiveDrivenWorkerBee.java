package com.sica.entities.agents;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import com.sica.behaviour.Objective;
import com.sica.behaviour.Task;
import com.sica.behaviour.TaskGetToPosition;
import com.sica.behaviour.TaskOneShot;
import com.sica.entities.Entity;
import com.sica.entities.Entity.EntityType;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import com.util.movement.Direction;
import com.util.movement.MovementFunctions;

import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class ObjectiveDrivenWorkerBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -1667202705195646436L;
	
	// TODO configurable
	private static final int FLOWER_THRESHOLD = 1;
	
	private boolean carriesAliment;

	public ObjectiveDrivenWorkerBee() {													
		super(EntityType.OBJECTIVE_DRIVEN);
		carriesAliment = false;
	}
	
	public void broadcastKnowledge(final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		Bag beeBag = simState.entities.getRadialNeighbors(
				location.getX(),
				location.getY(),
				simState.getConfig().getRadioView(),
				SimulationConfig.ENV_MODE,
				false);
		for (Object a: beeBag) {
			Entity ag = (Entity) a;
			if (ag.getType() != EntityType.OBJECTIVE_DRIVEN || ag.getUAID() == this.getUAID()) {
				continue;
			}
			sendKnowledge((ObjectiveDrivenWorkerBee)a); 
		}
	}
	
	// This is called 'thinks' because it does not necessarily have to be true
	private boolean thinksKnowsFlowers(int min) {
		return getKnowledgeMap().getKnowledgeOf(Knowledge.FLOWER).size() >= min;
	}
	
	
	/* 
	 * COMMON TASKS FOR BOTH OBJECTIVES OF THIS BEE
	 * -------------------------------------------------
	 */
	
	private class TaskBroadcastKnowledge extends TaskOneShot {
		@Override
		public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
			//System.out.println("broadcasting knowledge ...");
			broadcastKnowledge(simState);
		}
	}
	
	/* 
	 * OBJECTIVES OF THIS BEE
	 * -------------------------------------------------
	 */
	
	/**
	 * 
	 * @author Tobias
	 *
	 */
	public class ObjectiveExplore extends Objective {
		
		public ObjectiveExplore() {
			taskQueue.add(new TaskMoveRandomly());
		}
		
		@Override
		public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			return thinksKnowsFlowers(FLOWER_THRESHOLD);
		}
		
		@Override
		public void onFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			objectives.add(new ObjectiveCollect());
		}
		
		/* 
		 * TASKS SPECIFIC TO OBJECTIVE EXPLORE
		 * -------------------------------------------------
		 */
		
		private class TaskObserveFlowersObstacles extends TaskOneShot {
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				observeEnvironment(simState, Knowledge.OBSTACLE);
				observeEnvironment(simState, Knowledge.FLOWER);
			}
			@Override
			public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
				if(a.getKnowledgeMap().pollNewKnowledge()) {
					obj.addTask(new TaskBroadcastKnowledge());
				}
				obj.addTask(new TaskMoveRandomly());
			}
		}
		
		private class TaskMoveRandomly extends TaskOneShot {
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				// randomly pick a direction and move
				Direction randomDir = Direction.values()[simState.random.nextInt(Direction.values().length)];
				move(randomDir, simState, SimulationConfig.ENV_MODE);
			}
			@Override
			public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
				obj.addTask(new TaskObserveFlowersObstacles());
			}
		}

	}

	
	/**
	 * Represents the objective to go to known flower position, take aliment
	 * and bring it to the hive.
	 * Implemented as inner class because:
	 * - conceptually nice because the objective without the agent does not make sense
	 * - objective has full control over agent
	 * @author Tobias
	 *
	 */
	public class ObjectiveCollect extends Objective {
		
		public ObjectiveCollect() {
			super();
			addTask(new TaskDecideWhereToGo());
		}
		
		@Override
		public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			return !thinksKnowsFlowers(FLOWER_THRESHOLD);
		}
		
		@Override
		public void onFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			addObjective(new ObjectiveExplore());
		}
		
		/* 
		 * TASKS SPECIFIC TO OBJECTIVE COLLECT
		 * -------------------------------------------------
		 */
		
		// TODO what is the criterion to decide where to go next?
		private class TaskDecideWhereToGo extends TaskOneShot {
			private Int2D decision;
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				Collection<Int2D> flowerPositions = knowledge.getKnowledgeOf(Knowledge.FLOWER);
				if(flowerPositions.isEmpty()) {
					throw new IllegalStateException("no flower pos known");
				}
				int i = simState.random.nextInt(flowerPositions.size());
				decision = (Int2D) flowerPositions.toArray()[i];
			}
			@Override
			public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
				addTask(new TaskGetToPosition(decision));
				addTask(new TaskGrabAlimentFromCurrentPos());
			}
		}
		
		private class TaskGrabAlimentFromCurrentPos extends TaskOneShot {
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				Int2D pos = simState.entities.getObjectLocation(a);
				int meta = simState.environment.getMetadataAt(pos); 
				// only grab aliment if there really is some!
				if(simState.environment.hasTypeAt(pos, Knowledge.FLOWER) && meta > 0) {
					carriesAliment = true;
					/* TODO decreasing the number of aliment and deleting the flower should probably
					 * be implemented in the environment class ...
					 */
					simState.environment.setMetadataAt(pos, meta - 1);
					if(simState.environment.getMetadataAt(pos) <= 0) {
						simState.environment.set(pos, Knowledge.EMPTY);
					}
				}
			}
			@Override
			public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
				obj.addTask(new TaskObserveEnvironment());
				// only go back to hive if we really grabbed aliment
				if(carriesAliment) {
					obj.addTask(new TaskGetToPosition(new Int2D(50, 50))); // TODO hive position not hard coded
					obj.addTask(new TaskLeaveAlimentInHive());
					obj.addTask(new TaskBroadcastKnowledge());
				}
				obj.addTask(new TaskDecideWhereToGo());
			}
		}
		
		private class TaskLeaveAlimentInHive extends TaskOneShot {
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				if(carriesAliment) {
					simState.aliment++;
				}
			}
		}
		
		private class TaskObserveEnvironment extends TaskOneShot {
			@Override
			public void interactWithOneShot(ObjectiveDrivenAgent a, SimulationState simState) {
				observeEnvironment(simState, Knowledge.OBSTACLE);
				observeEnvironment(simState, Knowledge.FLOWER);
				observeEnvironment(simState, Knowledge.EMPTY);
			}
		}
		
	}
	
}
