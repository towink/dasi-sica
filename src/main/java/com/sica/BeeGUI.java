package com.sica;

import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;

public class BeeGUI extends GUIState {

	public Display2D display;
	public JFrame frame;

	FastValueGridPortrayal2D hive = new FastValueGridPortrayal2D("Hive");
	FastValueGridPortrayal2D flowers = new FastValueGridPortrayal2D("Flowers");
	SparseGridPortrayal2D obstacles = new SparseGridPortrayal2D();
	SparseGridPortrayal2D bees = new SparseGridPortrayal2D();
	SparseGridPortrayal2D drlBees = new SparseGridPortrayal2D();
	

	public BeeGUI() { 
		super(new SimulationController(System.currentTimeMillis())); 
	}

	public BeeGUI(SimState state) { 
		super(state); 
	}

	/**
	 *  Allow the user to inspect the model
	 */
	public Object getSimulationInspectedObject() { 
		return state; 
	}

	/**
	 * Window takes its name from here
	 * @return window's name
	 */
	public static String getName() { 
		return "Bee agents"; 
	}

	/**
	 * Set up every objects in the interface
	 */
	public void setUp() {
		SimulationController simulation = (SimulationController) state;
		// TODO: set up the objects from simulation
		
		flowers.setField(simulation.flowers);
		flowers.setMap(new sim.util.gui.SimpleColorMap(
				0,
				1,
				new Color(0,0,0,0),
				Color.YELLOW
				));
		obstacles.setField(simulation.obstacles);
		
		hive.setField(simulation.hive);
		hive.setMap(new sim.util.gui.SimpleColorMap(
				0,
				1,
				new Color(0,0,0,0),
				Color.RED
				));
		bees.setField(simulation.bees);
		drlBees.setField(simulation.drlBees);
        
		display.reset();
        display.repaint();
	}

	public void start()
	{
		super.start(); 
		setUp();
	}

	public void load(SimState state)
	{
		super.load(state);
		setUp();
	}

	public void quit()
	{
		super.quit();
		if (frame!=null) {
			frame.dispose();
			frame = null;
		}
		display = null;
	}

	public void init(Controller c)
	{
		super.init(c);

		display = new Display2D(400,400,this);
		frame = display.createFrame();
		c.registerFrame(frame);
		frame.setVisible(true);

		// attach the objects from bottom to top
		display.attach(flowers,"Flowers");
		display.attach(obstacles,"Obstacles");
		display.attach(hive,"Hive");
		display.attach(bees,"Bees");
		display.attach(drlBees, "Drool Bees");

		display.setBackdrop(Color.white);
	}

	public static void main(String[] args) {
		// Create the GUI
		new BeeGUI().createController();
	}

}
