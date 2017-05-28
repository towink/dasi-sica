package com.sica.entities.unmovable;

import java.util.ArrayList;

import sim.util.Int2D;

public class ListEnemySpawner{
	private static ListEnemySpawner instance;
	
	private ArrayList <EnemySpawner> spawners;
	private int size;

	public static ListEnemySpawner getListSpawner() {
		if (ListEnemySpawner.instance == null) {
			ListEnemySpawner.instance = new ListEnemySpawner();
		}
		
		return ListEnemySpawner.instance;
	}
	
	
	private ListEnemySpawner() {
		this.spawners = new ArrayList<EnemySpawner>();
		this.size = 0;
	}
	
	public void putSpawner (EnemySpawner spawner) {
		for (EnemySpawner i : this.spawners) {
			if (i.getPosition() == spawner.getPosition()) {
				return ;
			}
		}
		
		this.size += 1;
		this.spawners.add(spawner);
	}
	
	public int size () {
		return this.size;
	}
	
	public void increaseSeasonCount () {
		for (EnemySpawner i : this.spawners) {
			i.increaseSeasonCount();
		}
	}
	
	public ArrayList<EnemySpawner> getSpawners () {
		return this.spawners;
	}
	
	public EnemySpawner getSpawner (int index) {
		if ((index >= 0) && (index < this.spawners.size())) {
			return this.spawners.get(index);
		}
		
		return null;
	}
	
	public EnemySpawner getSpawner (Int2D position) {
		for (EnemySpawner i : this.spawners) {
			if (i.getPosition() == position) {
				return i;
			}
		}
		
		return null;
	}

}
