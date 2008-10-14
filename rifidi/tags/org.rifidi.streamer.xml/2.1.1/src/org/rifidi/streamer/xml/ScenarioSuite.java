/*
 *  ScenarioSuite.java
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

import org.rifidi.streamer.xml.scenario.Scenario;

/**
 * The ScenarioSuite is the collection of all Scenarios in a MetaFile
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlRootElement
public class ScenarioSuite {

	/**
	 * List of scenarios in this suite
	 */
	private List<Scenario> scenarios;

	/**
	 * @return the scenarios in this suite
	 */
	public List<Scenario> getScenarios() {
		return scenarios;
	}

	/**
	 * @param scenarios
	 *            the scenarios to set
	 */
	@XmlElement(name = "scenario")
	public void setScenarios(List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

}
