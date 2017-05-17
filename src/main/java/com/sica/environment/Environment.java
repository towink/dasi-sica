package com.sica.environment;

import com.sica.simulation.SimulationConfig;
import com.util.knowledge.Knowledge;

import sim.field.grid.IntGrid2D;
import sim.util.Int2D;
import sim.util.IntBag;

public class Environment extends IntGrid2D {
	private static final long serialVersionUID = 8925118805389868575L;

	private static Environment instance;
	
	public static Environment getEnvironment(int width, int height) {
		if (instance == null) {
			instance = new Environment(width, height);
		}
		
		return instance;
	}
	
	public static Environment getNewEnvironment(int width, int height) {
		instance = new Environment(width, height);
		return instance;
	}
	
	private Environment(int width, int height) {
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
	
	public boolean hasAlimentAt (Int2D pos) {
		return hasTypeAt(pos, Knowledge.FLOWER);
	}
	
	public boolean getAlimentAt(Int2D pos) {
		if (hasAlimentAt(pos)) {
			setMetadataAt(pos, getMetadataAt(pos) - 1);
			if (getMetadataAt(pos) <= 0) {
				set(pos, Knowledge.EMPTY);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Gets the metadata at the given position
	 * @param pos
	 * @return
	 */
	public short getMetadataAt(Int2D pos) {
		return Knowledge.extractMetadata(this.get(pos.x, pos.y));
	}
	
	/**
	 * Gets the knowledge at the given position
	 * without the metadata!
	 * @param pos
	 * @return
	 */
	public Knowledge getKnowledgeAt(Int2D pos) {
		return Knowledge.fromInt(this.get(pos.x, pos.y));
	}
	
	/**
	 * Gets the knowledge at the given position
	 * without the metadata!
	 * @param x
	 * @param y
	 * @return
	 */
	public Knowledge getKnowledgeAt(int x, int y) {
		return Knowledge.fromInt(this.get(x, y));
	}
	
	/**
	 * Sets the metadata at the given position.
	 * Existing knowledge is preserved
	 * @param pos
	 * @param meta
	 */
	public void setMetadataAt(Int2D pos, int meta) {
		Knowledge knowledge = Knowledge.fromInt(this.get(pos.x, pos.y));
		this.set(pos, knowledge.inyectMetadata((short)meta));
	}
	
	/**
	 * Sets the type at the specified position,
	 * overwriting existing metadata
	 * @param x
	 * @param y
	 * @param type
	 */
	public void set(int x, int y, Knowledge type) {
		this.set(x, y, type.toInt());
	}
	
	/**
	 * Sets the type at the specified position,
	 * overwriting existing metadata
	 * @param x
	 * @param y
	 * @param type
	 */
	public void set(Int2D pos, Knowledge type) {
		this.set(pos.x, pos.y, type.toInt());
	}
	
	/**
	 * Sets the given metadata and knowledge at the given position
	 * @param pos
	 * @param metadata
	 * @param knowledge
	 */
	public void setMetadataAndTypeAt(Int2D pos, short metadata, Knowledge knowledge) {
		int value = knowledge.inyectMetadata(metadata);
		this.set(pos, value);
	}
	
	/**
	 * Sets the value at the given point.
	 * The value includes both metadata and type.
	 * The former is not checked for validity, so call 
	 * this only if you know what you are doing.
	 * @param x
	 * @param y
	 * @param type
	 */
	private void set(Int2D pos, int envValue) {
		this.set(pos.x, pos.y, envValue);
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
	 * Change every type to EMPTY
	 * @param type to change to EMPTY
	 */
	public void removeAll (Knowledge type) {
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.hasTypeAt(i,  j, type)) {
					this.set(i, j, Knowledge.EMPTY);
				}
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
			for (; j < center.y+length.y/2; j++) {
				this.set(i, j, value);
			}
			j = center.y -length.y/2;
		}
	}
	
	/**
	 * Check if a position is inside the hive
	 * @param checkPos
	 * @param hivePos
	 * @return true if checkPos is inside the hive, otherwise false
	 * 
	 * true if this environment has the specified type
	 * at the specified coordinates
	 */
	public boolean inHive (Int2D checkPos, Int2D hivePos) {
		int minX = (int) (hivePos.getX() - SimulationConfig.HIVE_WIDTH/2);
		int minY = (int) (hivePos.getY() - SimulationConfig.HIVE_HEIGHT/2);
		int maxX = (int) (hivePos.getX() + SimulationConfig.HIVE_WIDTH/2);
		int maxY = (int) (hivePos.getY() + SimulationConfig.HIVE_HEIGHT/2);

		if (checkPos.x >= maxX || checkPos.x <= minX) {
			return false;
		}

		if (checkPos.y >= maxY || checkPos.y <= minY) {
			return false;
		}

		return true;
	} 
		
	/**
	 * Gets the count of instances of the given knowledge in this map
	 * @param knowledge
	 * @return
	 */
	public int getCountOf(Knowledge knowledge) {
		int counter = 0;
		for (int i = 0; i < this.width; i++) {
			for (int j = 0; j < this.height; j++) {
				if (this.getKnowledgeAt(i, j) == knowledge)
					counter++;
			}
		}
		return counter;
	}
	
}
