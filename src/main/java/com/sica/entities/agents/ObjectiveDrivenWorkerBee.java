package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

/**
 * Represents a worker bee in the simulation.
 * Technically it is an agent which is guided by the two objectives 'explore' and 'collect'.
 * 
 * One of the main parameters of this bee is flowerThreshold. This is the number of flowers it needs to think it knows
 * before switching to the collecting objective.
 * 
 * @author Tobias
 *
 */
public class ObjectiveDrivenWorkerBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -1667202705195646436L;
	
	private boolean carriesAliment;

	public ObjectiveDrivenWorkerBee(Int2D home) {													
		super(EntityType.OBJECTIVE_DRIVEN, home);
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
	public boolean thinksKnowsFlowers(int min) {
		return getKnowledgeMap().getKnowledgeOf(Knowledge.FLOWER).size() >= min;
	}
	
	
	public boolean getCarriesAliment () {
		return this.carriesAliment;
	}
	
	public void setCarriesAliment (boolean carriesAliment) {
		this.carriesAliment = carriesAliment;
	}
	
}
