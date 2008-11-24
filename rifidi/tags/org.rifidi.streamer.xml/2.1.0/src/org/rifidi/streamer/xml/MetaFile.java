/*
 *  MetaFile.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The MetaFile is the container for all Collections of Components in the
 * Streamer. It's the root structure for the XML file in the filesystem
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class MetaFile {

	private ScenarioSuite scenarioSuite;
	private BatchSuite batchSuite;
	private ComponentSuite componentSuite;
	private TestUnitSuite testUnitSuite;

	/**
	 * @return the scenarioSuite
	 */
	public ScenarioSuite getScenarioSuite() {
		return scenarioSuite;
	}

	/**
	 * @param scenarioSuite
	 *            the scenarioSuite to set
	 */
	public void setScenarioSuite(ScenarioSuite scenarioSuite) {
		this.scenarioSuite = scenarioSuite;
	}

	/**
	 * @return the batchSuite
	 */
	public BatchSuite getBatchSuite() {
		return batchSuite;
	}

	/**
	 * @param batchSuite
	 *            the batchSuite to set
	 */
	public void setBatchSuite(BatchSuite batchSuite) {
		this.batchSuite = batchSuite;
	}

	/**
	 * @return the componentSuite
	 */
	public ComponentSuite getComponentSuite() {
		return componentSuite;
	}

	/**
	 * @param componentSuite
	 *            the componentSuite to set
	 */
	public void setComponentSuite(ComponentSuite componentSuite) {
		this.componentSuite = componentSuite;
	}

	/**
	 * @return TestUnitSuite
	 */
	public TestUnitSuite getTestUnitSuite() {
		return testUnitSuite;
	}

	/**
	 * @param testUnitSuite
	 */
	public void setTestUnitSuite(TestUnitSuite testUnitSuite) {
		this.testUnitSuite = testUnitSuite;
	}

}
