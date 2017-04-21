package com.sica.entities.agents;

import com.util.knowledge.HashMapKnowledgeMap;

public class ObjectiveDrivenWorkerBee extends ObjectiveDrivenAgent {
	private static final long serialVersionUID = -1667202705195646436L;

	public ObjectiveDrivenWorkerBee() {
		super(EntityType.OBJECTIVE_DRIVEN, new HashMapKnowledgeMap());
	}

}
