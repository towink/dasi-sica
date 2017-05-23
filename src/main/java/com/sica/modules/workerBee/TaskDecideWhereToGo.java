package com.sica.modules.workerBee;

import com.sica.behaviour.Objectives.Objective;
import com.sica.behaviour.Tasks.TaskGoToRandomPlaceWithKnowledge;
import com.sica.behaviour.Tasks.TaskOneShot;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

// TODO what is the criterion to decide where to go next? at the moment random ...
public class TaskDecideWhereToGo extends TaskOneShot {
	@Override
	public void interactWithOneShot(Agent a, SimulationState simState) {	}
	@Override
	public void endTask(Agent a, Objective obj, SimulationState simState) {
		obj.addTaskLast(new TaskGoToRandomPlaceWithKnowledge(Knowledge.FLOWER, true));
		obj.addTaskLast(new TaskGrabAlimentFromCurrentPos());
	}
}