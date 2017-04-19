package com.util.knowledge;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Sites {

	public static final String OBSTACLES = "obstacles";
	public static final String FLOWERS = "flowers";
	public static final String HIVES = "hive";
	
	protected HashMap <String, ArrayList<Point>> sites;
	protected boolean newData;
	protected boolean newObstacles;

	public Sites () {
		sites = new HashMap <String, ArrayList<Point>>();
		sites.put(FLOWERS, new ArrayList<Point>());
		sites.put(HIVES, new ArrayList<Point>());
		sites.put(OBSTACLES, new ArrayList<Point>());
	}

	public boolean equals (Sites auxSites) {	
		return equalsKey (auxSites, HIVES) && equalsKey (auxSites, FLOWERS) && equalsKey (auxSites, OBSTACLES);
	}

	private boolean equalsKey (Sites auxSites, String key) {
		for (int i = 0; i < auxSites.get(key).size(); i++) {
			if (!sites.get(key).contains(auxSites.get(key).get(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean updateSites (Sites auxSites) {
		return updateKey (auxSites, HIVES) || updateKey (auxSites, FLOWERS) || updateKey (auxSites, OBSTACLES);
	}

	private boolean updateKey (Sites auxSites, String key) {
		boolean result = false;
		for (Point point: auxSites.get(key)) {
			result |= insert (key, point);
		}
		return result;
	}

	@Override
	public String toString () {
		String value = "Knowledge {";
		value += "\n\tHives: " + sites.get(HIVES).size();
		value += "\n\tFlowers: " + sites.get(FLOWERS).size();
		value += "\n\tObstacles: " + sites.get(OBSTACLES).size();
		value += "\n}";
		return value;
	}
	
	// Getters and Setters
	public ArrayList<Point> get (String key) {
		if (sites.containsKey(key)) {
			return sites.get(key);
		}
		else {
			return new ArrayList<Point> ();
		}
	}

	public boolean insert (String key, Point data) {
		if (sites.containsKey(key)) {
			if (!sites.get(key).contains(data)) {
				sites.get(key).add(data);
				newData = true;
				if (key.equals("obstacles")) {
					newObstacles = true;
				}
				return true;
			}
		}

		return false;
	}

	public void updated () {
		newData = false;
	}

	public boolean newData () {
		return newData;
	}

	public void updatedObstacles () {
		newObstacles = false;
	}

	public boolean newObstacles () {
		return newObstacles;
	}
}
