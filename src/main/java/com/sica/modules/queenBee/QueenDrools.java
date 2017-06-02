package com.sica.modules.queenBee;


import java.util.PriorityQueue;

import com.sica.behaviour.Objective;
import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.entities.agents.DroolsAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

public class QueenDrools extends DroolsAgent{
	
	private static final long serialVersionUID = 7010748442357851286L;
	
	public boolean waiting = false;
	
	public boolean onObjectiveAvoidEnemies = false;
	
	private int count;
	private int availableFood;
	private Int2D location;
	
	protected PriorityQueue<Objective> objectives;
	
	public QueenDrools() {
		super("ksession-queenDrools", 1, EntityType.QUEEN_BEE);
		count = 0;
		this.objectives = new PriorityQueue<Objective>();
	}
	
	@Override
	public void stepBeforeFiringRules(SimulationState arg0) {
		this.heal(3);//avoid damage from stepping
		
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
		// maybe better as a global variable to drools
		this.addObjectToKnowledgeBase(this);
	}
	
	public void createBee(SimulationState state) {
		float workers = ((float) state.entities.getNumberOf(EntityType.WORKER_BEE));
		float defenders = ((float) state.entities.getNumberOf(EntityType.DEFENDER_BEE));
		float total = workers + defenders;

		if (100.0 * defenders / total > state.config.getPercentageDefender()) {
			EntityPlacer.deployEntity(EntityType.WORKER_BEE, state.entities, state.entities.getObjectLocation(this), state.schedule);
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
	
	public boolean enemiesClose(SimulationState simState) {
		Bag entityBag = getNeighboringEntities(simState);
		// check if there are enemies within range
		for (Object o: entityBag) {
			Entity entity = (Entity) o;
			if (Entity.isEnemy(entity)) {
				return true;
			}
		}
		return false;
	}
	
	

}
