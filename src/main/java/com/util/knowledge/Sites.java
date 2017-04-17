package com.util.knowledge;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class Sites {

	protected HashMap <String, ArrayList<Point>> sites;
	protected boolean newData;
	protected boolean newObstacles;

	public Sites () {
		sites = new HashMap <String, ArrayList<Point>>();
		sites.put("flowers", new ArrayList<Point>());
		sites.put("hive", new ArrayList<Point>());
		sites.put("obstacles", new ArrayList<Point>());
	}

	public boolean equals (Sites auxSites) {	
		return equalsKey (auxSites, "hive") && equalsKey (auxSites, "flowers") && equalsKey (auxSites, "obstacles");
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
		return updateKey (auxSites, "hive") || updateKey (auxSites, "flowers") || updateKey (auxSites, "obstacles");
	}

	private boolean updateKey (Sites auxSites, String key) {
		boolean result = false;
		if (!equalsKey(auxSites, key)) {
			for (Point point: auxSites.get(key)) {
				result |= insert (key, point);
			}
		}
		return result;
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

	public boolean insert (String key, Point site) {
		if (sites.containsKey(key)) {
			if (!sites.get(key).contains(site)) {
				sites.get(key).add(site);
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
