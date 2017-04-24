package com.sica;

import java.awt.Color;
import java.util.Locale;

import javax.swing.JFrame;

import com.sica.entities.agents.DroolsBee;
import com.sica.entities.agents.ObjectiveDrivenWorkerBee;
import com.sica.entities.agents.WorkerBee;
import com.sica.environment.EnvironmentColorMap;
import com.sica.simulation.SimulationState;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;

public class BeeGUI extends GUIState {

	public Display2D display;
	public JFrame frame;

	FastValueGridPortrayal2D environmentPortrayal = new FastValueGridPortrayal2D("environment");
	SparseGridPortrayal2D entityPortrayal = new SparseGridPortrayal2D();

	public BeeGUI() { 
		super(new SimulationState(System.currentTimeMillis())); 
	}

	public BeeGUI(SimState state) { 
		super(state); 
	}
	
	/**
	 *  Allows the user to inspect the model.
	 *  The properties if the return object will be visible/manipulable
	 *  in the 'Model' tab of the console window.
	 */
	public Object getSimulationInspectedObject() { 
		return ((SimulationState)state).getConfig();
	}

	/**
	 * Window takes its name from here
	 * @return window's name
	 */
	public static String getName() { 
		return "Bee agents"; 
	}

	/**
	 * Set up the portrayals for all static and dynamic objects in the simulation
	 */
	public void setUpPortrayals() {
		SimulationState simulation = (SimulationState) state;

		//set up the portrayal for the static objects
		environmentPortrayal.setField(simulation.environment);
		environmentPortrayal.setMap(new EnvironmentColorMap());
		//set up the portrayals for the agents
		entityPortrayal.setField(simulation.entities);
		entityPortrayal.setPortrayalForClass(WorkerBee.class, new OvalPortrayal2D(Color.BLUE, true));
		entityPortrayal.setPortrayalForClass(DroolsBee.class, new OvalPortrayal2D(Color.GREEN, true));
		entityPortrayal.setPortrayalForClass(ObjectiveDrivenWorkerBee.class, new OvalPortrayal2D(Color.MAGENTA, true));
		entityPortrayal.setPortrayalForRemainder(new OvalPortrayal2D(Color.ORANGE, true));
		
		display.reset();
        display.repaint();
	}

	// Is called when the play button is pressed, just before the SimState's start method is called
	@Override
	public void start()
	{
		super.start(); 
		setUpPortrayals();
	}

	// Called when simulation is loaded from a checkpoint (not relevant for us so far)
	@Override
	public void load(SimState state)
	{
		super.load(state);
		setUpPortrayals();
	}
	
	@Override
	public void quit()
	{
		super.quit();
		if (frame!=null) {
			frame.dispose();
			frame = null;
		}
		display = null;
	}

	// Is called when the GUI object is initially created
	@Override
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
		// My locale was set to GERMAN by default and this can crush mason
		// because of number formats (3.14 is 3,14 in German format)
		Locale.setDefault(Locale.ENGLISH);
		
		// Create the GUI
		new BeeGUI().createController();
	}

}
