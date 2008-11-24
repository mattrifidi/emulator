package org.rifidi.ui.common.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistryListener;

/**
 * This is a UI representation of an antenna associated to a reader. It's
 * responsible for the addTag and removeTag operations. It's aware of Events and
 * interested listeners can register through the registerListener method. Every
 * Listener must implement RegsitryChangeListener. There will be a null event
 * sent.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
public class UIAntenna {

	private Log logger = LogFactory.getLog(UIAntenna.class);

	private UIReader reader;
	private ReaderModuleManagerInterface readerManager;

	/**
	 * Number or Name of this Antenna
	 */
	@XmlElement
	private Integer id;

	/**
	 * This are the tags which are also in the list of the real antenna of the
	 * reader
	 */
	private Map<String, RifidiTag> tagList = new HashMap<String, RifidiTag>();

	/**
	 * This are a list of tags which are disabled and not on the real antenna of
	 * the reader
	 */
	private Map<String, RifidiTag> inactiveTags = new HashMap<String, RifidiTag>();
	/**
	 * The list of Listeners to events occurring in the antenna (like add tag or
	 * remove tag)
	 */
	private List<ITagRegistryListener> listeners = new LinkedList<ITagRegistryListener>();

	/**
	 * Default constructor (needed by jaxb)
	 */
	public UIAntenna() {
	}

	/**
	 * Default constructor (this one should be used)
	 * 
	 * @param reader
	 *            UIReader this Antenna belongs to
	 * @param id
	 *            Number (or Name) of the antenna
	 */
	public UIAntenna(UIReader reader, Integer id) {
		this.reader = reader;
		this.id = id;
		reader.setAntenna(this);
	}

	/**
	 * Get the reader to whom the antenna belongs to
	 * 
	 * @return the reader
	 */
	public UIReader getReader() {
		return reader;
	}

	/**
	 * Set the reader to whom the antenna belongs to
	 * 
	 * @param reader
	 *            the reader to set
	 */
	public void setReader(UIReader reader) {
		this.reader = reader;
	}

	/**
	 * Get the antenna id (Name of the Antenna)
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Set the antenna id (Name of Antenna)
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Get the tags in the field of the antenna. If there are any inactive Tags
	 * they will added to the return value.
	 * 
	 * @return the tagList
	 */
	public List<RifidiTag> getTagList() {
		HashMap<String, RifidiTag> tagsToReturn = new HashMap<String, RifidiTag>();
		if (readerManager == null) {
			getManager();
		}
		List<RifidiTag> tagsFromReader = null;
		try {
			tagsFromReader = readerManager.getTagList(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (tagsFromReader != null) {
			tagList = new HashMap<String, RifidiTag>();
			for (RifidiTag tag : tagsFromReader) {
				logger.debug("New Tag in the UIAntenna: " + tag.toString());
				tagList.put(tag.toString(), tag);
			}
		}
		tagsToReturn.putAll(tagList);
		if (!inactiveTags.isEmpty()) {
			tagsToReturn.putAll(inactiveTags);
		}
		return new ArrayList<RifidiTag>(tagsToReturn.values());
	}

	/**
	 * Add tags to the field of the antenna
	 * 
	 * @param tagsToAdd
	 */
	public void addTag(List<RifidiTag> tagsToAdd) {
		try {
			if (readerManager == null)
				getManager();
			readerManager.addTags(id, tagsToAdd);
			for (RifidiTag tag : tagsToAdd)
				tagList.put(tag.toString(), tag);
			addEvent();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove Tags from the field of the antenna
	 * 
	 * @param tagsToRemove
	 */
	public void removeTag(List<RifidiTag> tagsToRemove) {
		if (readerManager == null)
			getManager();
		ArrayList<Long> list = new ArrayList<Long>(tagsToRemove.size());
		for (RifidiTag tag : tagsToRemove) {
			list.add(tag.getTagEntitiyID());
		}
		try {
			readerManager.removeTags(id, list);
			for (RifidiTag tag : tagsToRemove)
				tagList.remove(tag.toString());
			logger.debug("TagList is now : " + tagList.size()
					+ " Event listeners : " + listeners.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		removeEvent();
	}

	/**
	 * Disable the given tag. Removes the tag from the Antenna and stores the
	 * tag in a separate List.
	 * 
	 * @param tag
	 *            RifidiTag to disable
	 */
	public void disableTag(List<RifidiTag> tagsToDisable) {
		for (RifidiTag tag : tagsToDisable) {
			logger.debug("Disableing tag with ID: " + tag.toString());
			tag.isVisbile = false;
			inactiveTags.put(tag.toString(), tag);
		}
		removeTag(tagsToDisable);
	}

	/**
	 * Enables the previous disabled tag and adds it back to the Antenna of the
	 * reader and deletes it from the inactiveTag List.
	 * 
	 * @param tag
	 *            RifidiTag to enable
	 */
	public void enableTag(List<RifidiTag> tagsToEnable) {
		for (RifidiTag tag : tagsToEnable) {
			logger.debug("Enabling tag with ID: " + tag.toString());
			tag.isVisbile = true;
			inactiveTags.remove(tag.toString());
		}
		addTag(tagsToEnable);
	}

	/**
	 * Add a listener to this antenna
	 * 
	 * @param listener
	 */
	public void addListener(ITagRegistryListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove a listener from this antenna
	 * 
	 * @param listener
	 */
	public void removeListener(ITagRegistryListener listener) {
		listeners.remove(listener);
	}

	/**
	 * AddEvent occurred inform listeners
	 */
	private void addEvent() {
		// TODO Event must be set
		for (ITagRegistryListener r : listeners) {
			r.addEvent(null);
		}
	}

	/**
	 * RemoveEvent occurred inform listeners
	 */
	private void removeEvent() {
		// TODO Event must be set
		for (ITagRegistryListener r : listeners) {
			r.removeEvent(null);
		}
	}

	/**
	 * UpdateEvent occurred inform listeners
	 */
	// TODO Never used just there for compatibility issues
	@SuppressWarnings("unused")
	private void updateEvent() {
		// TODO Event must be set
		for (ITagRegistryListener r : listeners) {
			r.modifyEvent(null);
		}
	}

	/**
	 * This gets the Manager out of the UIReader after the UIAntenna was created
	 */
	private void getManager() {
		readerManager = reader.getReaderManager();
	}
}
