/*
 *  @(#)Sphere.java
 *
 *  Created:	Nov 16, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.common.math;


/**
 * This class represents a sphere object and contains some utility methods for
 * determining intersection with other geometric objects.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class Sphere {

	/**
	 * The center point of the sphere.
	 */
	private Point3d origin;

	/**
	 * The radius of the sphere.
	 */
	private double radius;

	/**
	 * Creates a new Sphere centered at the specifed origin and has a
	 * radius of the size passed.
	 * 
	 * @param origin
	 *            The center point of the sphere.
	 * @param radius
	 *            The radius of the sphere.
	 */
	public Sphere(Point3d origin, double radius) {
		this.origin = origin;
		this.radius = radius;

	}

	/**
	 * Determines whether or not two spheres are intersecting.
	 * 
	 * @param anotherSphere
	 *            The other sphere to check against.
	 * @return True if there is an intersection, false otherwise.
	 */
	public boolean intersect(Sphere anotherSphere) {
		boolean retVal = false;

		/* Get the origin distance */
		double originDistanceSq = this.origin
				.getSquaredDistance(anotherSphere.origin);

		/* Get the sum of the radii, square */
		double sumRadSq = this.radius + anotherSphere.radius;
		sumRadSq *= sumRadSq;

		/* Now compare the values to see if an intersection occured. */
		if (originDistanceSq <= sumRadSq) {
			retVal = true;
		}

		return retVal;

	}

}
