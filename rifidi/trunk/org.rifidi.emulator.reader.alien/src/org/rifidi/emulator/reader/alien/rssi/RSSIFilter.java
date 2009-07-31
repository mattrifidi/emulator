/**
 * 
 */
package org.rifidi.emulator.reader.alien.rssi;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the logic for 'RSSIFilter' in an Alien reader. See
 * page 108 in the Reader Interface Guide supplied with the Alien Reader.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle
 */
public class RSSIFilter {

	private List<Float[]> filterAtoms = new ArrayList<Float[]>();

	private boolean enabled = false;

	/**
	 * Constructor for the RSSIFilter class. There must be an even number of
	 * arguments, and the number of arguments must be less then 8.
	 * Alternatively, you could supply the argument '0' and the filter will be
	 * turned off.
	 * 
	 * @param args
	 * @throws RSSIException
	 */
	public RSSIFilter(Float... args) throws RSSIException {
		if (args.length == 1) {
			if (args[0] != 0.0f) {
				throw new RSSIException("Invalid value");
			} else {
				// If value is 0, there are no filter atoms
				this.enabled = false;
				return;
			}
		} else if (args.length > 8 || (args.length % 2) != 0) {
			throw new RSSIException();
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
	 * Returns true if the given Float matches any of the filters.
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
	private boolean inclusive(Float x, Float a, Float b) {
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
	private boolean exclusive(Float x, Float a, Float b) {
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
	private boolean exact(Float x, Float a) {
		// System.out.println("Exact has been called, x=" + x + ", a=" + a);
		return x == a;
	}
}
