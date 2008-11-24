/*
 *  BatchSuite.java
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

import org.rifidi.streamer.xml.batch.Batch;

/**
 * BatchSuite is a collection of all Batches in a MetaFile
 * 
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
@XmlRootElement
public class BatchSuite {
	
	/**
	 * List of Batches in this suite
	 */
	private List<Batch> batches;

	/**
	 * @return the batches in this suite
	 */
	public List<Batch> getBatches() {
		return batches;
	}

	/**
	 * @param batches the batches to set
	 */
	@XmlElement(name="batch")
	public void setBatches(List<Batch> batches) {
		this.batches = batches;
	}
	
	

}
