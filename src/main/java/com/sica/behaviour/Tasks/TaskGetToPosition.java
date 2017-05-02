package com.sica.behaviour.Tasks;

import java.util.List;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;
import com.util.knowledge.Knowledge;
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

	public TaskGetToPosition(Int2D destination) {
		this.destination = destination;
	}
	

	@Override
	public void interactWith(ObjectiveDrivenAgent agent, SimulationState simState) {
		// a.move(Direction.values()[simState.random.nextInt(4)], simState, Grid2D.TOROIDAL);
		
		// the first step in a get-to-position task is to observe the environment nearby
		// in order to find obstacles
		agent.observeEnvironment(simState, Knowledge.OBSTACLE);
		
		// check if agent already has a path
		List<Int2D> path = agent.getActualPath();
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
		}
	}

	@Override
	public boolean isFinished(ObjectiveDrivenAgent a, SimulationState simState) {
		// if the destination (for some strange reason) is an obstacle then
		// we say that this objective is immediately finished
		return simState.entities.getObjectLocation(a).equals(this.destination)
			|| simState.environment.hasTypeAt(
					destination,
					Knowledge.OBSTACLE);
	}

}
