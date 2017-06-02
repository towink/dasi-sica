package com.sica.behaviour.common;


import com.sica.behaviour.Task;
import com.sica.entities.agents.Agent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import sim.util.Int2D;

/**
 * When this task is executed on an agent, it will move to the point specified as
 * destination, computing shortest paths based on the knowledge of this agent about
 * the world.
 * 
 * After moving to a new position, the environment is observed and knowledge about obstacles
 * is inserted.
 * 
 * If the current path turns out to be impossible because of obstacles or other things
 * in the way, then paths are recomputed.
 * 
 * @author Tobias
 *
 */
public class TaskGetToPosition extends Task {

	private Int2D destination;
	
	private int sleep;
	private int count;
	
	public TaskGetToPosition(Int2D destination, int sleep) {
		this.destination = destination;
		this.sleep = sleep;
		this.count = 0;
	}

	public TaskGetToPosition(Int2D destination) {
		this(destination, 0);
	}
	

	@Override
	public void interactWith(Agent agent, SimulationState simState) {
		if(count >= sleep) {
			if (!agent.followCurrentPath(simState)) {
				agent.computePath(simState, destination);
			}
			count = 0;
		}
		else {
			count++;
		}
		
		/*// a.move(Direction.values()[simState.random.nextInt(4)], simState, Grid2D.TOROIDAL);
		
		// the first step in a get-to-position task is to observe the environment nearby
		// in order to find obstacles
		ObjectiveDrivenWorkerBee bee = (ObjectiveDrivenWorkerBee) agent;
		agent.observeEnvironment(simState, Knowledge.OBSTACLE);
		
		// check if agent already has a path
		List<Int2D> path = bee.getActualPath();
		if(path != null && !path.isEmpty()) {
			
			// check if this path already goes to this tasks destination
			if(path.get(path.size() - 1).equals(destination)) {
				
				// now we try to move to the next point in the path
				Int2D nextPoint = path.remove(0);
				// check if we can actually move to this location, i.e. that it is no obstacle
				// or the environment boundary (in case of a bounded environment)
				if(agent.canMoveTo(nextPoint, simState, SimulationConfig.ENV_MODE)) {
					
					// finally move to this point
					simState.entities.setObjectLocation(agent, nextPoint);
				}
				else {
					// cannot move to next point - recalculate path
					//System.out.println("Recomputing path: cant move to next point in old path");
					agent.computePath(simState, destination);
				}
			}
			else {
				// agent has a path stored but its not the right one
				//System.out.println("Recomputing path: path stored is not the right one");
				agent.computePath(simState, destination);
			}
		}
		else {
			// agent has no actual path - calculate it
			//System.out.println("Recomputing path: no path yet");
			agent.computePath(simState, destination);
		}*/
	}

	@Override
	public boolean isFinished(Agent a, SimulationState simState) {
		// if the destination (for some strange reason) is an obstacle then
		// we say that this objective is immediately finished
		return simState.entities.getObjectLocation(a).equals(this.destination)
			|| !a.canMoveTo(destination, simState, SimulationConfig.ENV_MODE);
	}

}
