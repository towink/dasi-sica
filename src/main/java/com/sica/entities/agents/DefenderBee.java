package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.movement.PositioningFunctions;

import sim.util.Bag;
import sim.util.Int2D;

public class DefenderBee extends Agent {
	private static final long serialVersionUID = 1746904605899616287L;
	

	private enum State {FINDING_TRENCH, MOVING_TO_TRENCH, GUARDING_COLONY, MOVING_TO_ENEMY, ATTACK, REEVALUATE_LIFE, RECEIVED_ALARM}
	private State state;
	private Int2D alarm;


	public DefenderBee() {
		super(EntityType.DEFENDER_BEE);
		this.state = State.FINDING_TRENCH;
	}


	@Override
	//TODO some repeated code and stuff, probably cleaner to extract it to an objective driven bee, 
	//that way some functions can even be shared across agents
	public void doStep(SimulationState simState) {
		switch(this.state) {
		case FINDING_TRENCH: {
			//decide which hive you are defending, if we don't have a hive just die in peace
			Int2D site = this.knowledge.getRandomPositionOfKnowledge(Knowledge.HIVE, simState.random);
			if (site == null) {
				this.state = State.REEVALUATE_LIFE; //I HAVE NO PURPOSE
				break;
			} 
			//calculate a point which is as far as possible from the swarm centroid, while
			//still being close to the hive we are defending
			//make sure it is not on a known obstacle as well!!
			Int2D objective = PositioningFunctions.findValidPosition(
					site, 
					5, //TODO extract to config 
					//swarmCentroid, 
					this.knowledge,
					simState.random);
			
			this.computePath(simState, objective);
			this.state = State.MOVING_TO_TRENCH;
			break;
		}
		case MOVING_TO_TRENCH: {
			//if for some reason we couldn't get ot our objective, find one again
			if (this.actualPath == null) {
				this.state = State.FINDING_TRENCH;
				break;
			}
			if (this.actualPath.isEmpty()) {
				this.state = State.GUARDING_COLONY;
				break;
			}
				
			boolean moved = this.moveTo(this.actualPath.remove(0), simState, SimulationConfig.ENV_MODE);
			if (moved) { //if it moved, continue moving
				this.state = State.MOVING_TO_TRENCH;
			} else { //otherwise recalculate
				this.state = State.FINDING_TRENCH;
			}
			break;
		}
		case GUARDING_COLONY: {
			//just scan for enemies, if none are found, reevaluate your trench
			Int2D location = simState.entities.getObjectLocation(this);
			//TODO maybe the radius, mode, etc are agent independent?
			Bag entityBag = simState.entities.getRadialNeighbors(location.x, location.y, SimulationConfig.config().getRadioView(), SimulationConfig.ENV_MODE, true);
			boolean foundEnemy = false;
			//look through all agents to see if you find an enemy
			for (Object o: entityBag) {
				Entity entity = (Entity) o;
				if (Entity.isEnemy(entity)) {
					this.computePath(simState, simState.entities.getObjectLocation(entity)); 
					this.state = State.MOVING_TO_ENEMY;
					foundEnemy = true;
					break;
				}
			}
			if (!foundEnemy) {
				this.state = State.FINDING_TRENCH;
			}
			break;
		}
		case MOVING_TO_ENEMY: {//CHAAAAARGE
			//if for some reason we couldn't get ot our objective, find one again
			if (this.actualPath == null) {
				this.state = State.GUARDING_COLONY;
				break;
			}
			if (this.actualPath.isEmpty()) {
				this.state = State.ATTACK;
				break;
			}
				
			boolean moved = this.moveTo(this.actualPath.remove(0), simState, SimulationConfig.ENV_MODE);
			if (moved) { //if it moved, continue moving
				this.state = State.MOVING_TO_ENEMY;
			} else { //otherwise recalculate
				this.state = State.GUARDING_COLONY;
			}
			break;
		}
		case ATTACK: {
			//get all agents at this location
			Bag entityBag = simState.entities.getObjectsAtLocationOfObject(this);
			//find enemies and kill them
			boolean killed = false;
			for (Object o: entityBag) {
				Entity entity = (Entity) o;
				if (Entity.isEnemy(entity)) {
					entity.die(simState);
					killed = true;
				}
			}
			//back to work after a fun battle
			//if we always go to FINDING_TRENCH, the bees are less prone
			//to wander away being blood thirsty
			if (!killed) {
				this.state = State.GUARDING_COLONY;
			} else {
				this.state = State.FINDING_TRENCH;
			}
			break;
		}
		case REEVALUATE_LIFE: {
			//we don't have a hive to defend.
			//we have time to think of our children
			//oh wait we are unfertile drones
			//think about our life choices
			//wait we didn't make those either
			//we probably could sting the queen and
			//at least be rememberd as assholes
			//but then the others would kill us
			//maybe reading a book is a better choice
			break;
		}
		case RECEIVED_ALARM: {
			this.computePath(simState, this.alarm);
			this.state = State.MOVING_TO_ENEMY;
			break;
		}
		}
	}
	
	@Override
	public void receiveBitOfKnowledge(Int2D where, Knowledge knowledge) {
		if (knowledge == Knowledge.ENEMY && this.state == State.GUARDING_COLONY) {
			this.alarm = where;
			this.state = State.RECEIVED_ALARM;
		}
	}

}
