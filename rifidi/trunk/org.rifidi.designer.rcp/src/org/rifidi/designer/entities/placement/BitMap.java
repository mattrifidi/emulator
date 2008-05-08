/*
 *  BitMap.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.designer.entities.placement;

import org.eclipse.swt.graphics.Point;

/**
 * A class for keeping track of available space. It is a two dimensional map of
 * bits. A bit set to true represents an occupied field.
 * 
 * @author Jochen Mader Oct 30, 2007
 * 
 */
public class BitMap {
	/**
	 * The whole map.
	 */
	private boolean[][] map;
	/**
	 * Length of the side.
	 */
	private Integer sideLength;

	/**
	 * Empty constructor for JAXB
	 */
	public BitMap() {
	}

	/**
	 * Constructor.
	 * 
	 * @param sideLength
	 *            length of one side of the sqare map
	 */
	public BitMap(Integer sideLength) {
		map = new boolean[sideLength][sideLength];
		this.sideLength = sideLength;
	}

	/**
	 * Check if a given pattern collides on the bitmap.
	 * 
	 * @param position
	 *            position of the pattern
	 * @param pattern
	 *            the pattern itself
	 * @return true if the given position results in a collision, false if the
	 *         space is free
	 */
	public boolean checkCollision(final Point position,final boolean[][] pattern) {
		if (pattern.length > 0) {
			if (position.x >= 0 && position.y >= 0
					&& position.x + pattern[0].length - 1 < map[0].length
					&& position.y + pattern.length - 1 < map.length) {
				for (int countY = 0; countY < pattern.length; countY++) {
					for (int countX = 0; countX < pattern[countY].length; countX++) {
						if (map[countY + position.y][countX + position.x] == true
								&& pattern[countY][countX]) {
							return true;
						}
					}
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds the pattern to the map.
	 * 
	 * @param position
	 *            position on the map
	 * @param pattern
	 */
	public void addPattern(final Point position, final boolean[][] pattern) {
		for (int countY = 0; countY < pattern.length; countY++) {
			for (int countX = 0; countX < pattern[countY].length; countX++) {
				if (pattern[countY][countX] == true) {
					map[countY + position.y][countX + position.x] = true;
				}
			}
		}
	}

	/**
	 * Removes the pattern to the map.
	 * 
	 * @param position
	 *            position on the map
	 * @param pattern
	 */
	public void removePattern(final Point position, final boolean[][] pattern) {
		try {
			for (int countY = 0; countY < pattern.length; countY++) {
				for (int countX = 0; countX < pattern[countY].length; countX++) {
					if (pattern[countY][countX] == true) {
						map[countY + position.y][countX + position.x] = false;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: curse it
		}
	}

	/**
	 * Set the given bit to true
	 * 
	 * @param position
	 *            the position of the bit
	 */
	public void set(final Point pos) {
		map[pos.y][pos.x] = true;
	}

	/**
	 * Set the given bit to false
	 * 
	 * @param pos
	 *            the position of the bit
	 */
	public void unset(final Point pos) {
		map[pos.y][pos.x] = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int countY = 0; countY < sideLength; countY++) {
			for (int countX = 0; countX < sideLength; countX++) {
				if (map[countY][countX]) {
					sb.append("1 ");
				} else {
					sb.append("0 ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * @return the sideLength
	 */
	public Integer getSideLength() {
		return sideLength;
	}

	/**
	 * @param sideLength
	 *            the sideLength to set
	 */
	public void setSideLength(final Integer sideLength) {
		this.sideLength = sideLength;
	}

	/**
	 * @return the map
	 */
	public boolean[][] getMap() {
		return map;
	}

	/**
	 * @param map
	 *            the map to set
	 */
	public void setMap(final boolean[][] map) {
		this.map = map;
	}
}
