package com.sica.entities.agents;

import java.awt.Point;

import com.sica.entities.Entity;
import com.sica.environment.EnvironmentTypes;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.searching.Map;

import sim.engine.SimState;
import sim.field.grid.Grid2D;
import sim.util.Bag;
import sim.util.Int2D;

public class WorkerBee extends Agent {

	private static final long serialVersionUID = -1449354141004958564L;

	public WorkerBee() {
		super(EntityType.WORKER);
	}
	
	public WorkerBee(Map map) {
		super(EntityType.WORKER, map);
	}
	
	@Override
	public void doStep( final SimulationState simState ) {
		// TODO: do something
		super.doStep(simState);
		
		if (actualPath != null) {
			Point aux = actualPath.remove(0);
			simState.entities.setObjectLocation(this, new Int2D(aux.x, aux.y));
			if (actualPath.size() <= 0) {
				actualPath = null;
			}
		}
		
		else {
			if (simState.schedule.getSteps() > 2500 && simState.random.nextFloat() > simState.getConfig().getGroupingAffinity())
				group(simState);
			else
				move (simState);
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
		
		if (simulation.environment.hasTypeAt(x,  y, EnvironmentTypes.FLOWER)) {
			simulation.environment.set(x, y, EnvironmentTypes.EMPTY);
			setObjective(getHome());
			calculatePath(new Point(location.getX(), location.getY()));
		}

		
		simulation.entities.setObjectLocation(this, new Int2D(x, y));
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
