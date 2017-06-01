package com.sica.modules.defenderBee;

import com.sica.entities.agents.ObjectiveDrivenAgent;
import com.util.knowledge.Knowledge;

import sim.util.Int2D;

public class ObjectiveDrivenDefenderBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -3875684229097568234L;
	private Int2D alarm;
	private boolean recivedAlarm;

	public ObjectiveDrivenDefenderBee() {
		super(EntityType.DEFENDER_BEE);
	}
	
	@Override
	public void receiveBitOfKnowledge(Int2D where, Knowledge knowledge) {
		//add a new objetive with maximum priority, which will be pursued before anything it has right now
		if (knowledge == Knowledge.ENEMY)
			this.addObjective(new ObjectiveAttackEnemyFar(where));
		/*if (knowledge == Knowledge.ENEMY) {
			this.alarm = where;
			this.recivedAlarm = true;
		}*/
	}
	
	public Int2D getAlarm() {
		return alarm;
	}

	public boolean isRecivedAlarm() {
		return recivedAlarm;
	}

}
