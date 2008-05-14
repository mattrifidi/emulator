/*
 *  BinaryPattern.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.placement;

/**
 * This class represents a placement pattern. The pattern basically tells which
 * points in a grid are occupied by the object this pattern is assigned to.
 * 
 * @author Jochen Mader Oct 30, 2007
 * 
 */
public class BinaryPattern implements Cloneable {

	/**
	 * The patterns current state.
	 */
	private boolean[][] pattern;

	/**
	 * The unmodified state of the pattern.
	 */
	private boolean[][] originalPattern;

	/**
	 * Empty constructor for JAXB.
	 */
	public BinaryPattern() {
	}

	/**
	 * Rotate the pattern 90 degree to the right.
	 */
	public void rotateRight() {
		boolean[][] newArray = new boolean[pattern[0].length][pattern.length];
		int countX2 = newArray[0].length - 1;
		int countY2 = 0;
		for (int countY = 0; countY < pattern.length; countY++) {
			for (int countX = 0; countX < pattern[0].length; countX++) {
				newArray[countY2][countX2] = pattern[countY][countX];
				countY2++;
			}
			countY2 = 0;
			countX2--;
		}
		pattern = newArray;
	}

	/**
	 * @return the pattern
	 */
	public boolean[][] getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            the pattern to set
	 */
	public void setPattern(final boolean[][] pattern) {
		if (originalPattern == null) {
			originalPattern = pattern.clone();
		}
		this.pattern = pattern;
	}

	/**
	 * Get the width of the pattern. (This value changes if the pattern is
	 * rotated)
	 * 
	 * @return
	 */
	public Integer getWidth() {
		if (pattern != null && pattern.length > 0) {
			return pattern[0].length;
		}
		return 0;
	}

	/**
	 * Get the length of the pattern. (This value changes if the pattern is
	 * rotated)
	 * 
	 * @return
	 */
	public Integer getLength() {
		if(pattern!=null){
			return pattern.length;	
		}
		return 0;
	}

	/**
	 * @return the originalPattern
	 */
	public boolean[][] getOriginalPattern() {
		return originalPattern;
	}

	/**
	 * @param originalPattern
	 *            the originalPattern to set
	 */
	public void setOriginalPattern(final boolean[][] originalPattern) {
		this.originalPattern = originalPattern;
	}

	/**
	 * Get the width of the original pattern.
	 * 
	 * @return
	 */
	public Integer getOriginalWidth() {
		if (originalPattern.length > 0) {
			return originalPattern[0].length;
		}
		return 0;
	}

	/**
	 * Get the length of the original pattern.
	 * 
	 * @return
	 */
	public Integer getOriginalLength() {
		return originalPattern.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int countY = 0; countY < pattern.length; countY++) {
			for (int countX = 0; countX < pattern[0].length; countX++) {
				if (pattern[countY][countX]) {
					sb.append("1 ");
				} else {
					sb.append("0 ");
				}
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		BinaryPattern ret = new BinaryPattern();
		ret.setOriginalPattern(originalPattern);
		ret.setPattern(pattern);
		return ret;
	}

}
