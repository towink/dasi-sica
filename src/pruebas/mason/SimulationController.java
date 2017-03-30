package pruebas.mason;

import sim.engine.SimState;

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


	public SimulationController(long seed)
	{ 
		super(seed);
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
