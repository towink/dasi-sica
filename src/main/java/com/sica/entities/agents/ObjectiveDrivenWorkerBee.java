package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.entities.Entity.EntityType;
import com.sica.simulation.SimulationState;
import com.util.knowledge.HashMapKnowledgeMap;
import com.util.knowledge.KnowledgeMapInterface;

import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class ObjectiveDrivenWorkerBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -1667202705195646436L;

	public ObjectiveDrivenWorkerBee() {																																		
		super(EntityType.OBJECTIVE_DRIVEN, new HashMapKnowledgeMap());
	}
	
	
	public void broadcastKnowledge(final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		Bag beeBag = simState.entities.getRadialNeighbors(
				location.getX(),
				location.getY(),
				simState.getConfig().getRadioView(),
				Grid2D.TOROIDAL,
				true);

		for (Object a: beeBag) {
			Entity ag = (Entity) a;
			if (ag.getType() != EntityType.WORKER)
				continue;
			
			sendKnowledge((WorkerBee) a); 
		}
		
		// knowledge.pollNewKnowledge();
	}
	
	@Override
	public void sendKnowledge (Agent receptor) {
		WorkerBee bee = (WorkerBee) receptor;
		bee.receiveKnowledge(knowledge);
	}

	@Override
	public void receiveKnowledge (KnowledgeMapInterface knowledge) {
		this.knowledge.addKnowledge(knowledge);		
	}


}
