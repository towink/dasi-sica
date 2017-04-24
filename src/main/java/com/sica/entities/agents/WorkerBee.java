package com.sica.entities.agents;

import com.sica.entities.Entity;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
import com.util.knowledge.KnowledgeMapInterface;
import sim.engine.SimState;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class WorkerBee extends Agent {

	private static final long serialVersionUID = -1449354141004958564L;

	public static enum State {
		UPDATING, 		// State where bees is sharing their data
		EXPLORING,		// State where bees is exploring the map searching flowers
		MOVING			// State where bees is moving to some place (using A*)
	}

	public State actualState;

	public WorkerBee() {
		super(EntityType.WORKER);
		actualState = State.EXPLORING;
	}

	@Override
	public void doStep( final SimulationState simState ) {
		// TODO: do something
		super.doStep(simState);

		switch (actualState) {
		case UPDATING:
			sendMessages(simState);
			
			if (getObjective() == null) {
				actualState = State.EXPLORING;
			}
			else {
				if (actualPath == null || knowledge.pollNewKnowledge()) {
					Int2D location = simState.entities.getObjectLocation(this);
					calculatePath (new Int2D(location.x, location.y));
				}
				actualState = State.MOVING;
			}
			
			break;
		case EXPLORING:
			if (simState.schedule.getSteps() > 0 && simState.random.nextFloat() > simState.getConfig().getGroupingAffinity())
				group(simState);
			else
				move (simState);
			break;

		case MOVING:
			if (actualPath != null) {
				Int2D aux = actualPath.remove(0);
				simState.entities.setObjectLocation(this, new Int2D(aux.x, aux.y));
				if (actualPath.size() <= 0) {
					actualPath = null;
					if (getObjective().equals(getHome())) {
						actualState = State.UPDATING;
						// TODO: How do you decide when you have to go to a flower?
						setObjective(null);
					}
					else {
						// TODO: What happens if you come to a flower?
					}
				}
			}
			break;
		}	
	}

	private int sign(float number) {
		return number < 0 ? -1 : number > 0 ? 1 : 0;
	}


	private void group (final SimState state) {
		final SimulationState simulation = (SimulationState) state;
		Int2D location = simulation.entities.getObjectLocation(this);
		Bag beeBag = simulation.entities.getRadialNeighbors(location.getX(), location.getY(), 5, Grid2D.TOROIDAL, true);

		float meanx = 0, meany = 0;
		int cnt = 0;
		for (Object a: beeBag) {
			Entity ag = (Entity) a;
			if (ag.getType() != EntityType.WORKER)
				continue;

			Int2D loc = simulation.entities.getObjectLocation(a);
			meanx += loc.getX();
			meany += loc.getY();
			cnt ++;
		}
		meanx /= (float) cnt;
		meany /= (float) cnt;

		float diffx = meanx - (float) location.getX();
		float diffy = meany - (float) location.getY();

		simulation.entities.setObjectLocation(this, new Int2D(
				location.getX() + sign(diffx), 
				location.getY() + sign(diffy)));
	}

	private void move (final SimState state) {
		final SimulationState simulation = (SimulationState) state;
		Int2D location = simulation.entities.getObjectLocation(this);
		int max = 2;
		int min = -2;

		int x = ((int)(Math.random()*(max - min) + min)) + location.getX();
		int y = ((int)(Math.random()*(max - min) + min)) + location.getY();

		x = Math.floorMod(x, SimulationConfig.GRID_WIDTH); //== 1;
		y = Math.floorMod(y, SimulationConfig.GRID_HEIGHT);

		if (simulation.environment.hasTypeAt(x,  y, Knowledge.FLOWER)) {
			simulation.environment.set(x, y, Knowledge.EMPTY);
			setObjective(getHome());
			calculatePath(new Int2D(location.getX(), location.getY()));
			actualState = State.MOVING;
		}

		if (!simulation.environment.hasTypeAt(x, y, Knowledge.OBSTACLE))
				simulation.entities.setObjectLocation(this, new Int2D(x, y));
	}

	private void sendMessages(final SimulationState simState) {
		Int2D location = simState.entities.getObjectLocation(this);
		Bag beeBag = simState.entities.getRadialNeighbors(location.getX(), location.getY(), simState.getConfig().getRadioView(), Grid2D.TOROIDAL, true);

		for (Object a: beeBag) {
			Entity ag = (Entity) a;
			if (ag.getType() != EntityType.WORKER)
				continue;
			
			sendKnowledge((WorkerBee) a); 
		}
		
		knowledge.pollNewKnowledge();
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
	
	/*public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		graphics.setColor( Color.BLUE );

		// this code was stolen from OvalPortrayal2D
		int x = (int)(info.draw.x - info.draw.width / 2.0);
		int y = (int)(info.draw.y - info.draw.height / 2.0);
		int width = (int)(info.draw.width);
		int height = (int)(info.draw.height);
		graphics.fillOval(x,y,width, height);

	}*/

}
