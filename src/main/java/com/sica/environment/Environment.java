package com.sica.environment;

import com.util.knowledge.Knowledge;

import sim.field.grid.IntGrid2D;
import sim.util.Int2D;
import sim.util.IntBag;

public class Environment extends IntGrid2D {
	private static final long serialVersionUID = 8925118805389868575L;

	public Environment(int width, int height) {
		super(width, height, Knowledge.EMPTY.ordinal());
	}
	
	/**
	 * @param pos
	 * @param type
	 * @return true if this object has the environmentType type set at position pos
	 */
	public boolean hasTypeAt(Int2D pos, Knowledge type) {
		return this.hasTypeAt(pos.x, pos.y, type);
	}
	
	/**
	 * @param x
	 * @param y
	 * @param type
	 * @return true if this environment has the specified type
	 * at the specified coordinates
	 */
	public boolean hasTypeAt(int x, int y, Knowledge type) {
		return Knowledge.isType(this.get(x, y), type);
	}
	
	
	/**
	 * Sets the type at the specified position
	 * @param x
	 * @param y
	 * @param type
	 */
	public void set(int x, int y, Knowledge type) {
		this.set(x, y, type.ordinal());
	}
	
	
	/**
	 * Return the positions of the cells with the given type in the given search radius and position,
	 * similar to the other getRadialNeighbors(...) functions
	 * @param x center.x
	 * @param y center.y
	 * @param radius radius of the circle in which to look
	 * @param mode TOROIDAL or BOUNDED
	 * @param includeCenter if we search in the center as well
	 * @param type return positions of ONLY the given type
	 */
	public void getRadialNeighbors(int x, int y, int radius, int mode, boolean includeCenter, Knowledge type, IntBag xCoords, IntBag yCoords) {
		IntBag values = new IntBag(), tempXCoords = new IntBag(), tempYCoords = new IntBag();
		this.getRadialNeighbors(x, y, radius, mode, includeCenter, values, tempXCoords, tempYCoords);
		
		for (int i = 0; i < values.numObjs; i++) {
			if (Knowledge.isType(values.get(i), type)) {
				xCoords.add(tempXCoords.get(i));
				yCoords.add(tempYCoords.get(i));
			}
		}	
	}
	
	
	/**
	 * Draw a hollow rectangle of the specified size and type on to the environment
	 * @param center
	 * @param length
	 * @param container
	 * @param value
	 */
	public void drawRect (Int2D center, Int2D length, Knowledge value) {
		for (int i = 0; i <= length.getY()/2; i++) {
			this.set(center.x - length.x/2, center.y+i, value);
			this.set(center.x - length.x/2, center.y-i, value);
			this.set(center.x + length.x/2, center.y+i, value);
			this.set(center.x + length.x/2, center.y-i, value);
		}
		for (int i = 0; i < length.getX()/2; i++) {
			this.set(center.x+i, center.y - length.y/2, value);
			this.set(center.x-i, center.y - length.y/2, value);
			this.set(center.x+i, center.y + length.y/2, value);
			this.set(center.x-i, center.y + length.y/2, value);
		}
	}
	
	
	/**
	 * Draw a filled rectangle of the specified size and type on to the environment
	 * @param center
	 * @param length
	 * @param container
	 * @param value
	 */
	public void drawFillRect (Int2D center, Int2D length, Knowledge value) {		
		int i = center.x-length.x/2;
		int j = center.y-length.y/2;
		for (;i < center.x+length.x/2; i++) {
			for (; j < center.y+length.y/2; j++)
				this.set(i, j, value);
			
			j = center.y -length.y/2;
		}
	}
		
	
}