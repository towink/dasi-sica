package com.util;

public class BeeMath {
	
	/**
	 * @param number
	 * @return -1 if below 0, 1 if above 0, 0 if equal to zero
	 */
	public static int sign(float number) {
		return number < 0 ? -1 : number > 0 ? 1 : 0;
	}

}
