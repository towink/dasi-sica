package util;

import java.io.Serializable;

/**
 * Class for making it easy to work with vectors and points
 * @author Daniel
 *
 */
public class TwoDVector implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3255879516837694646L;
	
	public double x,y = 0;
	
	/**
	 * Initialize to the default value (0, 0)
	 */
	public TwoDVector() {}
	
	/**
	 * Create a TwoDVector with the given coordinates
	 * @param x
	 * @param y
	 */
	public TwoDVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Create a copy of this object
	 */
	public TwoDVector clone() {
		return new TwoDVector(this.x, this.y);
	}
	
	
	/**
	 * Multiply a vector by a scalar value
	 * @param value
	 * @return a new vector with the given result
	 */
	public TwoDVector getDot(double value) {
		TwoDVector result = this.clone();
		result.dot(value);
		return result;
	}
	
	/**
	 * Multiply the current vector by the given scalar value
	 * @param value
	 */
	public void dot(double value) {
		this.x *= value;
		this.y *= value;
	}
	
	/**
	 * Adds a vector onto the current one, modifying the object
	 * @param dot
	 */
	public void add(TwoDVector other) {
		this.x += other.x;
		this.y += other.y;
	}
	
	/**
	 * @return the norm of the vector
	 */
	public double norm() {
		return Math.sqrt(this.x*this.x + this.y*this.y);
	}

	/**
	 * Clamps the vector's coordinates between min and max
	 * @param min
	 * @param max
	 */
	public void clamp(double min, double max) {
		this.x = Math.max(min, Math.min(max, this.x));
		this.y = Math.max(min, Math.min(max, this.y));
	}
	
	
    @Override
	public String toString() {
    	return "(" + this.x + ", " + this.y + ")";
	}

	
	
	
}