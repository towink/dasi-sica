package com.sica.entities.agents;


import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class QueenDrools extends DroolsAgent{
	
	private static final long serialVersionUID = 7010748442357851286L;
	
	private int count;
	
	public QueenDrools (Int2D home) {
		super("ksession-queenDrools", 1, home);
		count = 0;
	}
	
	@Override
	public void stepBeforeFiringRules(SimulationState arg0) {
		this.addObjectToKnowledgeBase(arg0);
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
		EntityPlacer.generateWorkersAfter(state.entities, state.schedule, 1, getHome());
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

}
