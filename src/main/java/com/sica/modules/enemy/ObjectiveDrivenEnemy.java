package com.sica.modules.enemy;

import com.sica.entities.agents.ObjectiveDrivenAgent;

/**
 * Adaptation of SimpleEnemy to the Objective-Task model
 * 
 * @author Tobias
 *
 */
public class ObjectiveDrivenEnemy extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -5503355629094859100L;

	public ObjectiveDrivenEnemy() {
		super(EntityType.ENEMY);
	}

}
