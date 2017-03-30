package pruebas.mason;

import java.util.ArrayList;

import pruebas.mason.agents.*;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;

public class SimulationController extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;

	// Grid's size
	public static final int GRID_HEIGHT = 100;
	public static final int GRID_WIDTH = 100;

	// Hive's size
	public static final int HIVE_HEIGHT = 10;
	public static final int HOME_WIDTH = 10;

	// Representation value
	public static final int HIVE = 1;
	public static final int FLOWER = 2;

	// Simulation's variables
	public int numBees = 1000;
	public int numFlowers = 4;

	public IntGrid2D hive = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public IntGrid2D flowers = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public SparseGrid2D bees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

	public SimulationController(long seed)
	{ 
		super(seed);
	}

	public void start()
	{
		super.start();

		hive = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
		flowers = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
		bees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
		
		GenerateHive();
		GenerateFlowers();
		GenerateBees();
	}

	private void GenerateHive () {
		// TODO: Here, we can implement factory pattern

	}

	private void GenerateFlowers () {
		// TODO: Here, we can implement factory pattern

	}

	private void GenerateBees() {
		// TODO: Here, we can implement factory pattern
		for(int x = 0; x < getNumBees(); x++)
		{
			Agent agent = new WorkerBee();
			bees.setObjectLocation(agent, GRID_WIDTH/2, GRID_HEIGHT/2);
			schedule.scheduleRepeating(Schedule.EPOCH + x, 0, agent, 1);
		}
	}

	// Getters and Setters
	public int getNumBees() {
		return numBees;
	}

	public void setNumBees(int numBees) {
		if (numBees > 0) {
			this.numBees = numBees;
		}	
	}

	public int getNumFlowers() {
		return numFlowers;
	}

	public void setNumFlowers(int numFlowers) {
		if (numFlowers > 0) {
			this.numFlowers = numFlowers;
		}
	}

}
