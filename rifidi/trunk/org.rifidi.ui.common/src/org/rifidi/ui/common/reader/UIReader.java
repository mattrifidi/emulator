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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.emulator.manager.ReaderManager;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
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

	public static final String STATE_NEW = "NEW", STATE_RUNNING = "running",
			STATE_STOPPED = "stopped", STATE_SUSPENDED = "suspended",
			PROP_STATE = "readerstate";

	/** SerialVersionUID */
	private static final long serialVersionUID = 1L;

	/** The state of the reader - can be running, stopped or suspended */
	// FIXME Use enums, not this
	private String readerState = "NEW";

	@XmlTransient
	private transient PropertyChangeSupport pcs = new PropertyChangeSupport(
			this);

	/** This is the selection from the wizard Page */
	@XmlElement
	private String readerType;

	/** This holds the reference to the real reader */
	@XmlTransient
	private transient ReaderManager readerManager;

	/**
	 * This is the callback interface implementation it is used to get response
	 * from the reader without the need of polling it
	 */
	@XmlTransient
	private transient UIReaderCallbackManager readerCallbackManager;

	/** UI representation of the Antenna Fields */
	private transient HashMap<Integer, UIAntenna> antennas;

	/**
	 * Constructor.
	 * 
	 * @param readerManager
	 * @param grph
	 */
	public UIReader(ReaderManager readerManager,
			GeneralReaderPropertyHolder grph) {
		pcs = new PropertyChangeSupport(this);
		this.readerManager = readerManager;
		this.setNumAntennas(grph.getNumAntennas());
		this.setNumGPIs(grph.getNumGPIs());
		this.setNumGPOs(grph.getNumGPOs());
		this.setPropertiesMap(grph.getPropertiesMap());
		this.setReaderName(grph.getReaderName());
		this.setReaderClassName(grph.getReaderClassName());
		antennas = new HashMap<Integer, UIAntenna>();
		for (int i = 0; i < getNumAntennas(); i++) {
			antennas.put(i, new UIAntenna(readerManager, grph.getReaderName(),
					i));
		}
	}

	public void start() {
		readerManager.start(super.getReaderName());
		String oldstate = readerState;
		readerState = STATE_RUNNING;
		pcs.firePropertyChange(PROP_STATE, oldstate, readerState);

	}

	public void stop() {
		readerManager.stop(super.getReaderName());
		String oldstate = readerState;
		readerState = STATE_STOPPED;
		pcs.firePropertyChange(PROP_STATE, oldstate, readerState);

	}

	public void suspend() {
		readerManager.suspend(super.getReaderName());
		String oldstate = readerState;
		readerState = STATE_SUSPENDED;
		pcs.firePropertyChange(PROP_STATE, oldstate, readerState);

	}

	public void resume() {
		readerManager.resume(super.getReaderName());
		String oldstate = readerState;
		readerState = STATE_RUNNING;
		pcs.firePropertyChange(PROP_STATE, oldstate, readerState);

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
		readerManager.setReaderCallback(this.getReaderName(),
				readerCallbackManager);
	}

	public Collection<String> getAllCategories() {
		return readerManager.getAllCategories(this.getReaderName());
	}

	public Map<String, String> getCommandActionTagsByCategory(String category) {
		return readerManager.getCommandActionTagsByCategory(this
				.getReaderName(), category);
	}

	public String getReaderProperty(String propertyName) {
		return readerManager.getReaderProperty(this.getReaderName(),
				propertyName);
	}

	public Boolean setReaderProperty(String propertyName, String propertyValue) {
		return readerManager.setReaderProperty(this.getReaderName(),
				propertyName, propertyValue);
	}

	public List<Integer> getGPIList() {
		return readerManager.getGPIList(this.getReaderName());
	}

	public List<Integer> getGPOList() {
		return readerManager.getGPOList(this.getReaderName());
	}

	public void setGPIHigh(int port) {
		readerManager.setGPIPortHigh(getReaderName(), port);
	}

	public void setGPILow(int port) {
		readerManager.setGPIPortLow(getReaderName(), port);
	}

	public Boolean testGPO(int port) {
		return readerManager.testGPOPort(getReaderName(), port);
	}

	/**
	 * Add a state change listener
	 * 
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Remove a state change listener
	 * 
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
