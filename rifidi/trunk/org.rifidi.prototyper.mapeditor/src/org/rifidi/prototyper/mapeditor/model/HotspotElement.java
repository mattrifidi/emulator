/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.prototyper.mapeditor.Activator;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * This class represents a Hotspot - which is a read zone on the map. There is
 * one antenna associated to each hotspot.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotElement extends AbstractMapModelElement implements
		PropertyChangeListener, Container<ItemElement> {

	/** The serial version ID */
	static final long serialVersionUID = 1;
	/** The antenna range */
	private Float antennaRage;
	/** The location of this hotspot */
	private Point location;
	/** The general reader property holder for the emulated reader */
	private GeneralReaderPropertyHolder readerModel;
	/** The UI Reader object */
	private transient UIReader readerController;
	/** The antenna ID this hotpsot is associated with */
	private Integer antennaID;
	/** The logical name of this hotspot */
	private String logicalName;
	/** The state of the reader */
	private String readerState;
	/** True if this object has been initialized */
	private transient boolean hasBeenInitialized;
	/** The size of this hotspot */
	private Dimension size;
	/** Any items currently in this hotspot */
	private Set<ItemElement> containedItems;

	/***
	 * Constructor
	 * 
	 * @param reader
	 * @param antennaID
	 */
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
		if (containedItems == null) {
			containedItems = new HashSet<ItemElement>();
		}
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

	/**
	 * Return true if this element has been initialized
	 * 
	 * @return
	 */
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

	/**
	 * Get the image depending on the state of the reader
	 * 
	 * @return
	 */
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
	public void handleTagSeen(ItemElement item) {
		if (!hasBeenInitialized) {
			throw new IllegalStateException("Hotspot has not been initialized.");
		}
		this.containedItems.add(item);
		List<RifidiTag> tags = new ArrayList<RifidiTag>(item.getVisibleTags());
		this.readerController.getAntenna(antennaID).addTag(tags);
	}

	/**
	 * This method should be called when a tag currently on the reader is
	 * removed from the hotspot
	 * 
	 * @param tag
	 */
	public void handleTagUnseen(ItemElement item) {
		if (!hasBeenInitialized) {
			throw new IllegalStateException("Hotspot has not been initialized.");
		}
		this.containedItems.remove(item);
		List<RifidiTag> tags = new ArrayList<RifidiTag>(item.getVisibleTags());
		this.readerController.getAntenna(antennaID).removeTag(tags);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.prototyper.mapeditor.model.Container#contains(org.rifidi.
	 * prototyper.mapeditor.model.AbstractMapModelElement)
	 */
	@Override
	public boolean contains(ItemElement element) {
		for (ItemElement item : containedItems) {
			if (item.contains(element)) {
				return true;
			}
		}
		return false;
	}
}
