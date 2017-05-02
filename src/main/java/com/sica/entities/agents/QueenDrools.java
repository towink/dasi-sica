package com.sica.entities.agents;


import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class QueenDrools extends DroolsAgent{
	
	private static final long serialVersionUID = 7010748442357851286L;
	
	private int count;
	private int availableFood;
	private Int2D location;
	
	public QueenDrools() {
		super("ksession-queenDrools", 1);
		count = 0;
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
	}

	@Override
	public void setupSessionKnowledge() {
		this.addObjectToKnowledgeBase(this);
	}
	
	public void createBee(SimulationState state) {
		// TODO: Change to create workers and defenders
		EntityPlacer.generateWorkersAfter(state.entities, state.schedule, 1, state.entities.getObjectLocation(this));
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
