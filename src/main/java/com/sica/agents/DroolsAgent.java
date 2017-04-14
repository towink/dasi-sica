package com.sica.agents;

import java.util.Collection;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import sim.engine.SimState;

/**
 * Agent that can be controlled via drools rule-based system
 * this abstact class leaves the key methods unimplemented so
 * the implementing subclass can have full control about 
 * their behavior.
 * @author Daniel
 *
 */
public abstract class DroolsAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 488619554968170669L;
	private KieSession kSession;
	private int maxRulesFired;
	
	/**
	 * Create a drools-based agent using the specified knowledge session
	 * name. Check kmodule.xml to set up which rule files the knowledge
	 * session uses.
	 * @param kSessionName
	 */
	public DroolsAgent(String kSessionName) {
		super();
		this.setUpAgent(kSessionName, 0);
	}
	
	/**
	 * Use this constructor if you want to set up a maximum number of
	 * rules that can be triggered on each step. This avoids infinite
	 * loops
	 * @param kSessionName
	 * @param maxRulesFired
	 */
	public DroolsAgent(String kSessionName, int maxRulesFired) {
		this.setUpAgent(kSessionName, maxRulesFired);
	}
	
	private void setUpAgent(String kSessionName, int maxRulesFired) {
        try {
            // load up the knowledge base
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
    	    this.kSession = kContainer.newKieSession(kSessionName);
    	    // load the objects that make the session
    	    setupSessionKnowledge();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        this.maxRulesFired = maxRulesFired;
	}
	
	
	/**
	 * Function called ON EACH STEP before firing the rules. Use it to set
	 * up stuff
	 * @param arg0
	 */
	public abstract void stepBeforeFiringRules(SimState arg0);
	/**
	 * Function called ON EACH STEP after firing the rules. Use to set up 
	 * stuff
	 * @param arg0
	 */
	public abstract void stepAfterFiringRules(SimState arg0);
	/**
	 * This function is called in the constructor. Before any calls
	 * to the stepping functions are made. Use it to set up the class
	 * and add objects to the knowledge base
	 */
	public abstract void setupSessionKnowledge();
	

	public void step (final SimState arg0) {     
		//let the subclass do something before rules are fired
		this.stepBeforeFiringRules(arg0);
		//update all the knowledge base objects so that rules can fire again
		this.updateFactHandles();
		//fire all rules OR a finite number to avoid infinite loops
		if (maxRulesFired > 0)
			this.kSession.fireAllRules(maxRulesFired);
		else
			this.kSession.fireAllRules();
        //let the subclass do stuff after firing rules
        this.stepAfterFiringRules(arg0);
	}
	
	/**
	 * Adds an object to the underlying knowledge base
	 * @param o
	 */
	public void addObjectToKnowledgeBase(Object o) {
		this.kSession.insert(o);
	}
	
	/**
	 * Function needed to call fireAllRules() more than one time on
	 * the same object. Otherwise the rule motor ignores objects that had rules already fired on them.
	 * One could also rebuild the rule engine on each step, but this approach seems more 
	 * reasonable
	 */
	private void updateFactHandles() {
		Collection<? extends Object> objects = (Collection<? extends Object>) this.kSession.getObjects();
        for (Object o: objects) {
        	FactHandle fh = this.kSession.getFactHandle(o);
        	this.kSession.update(fh, o);
        }
	}

	
}
