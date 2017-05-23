package com.sica.modules.workerBee;

import com.sica.behaviour.Objectives.Objective;
import com.sica.behaviour.Tasks.TaskObserveEnvironment;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

public class TaskObserveEnvironmentObstacleFlower extends TaskOneShot {
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		//obj.addTaskFirst(new TaskAvoidEnemies());
		obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.FLOWER));
		obj.addTaskFirst(new TaskObserveEnvironment(Knowledge.OBSTACLE));
	}
	
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {}
}