package com.sica.entities.agents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import com.sica.simulation.SimulationState;

import sim.portrayal.DrawInfo2D;

public class DroolsBee extends DroolsAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7010748442357851286L;
	
	DroolsBeeController controller;

	public DroolsBee() {
		super("ksession-DroolsBee", EntityType.DROOLS);
	}

	@Override
	public void stepBeforeFiringRules(SimulationState arg0) {
		this.addObjectToKnowledgeBase(arg0);
		int random = (new Random()).nextInt(2);
		if (random > 0)
			this.controller.setStatus(DroolsBeeController.PRE_FIRING);
		else
			this.controller.setStatus(DroolsBeeController.POST_FIRING);
		//System.out.println(this.controller.status);
	}

	@Override
	public void stepAfterFiringRules(SimulationState arg0) {
		this.controller.setStatus(DroolsBeeController.POST_FIRING);
	}

	@Override
	public void setupSessionKnowledge() {
		this.controller = new DroolsBeeController();
		this.addObjectToKnowledgeBase(this.controller);
		this.addObjectToKnowledgeBase(this);
	}
	
	/**
	 * Use this POJO class to control the bee's behavior via drools
	 * @author Daniel
	 *
	 */
    public static class DroolsBeeController {

    	public static final int UNCONFIGURED = -1;
        public static final int PRE_FIRING = 0;
        public static final int FIRED = 1;
        public static final int POST_FIRING = 2;
        
        private int id;
        private static int idgen = 0;
        /**
         * Give each controller a unique ID for testing purposes
         */
        {
        	idgen++;
        	this.id = idgen;
        }

        private int status = UNCONFIGURED;

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
        
        public int getID() {
        	return this.id;
        }

    }
    
    public final void draw(Object object, Graphics2D graphics, DrawInfo2D info) {

		graphics.setColor( Color.GREEN );
		
		// this code was stolen from OvalPortrayal2D
		int x = (int)(info.draw.x - info.draw.width / 2.0);
		int y = (int)(info.draw.y - info.draw.height / 2.0);
		int width = (int)(info.draw.width);
		int height = (int)(info.draw.height);
		graphics.fillOval(x,y,width, height);

	}

}
