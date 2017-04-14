package com.sica.environment;

public class EnvironmentTypes {
	//unique id objects
	//assing in HEX, first 2 bytes are metadata, last 2 are the unique id
	//metadata is unused for now, but do not assign ids over 0xffff just in case we later
	//use it
	public static final short EMPTY = 0x0;
	public static final short OBSTACLE = 0x1;
	public static final short HIVE = 0x2;	
	public static final short FLOWER = 0x3;
	
	
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
	public static boolean isType(int environment, short type) {
		return (environment & 0xffff) == type; 
	}
	
	/**
	 * Extracts the environment metadata from a given value. It ranges
	 * from 0 to 65536
	 * @param environment
	 * @return
	 */
	public static short extractMetadata(int environment) {
		return (short) ((environment >> 16) & 0xffff);
	}
	
	/**
	 * Create an environment with the given type and metadata
	 * Useful for things like indicating that a specific flower
	 * has a certain amount of food.
	 * @param environment
	 * @param metadata
	 * @return
	 */
	public static int inyectMetadata(short type, short metadata) {
		return (metadata << 16) | type;
	}
}
