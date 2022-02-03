/**
 * 
 */
package org.rifidi.emulator.reader.alien.speed;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the logic for 'SpeedFilter' in an Alien reader. See
 * page 108 in the Reader Interface Guide supplied with the Alien Reader.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle
 */
public class SpeedFilter {

	private List<Float[]> filterAtoms = new ArrayList<Float[]>();

	private boolean enabled = false;

	/**
	 * Constructor for the SpeedFilter class. There must be an even number of
	 * arguments, and the number of arguments must be less then 8.
	 * Alternatively, you could supply the argument '0' and the filter will be
	 * turned off.
	 * 
	 * @param args
	 * @throws SpeedException
	 */
	public SpeedFilter(Float... args) throws SpeedException {
		if (args.length == 1) {
			if (args[0] != 0.0f) {
				throw new SpeedException("Invalid value");
			} else {
				// If value is 0, there are no filter atoms
				this.enabled = false;
				return;
			}
		} else if (args.length > 8 || (args.length % 2) != 0) {
			throw new SpeedException();
		}
		// We know that there are 8 or less values, and that there are an even
		// number of them.
		for (int i = 0; i < args.length; i += 2) {
			Float[] atom = new Float[2];
			atom[0] = args[i];
			atom[1] = args[i + 1];
			filterAtoms.add(atom);
		}
		this.enabled = true;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns true if the given float matches any of the filters.
	 * 
	 * @param f
	 * @return
	 */
	public boolean matches(Float f) {
		// System.out.println("Filter has been called, f=" + f);
		if (filterAtoms.isEmpty()) {
			return true;
		}
		for (Float[] atom : this.filterAtoms) {
			if (atom[0] > atom[1]) {
				if (this.exclusive(f, atom[0], atom[1])) {
					return true;
				}
			} else if (atom[0] < atom[1]) {
				if (this.inclusive(f, atom[0], atom[1])) {
					return true;
				}
			} else {
				if (this.exact(f, atom[0])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if a <= x <= b.
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean inclusive(float x, float a, float b) {
		// System.out.println("Inclusive has been called, x=" + x + ", a=" + a
		// + ", b=" + b);
		if (x >= a && x <= b) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if x <= a or x >= b.
	 * 
	 * @param x
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean exclusive(float x, float a, float b) {
		// System.out.println("Exclusive has been called, x=" + x + ", a=" + a
		// + ", b=" + b);
		if (x >= a || x <= b) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if x is equal to a.
	 * 
	 * @param x
	 * @param a
	 * @return
	 */
	private boolean exact(float x, float a) {
		// System.out.println("Exact has been called, x=" + x + ", a=" + a);
		return x == a;
	}
}
