package com.sica;

import java.awt.Point;
import java.util.Random;

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
	public static final int OBSTACLE = 3;

	// Simulation's variables
	public int numBees = 100;
	public int numFlowers = 1;
	public float groupingAffinity = 0.95f;
	public int radioView = 5;
	public int percentageObstacle = 10;


	public IntGrid2D hive = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public IntGrid2D flowers = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
	public SparseGrid2D obstacles = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
	public SparseGrid2D bees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
	public SparseGrid2D drlBees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);

	public SimulationController(long seed)
	{ 
		super(seed);
	}

	public void start()
	{
		super.start();

		hive = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
		flowers = new IntGrid2D(GRID_WIDTH, GRID_HEIGHT,0);
		obstacles = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
		bees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
		drlBees = new SparseGrid2D(GRID_WIDTH, GRID_HEIGHT);
		
		generateHive();
		generateFlowers();
		generateRandomObstacles(percentageObstacle);
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

	private void generateRandomObstacles (int percentage) {
		int numObstacles = (percentage * GRID_WIDTH * GRID_HEIGHT) / 100;
		int limit = (GRID_WIDTH * GRID_HEIGHT) - (HIVE_WIDTH * HIVE_HEIGHT) - (numFlowers * NORMAL_FLOWER_WIDTH * NORMAL_FLOWER_HEIGHT);
		if (numObstacles > limit) {
			numObstacles = limit - 1;
		}
		Point pos = new Point();
		Random rnd = new Random();
		for (int i = 0; i < numObstacles; i++) {
			pos.x = rnd.nextInt(GRID_WIDTH);
			pos.y = rnd.nextInt(GRID_HEIGHT);
			while (!emptyPos(pos)) {
				pos.x = rnd.nextInt(GRID_WIDTH);
				pos.y = rnd.nextInt(GRID_HEIGHT);
			}
			//obstacles.field[pos.x][pos.y] = OBSTACLE;
			obstacles.setObjectLocation(new Object(), pos.x, pos.y);
		}
	}
	
	private boolean emptyPos (Point pos) {
		return checkHive (pos) && checkFlowers(pos);
	}
	
	private boolean checkHive (Point pos) {
		Point center = new Point (GRID_WIDTH/2, GRID_HEIGHT/2);
		int minX = (int) (center.getX() - HIVE_WIDTH/2);
		int minY = (int) (center.getY() - HIVE_HEIGHT/2);
		int maxX = (int) (center.getX() + HIVE_WIDTH/2);
		int maxY = (int) (center.getY() + HIVE_HEIGHT/2);
	
		if (pos.x > maxX || pos.x < minX) {
			return true;
		}
		
		if (pos.y > maxY || pos.y < minY) {
			return true;
		}
		
		return false;
	}
	
	private boolean checkFlowers (Point pos) {	
		return flowers.field[pos.x][pos.y] == 0 ? true : false;
	}
	
	private void generateBees() {
		// TODO: Here, we can implement factory pattern
		
		Agent agent;
		
		for (int x = 0; x < 100; x++) {
			agent = new DroolsBee();
			drlBees.setObjectLocation(agent, 20,20);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		/*
		 * Test A*
		 * First: Create a map with a lot of obstacles
		 * Second: Put a random objetive where agents want to go
		 * Third: Every agent calculate A*
		 * 
		 * In addition, we check how long it takes to run
		 */
		
		/*Map map = new Map(GRID_WIDTH, GRID_HEIGHT);
		for (int i = 0; i < GRID_HEIGHT - 1; i++) {
			map.modifyMap(20, i, Type.OBSTACLE);
		}
		
		for (int i = 21; i < GRID_WIDTH/2+30; i++) {
			map.modifyMap(i, GRID_HEIGHT/2 + 20, Type.OBSTACLE);
		}*/
		
		long start = System.currentTimeMillis();
		
		for(int x = 0; x < getNumBees(); x++)
		{
			agent = new WorkerBee();
			
			/*// Testing A* {
			agent.setObjective(5, 5);
			Point aux = new Point (GRID_WIDTH/2, GRID_HEIGHT/2);
			
			agent.calculatePath(aux);
			// } testing A**/
			
			bees.setObjectLocation(agent, GRID_WIDTH/2, GRID_HEIGHT/2);
			schedule.scheduleRepeating(Schedule.EPOCH, 0, agent, 1);
		}
		
		System.out.println("Time of calculate " + getNumBees() + " A*: " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
		
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

	public int getRadioView() {
		return radioView;
	}

	public void setRadioView(int radioView) {
		if (radioView > 0) {
			this.radioView = radioView;
		}
	}

	public int getPercentageObstacle() {
		return percentageObstacle;
	}

	public void setPercentageObstacle(int percentageObstacle) {
		if (percentageObstacle > 0) {
			this.percentageObstacle = percentageObstacle;
		}
		if (percentageObstacle > 100) {
			this.percentageObstacle = 100;
		}
	}

}
