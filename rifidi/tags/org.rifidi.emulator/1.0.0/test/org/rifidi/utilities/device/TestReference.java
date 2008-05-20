/*
 *  TestReference.java
 *
 *  Created:	Mar 10, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.device;

import java.util.ArrayList;

/**
 *
 *
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class TestReference {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Long> newLong = new ArrayList<Long>();
		newLong.set(0, newLong.get(0)+1);
		for(int i=0;i<5;i++) {
			newLong.add(new Long(0l));
		}
		
		for(Long i:newLong) {
			init(i);
			System.out.println(newLong);
		}

		
	}
	
	public static void init(Long l) {
		System.out.println("1: "+l.hashCode());
		l=l+3;
		System.out.println("2: "+l.hashCode());
	}

}
