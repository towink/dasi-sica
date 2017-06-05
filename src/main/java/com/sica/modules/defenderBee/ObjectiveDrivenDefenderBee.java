package com.sica.modules.defenderBee;

import com.sica.behaviour.Objective;
import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

/**
 * Adaptation of the DefenderBee's class to the Objective and Task paradigm
 * 
 * @author Daniel
 * @author David
 *
 */
public class ObjectiveDrivenDefenderBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -3875684229097568234L;

	public ObjectiveDrivenDefenderBee() {
		super(EntityType.DEFENDER_BEE);
	}
	
	@Override
	public void receiveBitOfKnowledge(Int2D where, Knowledge knowledge) {
		//add a new objetive with maximum priority, which will be pursued before anything it has right now
		if (knowledge == Knowledge.ENEMY)
			this.addObjective(new ObjectiveAttackEnemy(where, Objective.VERY_HIGH_PRIORITY, false));
	}
}
