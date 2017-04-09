package com.sica.agents;

import java.util.Random;

import sim.engine.SimState;

public class DroolsBee extends DroolsAgent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7010748442357851286L;
	
	DroolsBeeController controller;

	public DroolsBee() {
		super("ksession-DroolsBee");
	}

	@Override
	public void stepBeforeFiringRules(SimState arg0) {
		int random = (new Random()).nextInt(2);
		if (random > 0)
			this.controller.setStatus(DroolsBeeController.PRE_FIRING);
		else
			this.controller.setStatus(DroolsBeeController.POST_FIRING);
		//System.out.println(this.controller.status);
	}

	@Override
	public void stepAfterFiringRules(SimState arg0) {
		this.controller.setStatus(DroolsBeeController.POST_FIRING);
	}

	@Override
	public void setupSessionKnowledge() {
		this.controller = new DroolsBeeController();
		this.addObjectToKnowledgeBase(this.controller);
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

}
