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
 * 
 */
@XmlAccessorType( XmlAccessType.NONE )
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
	private ReaderModuleManagerInterface readerManager;

	/**
	 * This is the callback interface implementation it is used to get response
	 * from the reader without the need of polling it
	 */
	private UIReaderCallbackManager readerCallbackManager;

	/**
	 * UI representation of the Antenna Fields
	 */
	private HashMap<Integer, UIAntenna> antennas = new HashMap<Integer, UIAntenna>();

	public void start() {
		//TODO implment functionality
		readerState = "running";
		try {
			readerManager.turnReaderOn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop()
	{
		//TODO implment functionality
		readerState = "stopped";
		try {
			readerManager.turnReaderOff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void suspend()
	{
		//TODO implment functionality
		readerState = "suspended";
		try {
			readerManager.suspendReader();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resume()
	{
		//TODO implment functionality
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
		return readerType;
	}

	/**
	 * @param readerType
	 *            the readerType to set
	 */
	public void setReaderType(String readerType) {
		this.readerType = readerType;
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
	 * @param readerManager
	 *            the readerManager to set
	 */
	public void setReaderManager(ReaderModuleManagerInterface readerManager) {
		this.readerManager = readerManager;
	}

	/**
	 * @return the antennas
	 */
	public HashMap<Integer, UIAntenna> getAntennas() {
		return antennas;
	}

	/**
	 * @param antennas
	 *            the antennas to set
	 */
	public void setAntennas(HashMap<Integer, UIAntenna> antennas) {
		this.antennas = antennas;
	}

//	/**
//	 * @return a list of the antennas
//	 */
//	@XmlElement
//	public List<UIAntenna> getAntennaList() {
//		System.out.println(antennas.size());
//		return new ArrayList<UIAntenna>(antennas.values());
//	}
//
//	/**
//	 * @param antennalist the antenna list to set
//	 */
//	public void setAntennaList( List<UIAntenna> antennalist ) {
//		antennas.clear();
//		for ( UIAntenna ant : antennalist ) {
//			ant.setReader(this);
//			antennas.put(ant.getId(),ant);
//		}
//	}

	@Override
	@XmlElement
	public int getNumAntennas() {
		return super.getNumAntennas();
	}

	@Override
	public void setNumAntennas( int newnumantennas ) {
		super.setNumAntennas(newnumantennas);
		for (int i = 0; i < newnumantennas; i++) {
			new UIAntenna(this, i);
		}
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
	 * Set a the antenna with the id of the UIAntenna to this UIAntenna
	 * 
	 * @param antenna
	 *            the Antenna itself
	 */
	public void setAntenna(UIAntenna antenna) {
		antennas.put(antenna.getId(), antenna);
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
