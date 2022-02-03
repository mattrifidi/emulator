package org.rifidi.ui.common.reader;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.manager.ReaderManager;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is a UI representation of an antenna associated to a reader. It's
 * responsible for the addTag and removeTag operations. It's aware of Events and
 * interested listeners can register through the registerListener method. Every
 * Listener must implement RegsitryChangeListener. There will be a null event
 * sent.
 * 
 * This class extends Observable so that listeners can be notified of when tags
 * are added or removed. When tags are added, the ADD_TAG_EVENT is passed as an
 * argument to update. When tags are removed, REMOVE_TAG_EVENT is passed in.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - Kyle@pramari.com
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UIAntenna extends Observable implements PropertyChangeListener,
		Serializable {

	/***/
	private static final long serialVersionUID = 1L;

	/**
	 * The event that happens when a tag is added
	 */
	public static final String ADD_TAG_EVENT = "add";

	/**
	 * The event that happens when a tag is removed
	 */
	public static final String REMOVE_TAG_EVENT = "remove";

	private transient Log logger = LogFactory.getLog(UIAntenna.class);

	private transient ReaderManager readerManager;

	/** Number or Name of this Antenna */
	@XmlElement
	private Integer id;

	/** Reader ID of the reader this antenna is on */
	@XmlElement
	private String readerID;

	/**
	 * This are the tags which are also in the list of the real antenna of the
	 * reader
	 */
	private transient Set<Long> tagList = new HashSet<Long>();

	/**
	 * This are a list of tags which are disabled and not on the real antenna of
	 * the reader
	 */
	private transient Set<Long> inactiveTags = new HashSet<Long>();

	/** TagRegistrySerbive */
	private transient ITagRegistry tagRegistry;

	/**
	 * Default constructor (needed by jaxb)
	 */
	public UIAntenna() {
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * Default constructor (this one should be used)
	 * 
	 * @param reader
	 *            UIReader this Antenna belongs to
	 * @param id
	 *            Number (or Name) of the antenna
	 */
	public UIAntenna(ReaderManager readerManager, String readerID, Integer id) {
		this.id = id;
		this.readerID = readerID;
		this.readerManager = readerManager;
		ServiceRegistry.getInstance().service(this);
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

	public String getReaderID() {
		return readerID;
	}

	public void setReaderID(String readerID) {
		this.readerID = readerID;
	}

	/**
	 * Get the tags in the field of the antenna. If there are any inactive Tags
	 * they will added to the return value.
	 * 
	 * @return the tagList
	 */
	public List<RifidiTag> getTagList() {
		List<RifidiTag> tagsToReturn = new ArrayList<RifidiTag>();
		List<RifidiTag> tagsFromReader = null;

		tagsFromReader = readerManager.getTagList(readerID, id);

		if (tagsFromReader != null) {
			tagList = new HashSet<Long>();
			for (RifidiTag tag : tagsFromReader) {
				logger.debug("New Tag in the UIAntenna: " + tag.toString());
				tagList.add(tag.getTagEntitiyID());
			}
		}
		for (Long tagID : tagList) {
			tagsToReturn.add(tagRegistry.getTag(tagID));
		}

		for (Long tagID : inactiveTags) {
			tagsToReturn.add(tagRegistry.getTag(tagID));
		}

		return tagsToReturn;
	}

	/**
	 * Add tags to the field of the antenna
	 * 
	 * @param tagsToAdd
	 */
	public void addTag(List<RifidiTag> tagsToAdd) {
		readerManager.addTags(readerID, id, new HashSet<RifidiTag>(tagsToAdd));
		for (RifidiTag tag : tagsToAdd)
			tagList.add(tag.getTagEntitiyID());
		super.setChanged();
		notifyObservers(ADD_TAG_EVENT);
	}

	/**
	 * Add tags to the field of the antenna
	 * 
	 * @param tagsToAdd
	 */
	public void addTagsByID(List<Long> tagsToAdd) {

		ArrayList<RifidiTag> tags = new ArrayList<RifidiTag>();
		for (Long id : tagsToAdd) {
			tags.add(tagRegistry.getTag(id));
			tagList.add(id);
		}

		readerManager.addTags(readerID, id, new HashSet<RifidiTag>(tags));
		super.setChanged();
		notifyObservers(ADD_TAG_EVENT);

	}

	/**
	 * Remove Tags from the field of the antenna
	 * 
	 * @param tagsToRemove
	 */
	private void removeTagHelper(List<RifidiTag> tagsToRemove) {
		readerManager.removeTags(readerID, id, new HashSet<RifidiTag>(
				tagsToRemove));
		super.setChanged();
		notifyObservers(REMOVE_TAG_EVENT);
	}

	/**
	 * Remove Tags from the field of the antenna
	 * 
	 * @param tagsToRemove
	 */
	public void removeTag(List<RifidiTag> tagsToRemove) {
		List<RifidiTag> tags = new ArrayList<RifidiTag>(tagsToRemove);

		for (RifidiTag tag : tagsToRemove) {
			if (inactiveTags.remove(tag.getTagEntitiyID()))
				tags.remove(tag);
		}
		removeTagHelper(tags);
	}

	/**
	 * Remove Tags from the field of the antenna
	 * 
	 * @param tagsToRemove
	 */
	@Deprecated
	public void removeTagByID(List<Long> tagsToRemove) {
		Set<RifidiTag> rifidiTagsToRemove = new HashSet<RifidiTag>();
		for (Long tag : tagsToRemove) {
			inactiveTags.remove(tag);
			rifidiTagsToRemove.add(tagRegistry.getTag(tag));
		}

		readerManager.removeTags(readerID, id, rifidiTagsToRemove);

		super.setChanged();
		notifyObservers(REMOVE_TAG_EVENT);
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
			inactiveTags.add(tag.getTagEntitiyID());
		}
		removeTagHelper(tagsToDisable);
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
			inactiveTags.remove(tag.getTagEntitiyID());
		}
		addTag(tagsToEnable);
	}

	@Inject
	public void setTagRegistry(ITagRegistry tagRegistry) {
		this.tagRegistry = tagRegistry;
		this.tagRegistry.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// check to see if tag was deleted
		if (arg0.getPropertyName() == "tags") {
			List<RifidiTag> newList = (List<RifidiTag>) arg0.getNewValue();
			ArrayList<Long> newIDList = new ArrayList<Long>();
			for (RifidiTag t : newList) {
				newIDList.add(t.getTagEntitiyID());
			}
			ArrayList<Long> thisIDList = new ArrayList<Long>(this.tagList);
			thisIDList.addAll(this.inactiveTags);
			thisIDList.removeAll(newIDList);
			if (thisIDList.size() != 0) {
				this.removeTagByID(thisIDList);
			}

		}
	}
}
