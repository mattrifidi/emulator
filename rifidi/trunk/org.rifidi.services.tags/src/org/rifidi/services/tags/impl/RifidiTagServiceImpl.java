/*
 *  RifidiTagServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.tags.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.RifidiTagServiceChangeListener;
import org.rifidi.services.tags.exceptions.RifidiTagNotAvailableException;
import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.factory.TagCreationPattern;
import org.rifidi.tags.factory.TagFactory;
import org.rifidi.tags.impl.RifidiTag;

/**
 * A threadsafe implementation of IRifidiTagService.
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 18, 2008
 * 
 */
public class RifidiTagServiceImpl implements IRifidiTagService {
	/** Logger for this class */
	private static final Log logger = LogFactory
			.getLog(RifidiTagServiceImpl.class);
	/** Map containing the tag id as key and the RifidiTag as value. */
	private Map<Long, RifidiTag> tagMap;
	/** List of available tags. */
	private List<RifidiTag> availableTags;
	/** Counter for tag id. */
	private Long idCounter;
	/** Used to block other threads from initializing */
	private AtomicBoolean writing;
	/** Set containing the registered tag containers. */
	private Set<IRifidiTagContainer> containers;
	/** Set of listeners for events. */
	private Set<RifidiTagServiceChangeListener> listeners;

	/**
	 * Constructor.
	 */
	public RifidiTagServiceImpl() {
		tagMap = new ConcurrentHashMap<Long, RifidiTag>();
		availableTags = new ArrayList<RifidiTag>();
		idCounter = 0l;
		writing = new AtomicBoolean(false);
		containers = new CopyOnWriteArraySet<IRifidiTagContainer>();
		listeners = new CopyOnWriteArraySet<RifidiTagServiceChangeListener>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.IRifidiTagService#clear()
	 */
	@Override
	public void clear() {
		while (!writing.compareAndSet(false, true)) {
		}
		try {
			listeners.clear();
			tagMap.clear();
			idCounter = 0l;
		} finally {
			writing.compareAndSet(true, false);
		}
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#createTags(org.rifidi.tags
	 * .factory.TagCreationPattern)
	 */
	@Override
	public ArrayList<RifidiTag> createTags(TagCreationPattern tagCreationPattern) {
		while (!writing.compareAndSet(false, true)) {
		}
		try {
			ArrayList<RifidiTag> newTags = TagFactory
					.generateTags(tagCreationPattern);
			for (RifidiTag t : newTags) {
				t.setTagEntitiyID(idCounter++);
				tagMap.put(t.getTagEntitiyID(), t);
				availableTags.add(t);
			}
			update();
			return newTags;
		} finally {
			writing.compareAndSet(true, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.IRifidiTagService#getAvailableTags()
	 */
	@Override
	public List<RifidiTag> getAvailableTags() {
		return new ArrayList<RifidiTag>(availableTags);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.IRifidiTagService#getRegisteredTags()
	 */
	@Override
	public List<RifidiTag> getRegisteredTags() {
		return new ArrayList<RifidiTag>(tagMap.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.tags.IRifidiTagService#getTag(java.lang.Long)
	 */
	@Override
	public RifidiTag getTag(Long tagEntityID) {
		return tagMap.get(tagEntityID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#registerTags(java.util.List)
	 */
	@Override
	public void registerTags(List<RifidiTag> tags) {
		while (!writing.compareAndSet(false, true)) {
		}
		try {
			for (RifidiTag tag : tags) {
				tag.setTagEntitiyID(idCounter++);
				tagMap.put(tag.getTagEntitiyID(), tag);
			}
			for (IRifidiTagContainer container : containers) {
				tags.removeAll(container.getTags());
			}
			availableTags.addAll(tags);
		} finally {
			writing.compareAndSet(true, false);
		}
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#releaseRifidiTag(org.rifidi
	 * .tags.impl.RifidiTag, org.rifidi.services.tags.model.IRifidiTagContainer)
	 */
	@Override
	public void releaseRifidiTag(RifidiTag tag, IRifidiTagContainer taker) {
		while (!writing.compareAndSet(false, true)) {
		}
		try {
			if (tagMap.values().contains(tag)) {
				availableTags.add(tag);
			}
		} finally {
			writing.compareAndSet(true, false);
		}
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#takeRifidiTag(org.rifidi.tags
	 * .impl.RifidiTag, org.rifidi.services.tags.model.IRifidiTagContainer)
	 */
	@Override
	public void takeRifidiTag(RifidiTag tag, IRifidiTagContainer taker)
			throws RifidiTagNotAvailableException {
		while (!writing.compareAndSet(false, true)) {
		}
		try {
			availableTags.remove(tag);
		} finally {
			writing.compareAndSet(true, false);
		}
		update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#registerTagContainer(org.rifidi
	 * .services.tags.model.ITagContainer)
	 */
	@Override
	public void registerTagContainer(IRifidiTagContainer rifidiTagContainer) {
		containers.add(rifidiTagContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#unregisterTagContainer(org
	 * .rifidi.services.tags.model.ITagContainer)
	 */
	@Override
	public void unregisterTagContainer(IRifidiTagContainer rifidiTagContainer) {
		containers.remove(rifidiTagContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.IRifidiTagService#addRifidiTagServiceChangeListener
	 * (org.rifidi.services.tags.RifidiTagServiceChangeListener)
	 */
	@Override
	public void addRifidiTagServiceChangeListener(
			RifidiTagServiceChangeListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.services.tags.IRifidiTagService#
	 * removeRifidiTagServiceChangeListener
	 * (org.rifidi.services.tags.RifidiTagServiceChangeListener)
	 */
	@Override
	public void removeRifidiTagServiceChangeListener(
			RifidiTagServiceChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Inform all listeners about updates.
	 */
	protected void update() {
		for (RifidiTagServiceChangeListener listener : listeners) {
			try {
				listener.tagsChanged();
			} catch (Exception e) {
				logger.fatal("Listener (" + listener + ") threw: " + e);
			}
		}
	}

}
