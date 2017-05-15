package com.sica.entities.unmovable;

import java.util.ArrayList;

import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class EnemySpawner extends Entity {
	private static final long serialVersionUID = -3430485150816800473L;
	private static EnemySpawner instance;
	
	private ArrayList <Int2D> positions;
	private int seasonCont;
	private Int2D position;

	public static EnemySpawner getSpawner() {
		return EnemySpawner.instance;
	}
	
	public static void createSpawner (Int2D position) {
		if (EnemySpawner.instance == null) {
			EnemySpawner.instance = new EnemySpawner(position);
		}
	}
	
	private EnemySpawner(Int2D position) {
		super(EntityType.ENEMY_SPAWNER);
		this.positions = new ArrayList<Int2D>();
		this.seasonCont = SimulationConfig.config().getTime4Enemies();
		this.position = position;
	}
	
	public void increaseSeasonCount () {
		this.seasonCont += 1;
	}
	
	public void putPosition (Int2D position) {
		if (!this.positions.contains(position)) {
			this.positions.add(position);
		}
	}
	
	public Int2D getPosition () {
		return this.position;
	}

	@Override
	public void doStep(SimulationState simState) {
		if (this.seasonCont >= simState.config.getTime4Enemies()) {
			for (int i = 0; i < simState.getConfig().getEnemies4Season(); i++) {
				EntityPlacer.deployEntity(
						EntityType.SIMPLE_ENEMY, 
						simState.entities, 
						this.positions.get(simState.random.nextInt(this.positions.size())), 
						simState.schedule);
			}
			this.seasonCont = 0;
		}
		
	}

}
