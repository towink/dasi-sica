package com.sica.environment;

import java.awt.Color;

import com.util.knowledge.Knowledge;

import sim.util.gui.ColorMap;

public class EnvironmentColorMap implements ColorMap {

	@Override
	public double defaultValue() {
		return Knowledge.EMPTY.ordinal();
	}

	@Override
	public int getAlpha(double arg0) {
		return 0;
	}

	@Override
	public Color getColor(double arg0) {
		switch (Knowledge.fromInt((int) arg0)) {
		case FLOWER:
			return Color.YELLOW;
		case HIVE:	
			return Color.RED;
		case OBSTACLE:	
			return Color.GRAY;
		//fall back to default behavior
		case EMPTY:	
		default:
			return Color.BLACK;
		}
	}

	@Override
	public int getRGB(double arg0) {
		return getColor(arg0).getRGB();
	}

	@Override
	public boolean validLevel(double arg0) {
		return true;
	}

}
