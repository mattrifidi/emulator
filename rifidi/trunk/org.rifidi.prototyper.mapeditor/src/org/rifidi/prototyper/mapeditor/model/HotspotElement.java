/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.prototyper.mapeditor.Activator;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotElement extends AbstractMapModelElement implements
		PropertyChangeListener {

	static final long serialVersionUID = 1;
	private Float antennaRage;
	private Point location;
	private GeneralReaderPropertyHolder readerModel;
	private transient UIReader readerController;
	private Integer antennaID;
	private String logicalName;
	private String readerState;
	private transient boolean hasBeenInitialized;
	private Dimension size;

	public HotspotElement(GeneralReaderPropertyHolder reader, Integer antennaID) {
		readerState = UIReader.STATE_NEW;
		this.readerModel = reader;
		this.antennaID = antennaID;
		this.antennaRage = new Float(30);
		this.size = new Dimension(getImage().getBounds().width, getImage()
				.getBounds().height);
		init();
	}

	/**
	 * Method that is called to do custom serialization when resurecting objects
	 * from persistance
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	/**
	 * Helper method to initialize this object either from persistance or from a
	 * constructor
	 */
	private void init() {
		hasBeenInitialized = false;
	}

	/**
	 * The initialize method must be called before this HotspotElement can be
	 * used.
	 * 
	 * @param readerService
	 */
	public void initialize(ReaderRegistryService readerService) {
		if (readerController == null) {
			readerController = readerService.getReaderByName(readerModel
					.getReaderName());
			if (readerController == null) {
				throw new IllegalStateException("Reader is null");
			}
			readerController.addPropertyChangeListener(this);
			if (UIReader.STATE_NEW.equals(readerState)) {
				readerController.stop();
			} else if (UIReader.STATE_RUNNING.equals(readerState)) {
				readerController.start();
			} else if (UIReader.STATE_STOPPED.equals(readerState)) {
				readerController.stop();
			} else if (UIReader.STATE_SUSPENDED.equals(readerState)) {
				readerController.suspend();
			}
			hasBeenInitialized = true;
		}
	}

	public boolean hasBeenInitialized() {
		return hasBeenInitialized;
	}

	/**
	 * @return the antennaRage
	 */
	public Float getAntennaRage() {
		return antennaRage;
	}

	/**
	 * @return the size
	 */
	public Dimension getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(Dimension size) {
		this.size = size;
		firePropertyMoved(this);
	}

	/**
	 * @param antennaRage
	 *            the antennaRage to set
	 */
	public void setAntennaRage(Float antennaRage) {
		this.antennaRage = antennaRage;
	}

	/**
	 * @return the location
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            the location to set
	 */
	public void setLocation(Point location) {
		this.location = location;
		firePropertyMoved(this);
	}

	/**
	 * The name of the reader
	 * 
	 * @return
	 */
	public String getReaderName() {
		return this.readerModel.getReaderName();
	}

	/**
	 * @return the antennaID
	 */
	public Integer getAntennaID() {
		return antennaID;
	}

	/**
	 * A logical name that is associated with this antenna-reader pair.
	 * 
	 * @return the logicalName
	 */
	public String getLogicalName() {
		return logicalName;
	}

	/**
	 * @param logicalName
	 *            the logicalName to set
	 */
	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public Image getImage() {
		if (readerState.equals(UIReader.STATE_RUNNING)) {
			return (Activator.getDefault().getImageRegistry()
					.get(Activator.IMG_ANT_ON));
		} else {
			return (Activator.getDefault().getImageRegistry()
					.get(Activator.IMG_ANT_OFF));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null && arg0 instanceof HotspotElement) {
			HotspotElement that = (HotspotElement) arg0;
			if (this.getReaderName().equals(that.getReaderName())) {
				if (this.antennaID.equals(that.antennaID)) {
					return true;
				}

			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(UIReader.PROP_STATE)) {
			readerState = readerController.getReaderState();
			firePropertyUpdate(this);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement#dispose()
	 */
	@Override
	public void dispose() {
		this.readerController.removePropertyChangeListener(this);
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Hotspot Element: ( " + getReaderName() + ":" + antennaID + " )";
	}

	/**
	 * This method should be called when a new tag arrives at this hotspot
	 * 
	 * @param tag
	 */
	public void handleTagSeen(RifidiTag tag) {
		if (!hasBeenInitialized) {
			throw new IllegalStateException("Hotspot has not been initialized.");
		}
		List<RifidiTag> tags = new ArrayList<RifidiTag>();
		tags.add(tag);
		this.readerController.getAntenna(antennaID).addTag(tags);
	}

	/**
	 * This method should be called when a tag currently on the reader is
	 * removed from the hotspot
	 * 
	 * @param tag
	 */
	public void handleTagUnseen(RifidiTag tag) {
		if (!hasBeenInitialized) {
			throw new IllegalStateException("Hotspot has not been initialized.");
		}
		List<RifidiTag> tags = new ArrayList<RifidiTag>();
		tags.add(tag);
		this.readerController.getAntenna(antennaID).removeTag(tags);
	}

}
