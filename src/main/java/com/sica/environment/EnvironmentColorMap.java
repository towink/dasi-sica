package com.sica.environment;

import java.awt.Color;

import sim.util.gui.ColorMap;

public class EnvironmentColorMap implements ColorMap {

	@Override
	public double defaultValue() {
		return EnvironmentTypes.EMPTY;
	}

	@Override
	public int getAlpha(double arg0) {
		return 0;
	}

	@Override
	public Color getColor(double arg0) {
		switch ((short) arg0) {
		case EnvironmentTypes.FLOWER:
			return Color.YELLOW;
		case EnvironmentTypes.HIVE:	
			return Color.RED;
		case EnvironmentTypes.OBSTACLE:	
			return Color.GRAY;
		//fall back to default behavior
		case EnvironmentTypes.EMPTY:	
		default:
			return Color.WHITE;
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
