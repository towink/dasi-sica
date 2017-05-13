package com.sica.entities.agents;

import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

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
	
	private static int uaidGenerator = 0;
	private static int uaidDead = 0;
	
	private boolean carriesAliment;
	private int uaid;
	
	{
		this.uaid = uaidGenerator;
		ObjectiveDrivenWorkerBee.uaidGenerator++;
	}
	
	public ObjectiveDrivenWorkerBee() {													
		super(EntityType.OBJECTIVE_DRIVEN_WORKER);
		carriesAliment = false;
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
	
	public int getUAIDWorker () {
		return this.uaid;
	}
	
	public static int count () {
		return ObjectiveDrivenWorkerBee.uaidGenerator - ObjectiveDrivenWorkerBee.uaidDead;
	}
	
	public static int deathCount() {
		return ObjectiveDrivenWorkerBee.uaidDead;
	}
	
	@Override
	public void die (SimulationState simState) {
		ObjectiveDrivenWorkerBee.uaidDead += 1;
		remove(simState);
	}
}
