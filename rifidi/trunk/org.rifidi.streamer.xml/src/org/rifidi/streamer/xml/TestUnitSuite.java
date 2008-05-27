/*
 *  TestUnitSuite.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml;

import java.util.List;

import org.rifidi.streamer.xml.testSuite.TestUnit;

/**
 * The TestUnitSuite is a collection of all TestUnits in a MetaFile
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestUnitSuite {

	private List<TestUnit> testUnits;

	/**
	 * @return List of TestUnits
	 */
	public List<TestUnit> getTestUnits() {
		return testUnits;
	}

	/**
	 * @param testUnits
	 */
	public void setTestUnits(List<TestUnit> testUnits) {
		this.testUnits = testUnits;
	}

}
