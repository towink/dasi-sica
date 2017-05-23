package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.data.IterableSet;
import com.util.knowledge.Knowledge;

import sim.util.Bag;
import sim.util.Int2D;

public class SimpleEnemy extends Agent {

	private static final long serialVersionUID = -8361812131256894788L;

	private enum State {DECIDING, START_MOVING, MOVING_DELAY, MOVING, APPROACHING, ATTACKING, SLEEPING}
	private State state;
	private int delay;

	public SimpleEnemy() {
		super(EntityType.SIMPLE_ENEMY);
		this.state = State.DECIDING;
	}

	@Override
	public void agentDoStep(SimulationState simState) {
		switch (this.state) {
		case DECIDING:
			Int2D objective = new Int2D(
					simState.random.nextInt(SimulationConfig.GRID_WIDTH), 
					simState.random.nextInt(SimulationConfig.GRID_HEIGHT));
			this.computePath(simState, objective);
			this.state = State.START_MOVING;
			break;
		case START_MOVING:
			this.state = State.MOVING_DELAY;
			this.delay = 5;
			break;
		case MOVING_DELAY:
			if (this.delay <= 0) {
				this.state = State.MOVING;
			} else {
				this.delay--;
			}
			break;
		case MOVING:
			//first check for bees and kill them if we can (we also die)
			Bag bees = simState.entities.getObjectsAtLocation(simState.entities.getObjectLocation(this));
			for (Object o: bees) {
				Entity bee = (Entity) o;
				if (Entity.isBee(bee)) {
					bee.die(simState);
					this.die(simState);
					return;
				}
			}
			//if we are on a free cell go on with our lives
			if (this.actualPath != null && !this.actualPath.isEmpty()) {
				if (!this.moveTo(this.actualPath.remove(0), simState, SimulationConfig.ENV_MODE)) {
					//cannot move, knowledge is automatically updated, so go to decide again
					this.state = State.DECIDING;
				} else {
					this.state = State.START_MOVING; //this is just to delay them a bit so our bees have time to defend themselves
				}
			} else {
				//we reached our destination.
				//if we are in a hive, go to atacking
				if (simState.environment.hasTypeAt(simState.entities.getObjectLocation(this), Knowledge.HIVE)) {
					this.state = State.ATTACKING;
				} else {
					this.observeEnvironment(simState, Knowledge.HIVE);
					if (this.knowledge.getKnowledgeOf(Knowledge.HIVE).isEmpty())
						this.state = State.DECIDING;
					else
						this.state = State.APPROACHING;
				}
			}
			break;
		case APPROACHING:
			IterableSet<Int2D> hives = this.knowledge.getKnowledgeOf(Knowledge.HIVE);
			//go to the first known hive
			this.computePath(simState, hives.iterator().next());
			this.state = State.START_MOVING;
			break;
		case ATTACKING:
			//break a hive block
			System.out.println("Attacked at " + simState.entities.getObjectLocation(this));
			simState.environment.set(simState.entities.getObjectLocation(this), Knowledge.EMPTY);
			this.state = State.SLEEPING;
			break;
		case SLEEPING: //do nothing, we already f'd something up
			break;
		}
	}
	
	@Override
	public void receiveKnowledgeFrom(Agent sender) {
		//these guys are dumb and do not learn from others
	}

}
