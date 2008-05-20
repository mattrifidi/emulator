/**
 * 
 */
package org.rifidi.utilities.math;

/**
 * @author dan
 */
public class FloatRounder {
	public static float round( float f, int places ) {
		int factor = (int) Math.pow(10,places);
		f *= factor;
		f = Math.round(f);
		f /= factor;
		return f;
	}
}
