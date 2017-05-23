package com.sica.modules.queenBee;


import java.util.PriorityQueue;

import com.sica.behaviour.Objectives.Objective;
import com.sica.entities.EntityPlacer;
import com.sica.entities.agents.DroolsAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class QueenDrools extends DroolsAgent{
	
	private static final long serialVersionUID = 7010748442357851286L;
	public boolean waiting = false;
	
	private int count;
	private int availableFood;
	private Int2D location;
	
	protected PriorityQueue<Objective> objectives;
	
	public QueenDrools() {
		super("ksession-queenDrools", 1, EntityType.QUEEN);
		count = 0;
		this.objectives = new PriorityQueue<Objective>();
	}
	
	@Override
	public void stepBeforeFiringRules(SimulationState arg0) {
		this.addObjectToKnowledgeBase(arg0);
		this.location = arg0.entities.getObjectLocation(this);
		if (arg0.environment.hasTypeAt(location, Knowledge.HIVE))
			this.availableFood = arg0.environment.getMetadataAt(location);
		else
			this.availableFood = 0;
	}
	
	@Override
	public void stepAfterFiringRules(SimulationState arg0) {
		if (!this.objectives.isEmpty()) {
			this.objectives.peek().step(this, arg0);
			
			if (this.objectives.peek().isFinished(this, arg0)) {
				this.objectives.peek().onFinished(this, arg0);
				this.objectives.poll();
			}
		}
	}

	@Override
	public void setupSessionKnowledge() {
		this.addObjectToKnowledgeBase(this);
	}
	
	public void createBee(SimulationState state) {
		float workers = ((float) state.entities.getNumberOf(EntityType.OBJECTIVE_DRIVEN_WORKER));
		float defenders = ((float) state.entities.getNumberOf(EntityType.DEFENDER_BEE));
		float total = workers + defenders;

		if (100.0 * defenders / total > state.config.getPercentageDefender()) {
			EntityPlacer.deployEntity(EntityType.OBJECTIVE_DRIVEN_WORKER, state.entities, state.entities.getObjectLocation(this), state.schedule);
		}
		else {
			EntityPlacer.deployEntity(EntityType.DEFENDER_BEE, state.entities, state.entities.getObjectLocation(this), state.schedule);
		}
	}
	
	public void addObjective (Objective objective) {
		this.objectives.add(objective);
	}
	
	public void increaseCount () {
		if (count <= SimulationConfig.config().getTime2Create()) {
			count++;
		}
	}
	
	public void resetCount() {
		count = 0;
	}
	
	public int getCount() {
		return count;
	}
	
	public int getAvailableFood() {
		return this.availableFood;
	}
	
	public Int2D getLocation() {
		return this.location;
	}

}