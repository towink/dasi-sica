package agents;

import java.util.Random;

import jade.core.Agent;
import util.TwoDVector;

/**
 * Wrapper which allows movement of the underlying agent
 * @author Daniel, Tobias
 *
 */
public class MovableAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8898201660520451909L;
	
	// used for random movement
    private Random r = new Random();
	
	// position, velocity, acceleration
	private TwoDVector pos;
	private TwoDVector vel;
	private TwoDVector acc;
    
	public MovableAgent() {
		this.pos = new TwoDVector(0.0, 0.0);
		this.vel = new TwoDVector(0.0, 0.0);
		this.acc = new TwoDVector(0.0, 0.0);
	}

	/**
	 * @return This Agent's position
	 */
	public TwoDVector getPos() {
		return pos;
	}
	
	/**
	 * Move this agent assuming a time equal to
	 * elapsedTime has passed
	 * @param elapsedTime
	 */
    public void move(double elapsedTime) {
    	this.vel.add(this.acc.getDot(elapsedTime));
        double max = 0.01;
        double norm = this.vel.norm();
        if(norm > max)
        	this.vel.dot(max / norm);
        
        this.pos.add(this.vel.getDot(elapsedTime));
        this.pos.clamp(-1.0, 1.0);
    }
    
    /**
     * Set the acceleration of this agent to a completely random one
     */
    public void randomlyAccelerate() {
        double max = 0.001;
        this.acc = new TwoDVector(max * (r.nextDouble() * 2 - 1), max * (r.nextDouble() * 2 - 1));
        double norm = this.acc.norm();
        if(norm > max) 
        	this.acc.dot(max / norm);
        
        //System.out.println(this.acc);
    }
}
