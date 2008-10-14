/*
 *  LoadTestSuite.java
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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.streamer.xml.testSuite.FileUnit;
import org.rifidi.streamer.xml.testSuite.TestUnit;

/**
 * This one is deprecated. Please use MetaFile instead.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
@Deprecated
public class LoadTestSuite {

	/**
	 * list of testUnits in this suite
	 */
	private List<TestUnit> testUnits;

	/**
	 * list of fileUnits in this suite
	 */
	private List<FileUnit> fileUnits;

	/**
	 * @return the testUnits
	 */
	public List<TestUnit> getTestUnits() {
		return testUnits;
	}

	/**
	 * @param testUnits
	 *            the testUnits to set
	 */
	@XmlElement(name = "testUnit")
	public void setTestUnits(List<TestUnit> testUnits) {
		this.testUnits = testUnits;
	}

	/**
	 * @return the fileUnits
	 */
	public List<FileUnit> getFileUnits() {
		return fileUnits;
	}

	/**
	 * @param fileUnits
	 *            the fileUnits to set
	 */
	@XmlElement(name = "fileUnit")
	public void setFileUnits(List<FileUnit> fileUnits) {
		this.fileUnits = fileUnits;
	}

}
