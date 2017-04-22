package com.util.knowledge;

/**
 * Class used to define all knowledge that a bee can learn.
 * It also includes methods to transform it to integers and allow the 
 * insertion of metadata, for more complex behaviours in the environments,
 * like storing the amount of food a FLOWER has and such.
 * @author Daniel
 *
 */
public enum Knowledge {
	//do not declare more than 65536 (0xffff) different types or the class will break!
	UNKNOWN, HIVE, FLOWER, OBSTACLE, EMPTY, BEE, ENEMY;

	/**
	 * Chech if a given environment value is of the given type.
	 * Keep in mind that when using environment metadata, the same
	 * type can have multiple values depending on it, so use
	 * this function instead of directly comparing. It should not
	 * have any overhead in calculations
	 * @param environment
	 * @param type
	 * @return
	 */
	public static boolean isType(int environmentValue, Knowledge type) {
		return (environmentValue & 0xffff) == type.ordinal(); 
	}
	
	
	/**
	 * Extracts the environment metadata from a given value. It ranges
	 * from 0 to 65536
	 * @param environment
	 * @return
	 */
	public static short extractMetadata(int environmentValue) {
		return (short) ((environmentValue >> 16) & 0xffff);
	}
	
	
	/**
	 * Create an environment with the given type and metadata
	 * Useful for things like indicating that a specific flower
	 * has a certain amount of food.
	 * @param environment
	 * @param metadata
	 * @return
	 */
	public int inyectMetadata(short metadata) {
		return (metadata << 16) | this.ordinal();
	}
	
	/**
	 * Transforms this enum to an integer value. Same as calling ordinal()
	 * @return
	 */
	public int toInt() {
		return this.ordinal();
	}
	
	/**
	 * @param environmentValue
	 * @return the Knowledge represented by the given environmentValue, regardless
	 * of the metadata.
	 */
	public static Knowledge fromInt(int environmentValue) {
		int size = Knowledge.values().length;
		int index = environmentValue & 0xffff;
		if (index >= size)
			return Knowledge.UNKNOWN;
		else
			return Knowledge.values()[index];
	}
}
