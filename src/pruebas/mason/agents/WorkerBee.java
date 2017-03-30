package pruebas.mason.agents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import pruebas.mason.SimulationController;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.util.Int2D;
import util.TwoDVector;

public class WorkerBee extends Agent{

	private static final long serialVersionUID = -1449354141004958564L;

	public WorkerBee() {
		
	}
	
	public void step( final SimState state ) {
		// TODO: do something
		move (state);
	}
	
	private void move (final SimState state) {
		final SimulationController simulation = (SimulationController) state;
		Int2D location = simulation.bees.getObjectLocation(this);
		int max = 2;
		int min = -2;
		int x = ((int)(Math.random()*(max - min) + min)) + location.getX();
		int y = ((int)(Math.random()*(max - min) + min)) + location.getY();
		
		if (x > simulation.GRID_WIDTH - 1) {
			x = simulation.GRID_WIDTH - 1;
		}
		else if (x < 0) {
			x = 0;
		}
		
		if (y > simulation.GRID_HEIGHT - 1) {
			y = simulation.GRID_HEIGHT - 1;
		}
		else if (y < 0) {
			y = 0;
		}
		
		if (simulation.flowers.field[x][y] == SimulationController.FLOWER) {
			simulation.flowers.field[x][y] = 0;
		}
		
		simulation.bees.setObjectLocation(this, new Int2D(x, y));
	}

	public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		graphics.setColor( Color.BLACK );
		
		// this code was stolen from OvalPortrayal2D
		int x = (int)(info.draw.x - info.draw.width / 2.0);
		int y = (int)(info.draw.y - info.draw.height / 2.0);
		int width = (int)(info.draw.width);
		int height = (int)(info.draw.height);
		graphics.fillOval(x,y,width, height);

	}
}
