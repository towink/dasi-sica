package com.sica.behaviour;

import java.util.List;
import java.util.Random;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.movement.Direction;

import sim.util.Int2D;

/**
 * Represents the exploring activity of a worker bee.
 * At the moment just randomly picks points in the world and tries to
 * get to them, never finishes.
 * @author Tobias
 *
 */
public class ObjectiveExplore extends Objective {
	
	private Int2D randomPoint() {
		Random r = new Random();
		int x = r.nextInt(SimulationConfig.GRID_WIDTH - 1);
		int y = r.nextInt(SimulationConfig.GRID_HEIGHT - 1);
		Int2D res = new Int2D(x, y);
		return res;
	}

	
	public ObjectiveExplore() {
		
		// for testing, randomly choose a location where we want to get to initially	
		// enqueue the new corresponding task
		taskQueue.add(new TaskMoveRandomly());
	}
	
	@Override
	public void taskFinishedCallback(Task t) {
		taskQueue.add(new TaskMoveRandomly());
		
	}

	@Override
	public int getPriority() {
		return 0;
	}
	
	// TASKS IN OBJECTIVE EXPLORE //
	
	private class TaskBroadcastKnowledge implements Task {
		
		// flag to indicate whether the only action in this task (name ly to send
		// the broadcast) has been executed
		private boolean finished = false;

		@Override
		public void interactWith(ObjectiveDrivenAgent a, SimulationState simState) {
			System.out.println("broadcasting knowledge ...");
			((ObjectiveDrivenWorkerBee)a).broadcastKnowledge(simState);
			finished = true;
		}

		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
			obj.addTask(new TaskMoveRandomly());
		}

		@Override
		public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			return finished;
		}
	}
	
	private class TaskObserveEnvironment implements Task {
		// flag to indicate whether the only action in this task (namely to observe
		// the environment) has been executed
		private boolean finished = false;

		@Override
		public void interactWith(ObjectiveDrivenAgent a, SimulationState simState) {
			System.out.println("observing environment ...");
			a.observeEnvironment(simState, Knowledge.OBSTACLE);
			a.observeEnvironment(simState, Knowledge.FLOWER);
			finished = true;
		}

		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
			if(a.getKnowledgeMap().pollNewKnowledge()) {
				obj.addTask(new TaskBroadcastKnowledge());
			}
			else {
				obj.addTask(new TaskMoveRandomly());
			}
		}

		@Override
		public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			return finished;
		}
	}
	
	private class TaskMoveRandomly implements Task {
		// flag to indicate whether the only action in this task (namely to do
		// a single random movement) has been executed
		private boolean finished = false;

		@Override
		public void interactWith(ObjectiveDrivenAgent a, SimulationState simState) {
			System.out.println("moving randomly ...");
			// randomly pick a direction
			Direction randomDir =
					Direction.values()[simState.random.nextInt(Direction.values().length)];
			// move in this direction!
			Int2D newPos = randomDir.getMovementOf(
					simState.entities.getObjectLocation(a),
					SimulationConfig.ENV_MODE,
					SimulationConfig.GRID_WIDTH,
					SimulationConfig.GRID_HEIGHT);
			simState.entities.setObjectLocation(a, newPos);
			// indicate that this task is finished
			finished = true;
		}

		@Override
		public void endTask(ObjectiveDrivenAgent a, Objective obj, SimulationState simState) {
			obj.addTask(new TaskObserveEnvironment());
		}

		@Override
		public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
			return finished;
		}
	}
	
	

}
