package com.sica.entities.unmovable;

import com.sica.entities.Entity;
import com.sica.entities.EntityPlacer;
import com.sica.simulation.SimulationConfig;
import com.sica.simulation.SimulationState;

import sim.util.Int2D;

public class EnemySpawner extends Entity{
	private static final long serialVersionUID = -5584094385810899356L;
	private Int2D position;
	private EntityType enemy;
	private int seasonCont;
	private int time4Enemy;
	private int nEnemies;
	
	public EnemySpawner(Int2D position, EntityType enemy, int time4Enemy, int nEnemies) {
		super(EntityType.ENEMY_SPAWNER);
		this.seasonCont = 0;
		if (availablePosition(position)) {
			this.position = new Int2D(position.x, position.y);
		}
		else{
			System.err.println("WARNING: Position not allowed");
			this.position = null;
		}
		
		if (availableEnemy(enemy)) {
			this.enemy = enemy;
		}
		else {
			System.err.println("WARNING: Type of enemy not allowed");
			enemy = null;
		}
		
		if (time4Enemy > 0) {
			this.time4Enemy = time4Enemy;
		}
		else {
			this.time4Enemy = 1;
		}
		
		if (nEnemies > 0) {
			this.nEnemies = nEnemies;
		}
		else {
			this.nEnemies = 1;
		}
	}
	
	private boolean availableEnemy(EntityType enemy) {
		boolean result;
		switch (enemy) {
		case ENEMY:
			result = true;
			break;
		default:
			result = false;
			break;
		}
		
		return result;
	}
	
	private boolean availablePosition (Int2D position) {
		return position.x >= 0 && position.y >= 0 &&
				position.x < SimulationConfig.GRID_WIDTH &&
				position.y < SimulationConfig.GRID_HEIGHT;
	}
	
	public void increaseSeasonCount () {
		this.seasonCont += 1;
	}
	
	public Int2D getPosition () {
		return this.position;
	}
	
	@Override
	public void doStep(SimulationState simState) {
		if ((this.seasonCont >= this.time4Enemy) && (this.enemy != null) && (this.position != null)) {
			for (int i = 0; i < this.nEnemies; i++) {
				EntityPlacer.deployEntity(
						this.enemy, 
						simState.entities, 
						this.position, 
						simState.schedule);
			}
			this.seasonCont = 0;
		}
		
	}
}
