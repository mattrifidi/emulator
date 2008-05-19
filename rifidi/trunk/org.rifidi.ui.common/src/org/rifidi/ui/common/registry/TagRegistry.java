package org.rifidi.ui.common.registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.rifidi.services.tags.factory.TagFactory;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * Container for all tags
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagRegistry {

	// private Log logger = LogFactory.getLog(TagRegistry.class);

	/**
	 * Singleton pattern
	 */
	private static TagRegistry INSTANCE = new TagRegistry();

	/**
	 * list of all tags in this registry, the key in this hashmap is the ID of
	 * the Tag (use the toString() method of a tag)
	 */
	private HashMap<String, RifidiTag> tagRegistry = new HashMap<String, RifidiTag>();

	/**
	 * list of listners for events like add or remove tags
	 */
	private LinkedList<RegistryChangeListener> listeners = new LinkedList<RegistryChangeListener>();

	private TagRegistry() {
	}

	/**
	 * get a instance of the TagRegistry
	 * 
	 * @return the TagRegistry instance
	 */
	public static TagRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * get a complete list of all tags in the registry
	 * 
	 * @return a List of all tags in the registry
	 */
	public List<RifidiTag> getTagList() {
		return (List<RifidiTag>) new ArrayList<RifidiTag>(tagRegistry.values());
	}

	/**
	 * add a new list of Tags to the registry
	 * 
	 * @param tagList
	 */
	public void addTag(List<RifidiTag> tagList) {
		for (RifidiTag t : tagList) {
			tagRegistry.put(t.toString(), t);
		}
		addEvent(null);
	}

	/**
	 * remove Tag form the registry
	 * 
	 * @param tag
	 */
	public void removeTag(RifidiTag tag) {
		tagRegistry.remove(tag.hashCode());
		removeEvent(tag.toString());
	}

	/**
	 * Remove the given Tags form the registry
	 * 
	 * @param tagList
	 */
	public void removeTag(List<RifidiTag> tagList) {
		for (RifidiTag tag : tagList) {
			tagRegistry.remove(tag.toString());
		}
		removeEvent(null);
	}

	/**
	 * get a tag by it's ID (needed because of Drag&Drop support)
	 * 
	 * @param tagID
	 * @return
	 */
	public RifidiTag getTagByString(String tagID) {
		RifidiTag tag = tagRegistry.get(tagID);
		return TagFactory.copyTag(tag);
	}

	/**
	 * notifies listeners about an remove tag event
	 * 
	 * @param event
	 *            the Tag to be removed
	 */
	private void removeEvent(Object event) {
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

	/**
	 * notifies listeners about a add tag event
	 * 
	 * @param event
	 *            the Tag to be added
	 */
	private void addEvent(Object event) {
		for (RegistryChangeListener listener : listeners) {
			listener.add(event);
		}
	}

	/**
	 * remove a listener from the event listeners list
	 * 
	 * @param listener
	 */
	public void removeListener(RegistryChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * add a listener to the list of listeners
	 * 
	 * @param listener
	 */
	public void addListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

}
