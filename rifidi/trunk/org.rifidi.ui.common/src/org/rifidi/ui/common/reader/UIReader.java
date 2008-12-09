/*
 *  ReaderRegistry.java
 *
 *  Created:	Mar 2, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.reader;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.ui.common.reader.callback.UIReaderCallbackManager;

/**
 * 
 * The UI representation of a reader.
 * 
 * As this class extends the GeneralReaderPropertyHolder it can be used to
 * create the reader over RMI. (Use getGeneralPropertyReader())
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - Kyle@pramari.com
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UIReader extends GeneralReaderPropertyHolder {

	/**
	 * SerialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The state of the reader - can be running, stopped or suspended
	 */
	// FIXME Use enums, not this
	private String readerState = "NEW";

	/**
	 * This is the selection from the wizard Page
	 */
	@XmlElement
	private String readerType;

	/**
	 * This holds the reference to the real reader
	 */
	@XmlTransient
	private ReaderModuleManagerInterface readerManager;

	/**
	 * This is the callback interface implementation it is used to get response
	 * from the reader without the need of polling it
	 */
	@XmlTransient
	private UIReaderCallbackManager readerCallbackManager;

	/**
	 * UI representation of the Antenna Fields
	 */
	private HashMap<Integer, UIAntenna> antennas;

	public UIReader(ReaderModuleManagerInterface readerManager, GeneralReaderPropertyHolder grph){
		this.readerManager = readerManager;
		this.setNumAntennas( grph.getNumAntennas());
		this.setNumGPIs(grph.getNumGPIs());
		this.setNumGPOs(grph.getNumGPOs());
		this.setPropertiesMap(grph.getPropertiesMap());
		this.setReaderName(grph.getReaderName());
		this.setReaderClassName(grph.getReaderClassName());
		antennas = new HashMap<Integer, UIAntenna>();
		for(int i=0; i<getNumAntennas(); i++){
			antennas.put(i, new UIAntenna(readerManager,i));
		}
	}
	
	public void start() {
		readerState = "running";
		try {
			readerManager.turnReaderOn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		readerState = "stopped";
		try {
			readerManager.turnReaderOff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void suspend() {
		// TODO implment functionality
		readerState = "suspended";
		try {
			readerManager.suspendReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resume() {
		// TODO implment functionality
		readerState = "running";
		try {
			readerManager.resumeReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the readerType
	 */
	public String getReaderType() {
		return super.getReaderClassName();
	}

	/**
	 * @return the readerState
	 */
	public String getReaderState() {
		return readerState;
	}

	/**
	 * @param readerState
	 *            the readerState to set
	 */
	public void setReaderState(String readerState) {
		this.readerState = readerState;
	}

	/**
	 * @return the readerManager
	 */
	public ReaderModuleManagerInterface getReaderManager() {
		return readerManager;
	}

	/**
	 * @return the antennas
	 */
	public HashMap<Integer, UIAntenna> getAntennas() {
		return antennas;
	}

	/**
	 * Returns a the Antenna with this id.
	 * 
	 * @param id
	 *            of the Antenna
	 * @return UIAntenna if the antenna exist. Otherwise there will be
	 *         <code>null</code> returned.
	 */
	public UIAntenna getAntenna(Integer id) {
		return antennas.get(id);
	}


	/**
	 * GeneralPropertyHolder describing this Reader for creation in the Emulator
	 * 
	 * @return GeneralPropertyHolder of the reader
	 */
	public GeneralReaderPropertyHolder getGeneralReaderPropertyHolder() {
		GeneralReaderPropertyHolder grph = new GeneralReaderPropertyHolder();
		grph.setNumAntennas(this.getNumAntennas());
		grph.setNumGPIs(this.getNumGPIs());
		grph.setNumGPOs(this.getNumGPOs());
		grph.setReaderClassName(this.getReaderClassName());
		grph.setReaderName(this.getReaderName());
		grph.setPropertiesMap(this.getPropertiesMap());
		return grph;
	}

	/**
	 * @return the readerCallbackManager
	 */
	public UIReaderCallbackManager getReaderCallbackManager() {
		return readerCallbackManager;
	}

	/**
	 * @param readerCallbackManager
	 *            the readerCallbackManager to set
	 */
	public void setReaderCallbackManager(
			UIReaderCallbackManager readerCallbackManager) {
		this.readerCallbackManager = readerCallbackManager;
	}
}
