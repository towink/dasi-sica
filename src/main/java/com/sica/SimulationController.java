package com.sica;

import java.awt.Point;

import com.sica.agents.Agent;
import com.sica.agents.DroolsBee;
import com.sica.agents.WorkerBee;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;

public class SimulationController extends SimState {

	private static final long serialVersionUID = -1449354141004958564L;

	// Grid's size
	public static final int GRID_HEIGHT = 100;
	public static final int GRID_WIDTH = 100;

	// Hive's size
	public static final int HIVE_HEIGHT = 10;
	public static final int HIVE_WIDTH = 10;
	
	//Flowers's size
	public static final int NORMAL_FLOWER_HEIGHT = 5;
	public static final int NORMAL_FLOWER_WIDTH = 5;
	
	// Representation value
	public static final int HIVE = 1;
	public static final int FLOWER = 2;

	// Simulation's variables
	public int numBees = 1000;
	public int numFlowers = 4;
	public float groupingAffinity = 0.95f;


	public IntGrid2D hive = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public IntGrid2D flowers = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public SparseGrid2D bees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
	public SparseGrid2D drlbees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

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
		drlbees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
		
		generateHive();
		generateFlowers();
		generateBees();
	}

	private void generateHive () {
		// TODO: Here, we can implement factory pattern
		Point center = new Point (GRID_WIDTH/2, GRID_HEIGHT/2);
		Point length = new Point (HIVE_WIDTH, HIVE_HEIGHT);
		drawRect (center, length, hive, HIVE);
	}

	private void generateFlowers () {
		// TODO: Here, we can implement factory pattern
		Point length = new Point (NORMAL_FLOWER_WIDTH, NORMAL_FLOWER_HEIGHT);
		drawFillRect(length, length, flowers, FLOWER);
	}

	private void generateBees() {
		// TODO: Here, we can implement factory pattern
		
		Agent agent;
		
		for (int x = 0; x < 100; x++) {
			agent = new DroolsBee();
			drlbees.setObjectLocation(agent, 20,20);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		for(int x = 0; x < getNumBees(); x++)
		{
			agent = new WorkerBee();
			bees.setObjectLocation(agent, GRID_WIDTH/2, GRID_HEIGHT/2);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		
		
	}
	
	private void drawRect (Point center, Point length, IntGrid2D container, int value) {
		for (int i = 0; i <= length.getY()/2; i++) {
			container.field[(int) (center.getX()-length.getX()/2)][(int) (center.getY()+i)] = value;
			container.field[(int) (center.getX()-length.getX()/2)][(int) (center.getY()-i)] = value;
			container.field[(int) (center.getX()+length.getX()/2)][(int) (center.getY()+i)] = value;
			container.field[(int) (center.getX()+length.getX()/2)][(int) (center.getY()-i)] = value;
		}
		for (int i = 0; i < length.getX()/2; i++) {
			container.field[(int) (center.getX()+i)][(int) (center.getY()-length.getY()/2)] = value;
			container.field[(int) (center.getX()-i)][(int) (center.getY()-length.getY()/2)] = value;
			container.field[(int) (center.getX()+i)][(int) (center.getY()+length.getY()/2)] = value;
			container.field[(int) (center.getX()-i)][(int) (center.getY()+length.getY()/2)] = value;
		}
	}
	
	private void drawFillRect (Point center, Point length, IntGrid2D container, int value) {		
		int i = (int) (center.getX()-length.getX()/2);
		int j = (int) (center.getY()-length.getY()/2);
		for (;i < center.getX()+length.getX()/2; i++) {
			for (; j < center.getY()+length.getY()/2; j++) {
				container.field[i][j] = value;
			}
			j = (int) (center.getY()-length.getY()/2);
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
	
	public float getGroupingAffinity() {
		return groupingAffinity;
	}

	public void setGroupingAffinity(float groupingAffinity) {
		this.groupingAffinity = groupingAffinity;
	}

}
