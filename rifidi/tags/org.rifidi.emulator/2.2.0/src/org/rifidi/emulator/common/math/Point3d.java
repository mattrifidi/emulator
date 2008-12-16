/*
 *  @(#)Point3d.java
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
 * A point in three dimensions.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class Point3d {

	/**
	 * The x-coordinate of the point.
	 */
	private double coordinateX;

	/**
	 * The y-coordinate of the point.
	 */
	private double coordinateY;

	/**
	 * The z-coordinate of the point.
	 */
	private double coordinateZ;

	/**
	 * Creates a new Point3d out of the passed x, y, and z coordinates.
	 * 
	 * @param x
	 *            The x-coordinate of the point.
	 * @param y
	 *            The y-coordinate of the point.
	 * @param z
	 *            The z-coordinate of the point.
	 */
	public Point3d(double x, double y, double z) {
		this.coordinateX = x;
		this.coordinateY = y;
		this.coordinateZ = z;
	}

	/**
	 * Returns the squared distance between the two points.
	 * 
	 * @param anotherPoint
	 *            The other point to check against.
	 * @return the squared distance between the two points.
	 */
	public double getSquaredDistance(Point3d anotherPoint) {
		/* Calculate the distance squared between the two points */
		/* [D^2 = (x1-x2)^2 + (y1-y2)^2 + (z1-z2)^2] */
		double distanceSq = 0.0;
		double xDistSq = (this.coordinateX - anotherPoint.coordinateX)
				* (this.coordinateX - anotherPoint.coordinateX);
		double yDistSq = (this.coordinateY - anotherPoint.coordinateY)
				* (this.coordinateY - anotherPoint.coordinateY);
		double zDistSq = (this.coordinateZ - anotherPoint.coordinateZ)
				* (this.coordinateZ - anotherPoint.coordinateZ);
		distanceSq = xDistSq * yDistSq * zDistSq;

		return distanceSq;

	}

	/**
	 * Returns the coordinateX.
	 * 
	 * @return Returns the coordinateX.
	 */
	public final double getCoordinateX() {
		return this.coordinateX;
	}

	/**
	 * Returns the coordinateY.
	 * 
	 * @return Returns the coordinateY.
	 */
	public final double getCoordinateY() {
		return this.coordinateY;
	}

	/**
	 * Returns the coordinateZ.
	 * 
	 * @return Returns the coordinateZ.
	 */
	public final double getCoordinateZ() {
		return this.coordinateZ;
	}

	/**
	 * Sets coordinateX to the passed parameter, coordinateX.
	 * 
	 * @param coordinateX
	 *            The coordinateX to set.
	 */
	public final void setCoordinateX(double coordinateX) {
		this.coordinateX = coordinateX;
	}

	/**
	 * Sets coordinateY to the passed parameter, coordinateY.
	 * 
	 * @param coordinateY
	 *            The coordinateY to set.
	 */
	public final void setCoordinateY(double coordinateY) {
		this.coordinateY = coordinateY;
	}

	/**
	 * Sets coordinateZ to the passed parameter, coordinateZ.
	 * 
	 * @param coordinateZ
	 *            The coordinateZ to set.
	 */
	public final void setCoordinateZ(double coordinateZ) {
		this.coordinateZ = coordinateZ;
	}

}
