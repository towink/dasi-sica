package com.sica.agents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import com.sica.SimulationController;
import com.util.searching.Map;

import sim.engine.SimState;
import sim.field.grid.Grid2D;
import sim.portrayal.DrawInfo2D;
import sim.util.Bag;
import sim.util.Int2D;

public class WorkerBee extends Agent{

	private static final long serialVersionUID = -1449354141004958564L;

	public WorkerBee() {
		super();
	}
	
	public WorkerBee(Map map) {
		super(map);
	}
	
	public void step( final SimState state ) {
		// TODO: do something
		super.step(state);
		
		if (actualPath != null) {
			final SimulationController simulation = (SimulationController) state;
			Point aux = actualPath.get(0);
			actualPath.remove(0);
			simulation.bees.setObjectLocation(this, new Int2D(aux.x, aux.y));
			if (actualPath.size() <= 0) {
				actualPath = null;
			}
		}
		
		else {
			if (state.schedule.getSteps() > 2500 && state.random.nextFloat() > ((SimulationController)state).groupingAffinity)
				group(state);
			else
				move (state);
		}
		
	}
	
	private int sign(float number) {
		return number < 0 ? -1 : number > 0 ? 1 : 0;
	}
	
	
	private void group (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.bees.getObjectLocation(this);
		Bag beeBag = simulation.bees.getRadialNeighbors(location.getX(), location.getY(), 5, Grid2D.TOROIDAL, true);
		
		float meanx = 0, meany = 0;
		int cnt = 0;
		for (Object a: beeBag) {
			Int2D loc = simulation.bees.getObjectLocation(a);
			meanx += loc.getX();
			meany += loc.getY();
			cnt ++;
		}
		meanx /= (float) cnt;
		meany /= (float) cnt;
		
		float diffx = meanx - (float) location.getX();
		float diffy = meany - (float) location.getY();
		
		simulation.bees.setObjectLocation(this, new Int2D(
				location.getX() + sign(diffx), 
				location.getY() + sign(diffy)));
	}
	
	private void move (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.bees.getObjectLocation(this);
		int max = 2;
		int min = -2;
		
		int x = ((int)(Math.random()*(max - min) + min)) + location.getX();
		int y = ((int)(Math.random()*(max - min) + min)) + location.getY();
			
		x = Math.floorMod(x, SimulationController.GRID_WIDTH); //== 1;
		y = Math.floorMod(y, SimulationController.GRID_HEIGHT);
		
		if (simulation.flowers.field[x][y] == SimulationController.FLOWER) {
			simulation.flowers.field[x][y] = 0;
			setObjective(getHome());
			calculatePath(new Point(location.getX(), location.getY()));
		}
		
		simulation.bees.setObjectLocation(this, new Int2D(x, y));
	}

	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		graphics.setColor( Color.BLUE );
		
		// this code was stolen from OvalPortrayal2D
		int x = (int)(info.draw.x - info.draw.width / 2.0);
		int y = (int)(info.draw.y - info.draw.height / 2.0);
		int width = (int)(info.draw.width);
		int height = (int)(info.draw.height);
		graphics.fillOval(x,y,width, height);

	}
}
