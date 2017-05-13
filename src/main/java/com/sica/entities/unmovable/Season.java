package com.sica.entities.unmovable;

import org.apache.poi.poifs.crypt.EncryptionMode;

import com.sica.entities.Entity;
import com.sica.entities.agents.Agent;
import com.sica.entities.agents.DefenderBee;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.environment.EnvironmentModeller;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;

import sim.util.Bag;

public class Season extends Entity {

	private static final long serialVersionUID = 4907817009023550510L;
	
	public static enum SeasonTypes {
		WINTER, AUTUMN, SPRING, SUMMER;
		
		public static SeasonTypes nextSeason (SeasonTypes actual) {
			switch (actual) {
			case WINTER: return SPRING;
			case AUTUMN: return WINTER;
			case SPRING: return SUMMER;
			case SUMMER: return AUTUMN;
			default: throw new IllegalStateException ("Do not exist the SeasonTypes: " + actual);
			}
		}
	}
	
	public SeasonTypes season;
	public int count;

	public Season (SeasonTypes season) {
		super (EntityType.SEASON);
		this.count = 0;
		this.season = season;
	}
	
	@Override
	public void doStep(SimulationState simState) {
		this.count++;
		if (this.count >= simState.config.getTime4Season()) {
			changeSeason(simState);
		}
	}
	
	private void changeSeason (SimulationState simState) {
		this.count = 0;
		this.season = SeasonTypes.nextSeason(this.season);
		simState.environment.removeAll(Knowledge.FLOWER);
		
		Bag agents = simState.getEntities().getAllObjects();
		for (Object o : agents) {
			Entity entity = (Entity) o;
			if (isBee(entity.getType())) {
				Agent agent = (Agent) entity;
				if (agent.dead()) {
					agent.die(simState);
				}
			}
		}
		
		EnvironmentModeller.randomlyGenerateFlowers(simState.environment, 
													getNumFlowers(simState), 
													simState.config.getMinAlimentFlower(), 
													simState.config.getMaxAlimentFlower(), 
													simState.random);
		
	}
	
	private boolean isBee (EntityType type) {
		return type == EntityType.DEFENDER_BEE ||
				type == EntityType.OBJECTIVE_DRIVEN_WORKER;
	}
	
	private boolean isBee2 (Class c) {
		return c == ObjectiveDrivenWorkerBee.class ||
				c == DefenderBee.class;
	}
	
	/**
	 * Return number of flowers in the actual season
	 * @param simState
	 * @return
	 */
	public int getNumFlowers (SimulationState simState) {
		int value = 0;
		switch (this.season) {
		case WINTER:
			value = 0;
			break;
		case AUTUMN:
			value = simState.config.getNumFlowers() / 2;
			break;
		case SPRING:
			value = simState.config.getNumFlowers();
			break;
		case SUMMER:
			value = simState.config.getNumFlowers() / 2;
			break;
		}
		
		return value;
	}

}
