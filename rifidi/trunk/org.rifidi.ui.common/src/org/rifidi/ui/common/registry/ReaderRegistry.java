/**
 * 
 */
package org.rifidi.ui.common.registry;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.log.ReaderLogService;
import org.rifidi.emulator.manager.ReaderManager;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.DigestReaders;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;
import org.rifidi.ui.common.reader.callback.UIReaderCallbackManager;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;
import org.xml.sax.SAXException;

/**
 * The ReaderRegistry is the main part for holding the UIReaders and generating
 * them. It's also a providing events whenever a reader was created or removed.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - Kyle Neumeier
 * 
 */
public class ReaderRegistry implements ReaderRegistryService {

	private static Log logger = LogFactory.getLog(ReaderRegistry.class);
	/** The reader manager */
	private ReaderManager readerManager;
	/** List of readers in the registy */
	private HashMap<String, UIReader> readerRegistry = new HashMap<String, UIReader>();
	/**
	 * Service that keeps up with output to be displayed on the console from
	 * readers
	 */
	private ReaderLogService readerLogService;
	/**
	 * reader blueprints describing the reader out of the reader xml definitions
	 */
	private HashMap<String, ReaderBlueprint> readerBlueprints = new HashMap<String, ReaderBlueprint>();
	/**
	 * List of listners for events like add reader or remove reader (there is
	 * also a event update)
	 */
	private LinkedList<RegistryChangeListener> listeners = new LinkedList<RegistryChangeListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#setReaderManager(
	 * org.rifidi.emulator.scripting.ReaderManager)
	 */
	public void setReaderManager(ReaderManager readerManager) {
		this.readerManager = readerManager;
		readerManager.getReaderTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#setReaderLogService
	 * (org.rifidi.emulator.log.ReaderLogService)
	 */
	public void setReaderLogService(ReaderLogService readerLogService) {
		this.readerLogService = readerLogService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#connect(java.lang
	 * .String, int)
	 */
	@Deprecated
	public void connect(String hostname, int port) throws ConnectException {
		logger.info("Connect is depricated");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#create(org.rifidi
	 * .emulator.reader.module.GeneralReaderPropertyHolder)
	 */
	public void create(GeneralReaderPropertyHolder grph)
			throws DuplicateReaderException {
		if (grph == null) {
			throw new IllegalArgumentException();
		}
		String readerName = grph.getReaderName();

		if (readerRegistry.containsKey(readerName)) {

			throw new DuplicateReaderException("The reader with the name "
					+ readerName + " already exists");
		}
		try {
			if (readerManager.createReader(grph) != null) {
				UIReader reader = new UIReader(readerManager, grph);
				UIReaderCallbackManager readerCallbackManager = new UIReaderCallbackManager();
				reader.setReaderCallbackManager(readerCallbackManager);
				readerRegistry.put(readerName, reader);
				addEvent(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * add event happends whenever a new reader is created
	 * 
	 * @param event
	 *            the UIReader added to the registry
	 */
	private void addEvent(UIReader event) {
		for (RegistryChangeListener listener : listeners) {
			listener.add(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#remove(org.rifidi
	 * .ui.common.reader.UIReader)
	 */
	public void remove(UIReader reader) {
		readerManager.deleteReader(reader.getReaderName());
		readerRegistry.remove(reader.getReaderName());
		removeEvent(reader);
	}

	/**
	 * remove event happends every time a reader was removed
	 * 
	 * @param event
	 *            the UIReader to be removed
	 */
	private void removeEvent(UIReader event) {
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#addListener(org.rifidi
	 * .ui.common.registry.RegistryChangeListener)
	 */
	public void addListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#update(org.rifidi
	 * .ui.common.reader.UIReader)
	 */
	public void update(UIReader event) {
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#removelListener(org
	 * .rifidi.ui.common.registry.RegistryChangeListener)
	 */
	public void removelListener(RegistryChangeListener listener) {
		listeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.common.registry.ReaderRegistryService#clean()
	 */
	public void clean() {
		for (UIReader reader : readerRegistry.values()) {
			try {
				readerManager.deleteReader(reader.getReaderName());
			} catch (Exception e) {

			}
		}
		readerRegistry.clear();
		readerBlueprints.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#getReaderBlueprints()
	 */
	public Map<String, ReaderBlueprint> getReaderBlueprints() {

		for (String currReader : readerManager.getReaderTypes()) {
			if (!readerBlueprints.containsKey(currReader)) {
				DigestReaders diggy = new DigestReaders();
				try {

					byte currentXMLBytes[];

					currentXMLBytes = readerManager.getXMLDescription(
							currReader).getBytes();

					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							currentXMLBytes);
					ReaderBlueprint rbp = diggy.digest(byteArrayInputStream);
					readerBlueprints.put(currReader, rbp);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return new HashMap<String, ReaderBlueprint>(readerBlueprints);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.common.registry.ReaderRegistryService#getReaderList()
	 */
	public List<UIReader> getReaderList() {
		return new ArrayList<UIReader>(readerRegistry.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#getReaderByName(java
	 * .lang.String)
	 */
	public UIReader getReaderByName(String readerName) {
		return readerRegistry.get(readerName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#isNameAvailable(java
	 * .lang.String)
	 */
	public boolean isNameAvailable(String readerName) {
		return !readerRegistry.containsKey(readerName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.ui.common.registry.ReaderRegistryService#getCachedLogs(java
	 * .lang.String)
	 */
	public List<String> getCachedLogs(String readerName) {

		try {
			List<String> cache = new ArrayList<String>(readerLogService
					.getCache(readerName));
			return cache;
		} catch (Exception e) {
			logger.error("Problem when logging", e);
			return new ArrayList<String>();
		}
	}

}
