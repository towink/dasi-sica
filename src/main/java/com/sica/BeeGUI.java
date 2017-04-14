package com.sica;

import java.awt.Color;

import javax.swing.JFrame;

import com.sica.environment.EnvironmentColorMap;
import com.sica.simulation.SimulationController;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;

public class BeeGUI extends GUIState {

	public Display2D display;
	public JFrame frame;

	FastValueGridPortrayal2D environmentPortrayal = new FastValueGridPortrayal2D("environment");
	SparseGridPortrayal2D entityPortrayal = new SparseGridPortrayal2D();

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
		
		environmentPortrayal.setField(simulation.environment);
		environmentPortrayal.setMap(new EnvironmentColorMap());
		
		entityPortrayal.setField(simulation.entities);

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
		display.attach(environmentPortrayal,"Environment");
		display.attach(entityPortrayal,"Entities");
		
		display.setBackdrop(Color.white);
	}

	public static void main(String[] args) {
		// Create the GUI
		new BeeGUI().createController();
	}

}
