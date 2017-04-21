package com.util;

import java.awt.Point;
import sim.util.Int2D;

public class TypeConversions {

	// avoid this by using consistent point types in project
	public static Int2D pointToInt2D(Point p) {
		return new Int2D(p.x, p.y);
	}
	
	public static Point int2DtoPoint(Int2D p) {
		return new Point(p.x, p.y);
	}
}
