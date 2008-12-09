/**
 * 
 */
package org.rifidi.ui.common.registry;

import gnu.cajo.utils.extra.TransparentItemProxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.emulator.rmi.server.RifidiManager;
import org.rifidi.emulator.rmi.server.RifidiManagerInterface;
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
public class ReaderRegistry {

	private static Log logger = LogFactory.getLog(ReaderRegistry.class);

	/**
	 * Singleton Pattern
	 */
	private static ReaderRegistry INSTANCE = new ReaderRegistry();

	/**
	 * List of readers in the registy
	 */
	private HashMap<String, UIReader> readerRegistry = new HashMap<String, UIReader>();

	/**
	 * remote RMI instance of the emulator
	 */
	private RifidiManagerInterface rifidiManager;

	/**
	 * Connection String to which RMI Server we are connected
	 */
	private String connectionURI = null;

	/**
	 * list of readers supported by the RMI Service
	 */
	private List<String> supportedReaders = null;

	/**
	 * reader blueprints describing the reader out of the reader xml definitions
	 */
	private HashMap<String, ReaderBlueprint> readerBlueprints = new HashMap<String, ReaderBlueprint>();

	/**
	 * List of listners for events like add reader or remove reader (there is
	 * also a event update)
	 */
	private LinkedList<RegistryChangeListener> listeners = new LinkedList<RegistryChangeListener>();

	private ReaderRegistry() {

	}

	/**
	 * @return a instance of the ReaderRegistry
	 */
	public static ReaderRegistry getInstance() {
		return INSTANCE;
	}

	/**
	 * connect to a remote RMI Service
	 * 
	 * @param hostname
	 *            of the remote RMI Service
	 * @param port
	 *            of the remote RMI Service
	 * @throws ConnectException
	 */
	public void connect(String hostname, int port) throws ConnectException {
		logger.debug("Trying to establish RMI Connection with : " + hostname
				+ " " + port);
		connectionURI = "//" + hostname + ":" + port + "/";

		try {
			rifidiManager = (RifidiManagerInterface) TransparentItemProxy
					.getItem(connectionURI + RifidiManager.URL,
							new Class[] { RifidiManagerInterface.class });
		} catch (java.rmi.ConnectException e) {
			logger.error("Connection could not be established");
			throw e;
		} catch (RemoteException e2) {
			e2.printStackTrace();
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (NotBoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		}

		logger.debug("RMI Connection successful created");

		logger
				.info("Getting supported Readers from the Remote Rifidi Emulation");
		try {
			supportedReaders = rifidiManager.getSupportedReaderTypes();
		} catch (Exception e1) {
			logger.error("RMI Exception occured");
			e1.printStackTrace();
		}

		for (String currReader : supportedReaders) {
			DigestReaders diggy = new DigestReaders();
			try {

				byte currentXMLBytes[];

				currentXMLBytes = rifidiManager.getReaderXMLDescription(
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

	/**
	 * create a new reader over RMI
	 * 
	 * @param reader
	 *            UIReader representing the necessary data to create the reader
	 * @throws DuplicateReaderException
	 */
	public void create(GeneralReaderPropertyHolder grph)
			throws DuplicateReaderException {
		if (grph == null) {
			throw new IllegalArgumentException();
		}
		String readerName = grph.getReaderName();
		ReaderModuleManagerInterface readerManager = null;

		if (readerRegistry.containsKey(readerName)) {

			throw new DuplicateReaderException("The reader with the name "
					+ readerName + " already exists");
		}
		logger.debug("Creating reader " + readerName + " through RMI");
		try {
			if (rifidiManager.createReader(grph)) {
				readerManager = (ReaderModuleManagerInterface) TransparentItemProxy
						.getItem(
								connectionURI + readerName,
								new Class[] { ReaderModuleManagerInterface.class });
			}

			UIReader reader = new UIReader(readerManager, grph);

			UIReaderCallbackManager readerCallbackManager = new UIReaderCallbackManager(
					readerManager.getClientProxy());
			reader.setReaderCallbackManager(readerCallbackManager);

			readerRegistry.put(readerName, reader);
			addEvent(reader);
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

	/**
	 * remove a reader form the registry and delete the real one over RMI
	 * 
	 * @param reader
	 *            the UIReader to delete
	 */
	public void remove(UIReader reader) {
		try {
			rifidiManager.removeReader(reader.getReaderName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	/**
	 * add a new listener for events
	 * 
	 * @param listener
	 *            the listner for these events
	 */
	public void addListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * update event for a certain reader
	 * 
	 * @param event
	 *            the UIReader to be updated
	 */
	public void update(UIReader event) {
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

	/**
	 * remove this listner
	 * 
	 * @param listener
	 *            to listen for events
	 */
	public void removelListener(RegistryChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * cleanup the registry and all remote instances of readers
	 */
	public void clean() {
		for (UIReader reader : readerRegistry.values()) {
			try {
				rifidiManager.removeReader(reader.getReaderName());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		rifidiManager = null;
		connectionURI = null;
		readerRegistry.clear();
		readerBlueprints.clear();
		supportedReaders.clear();

	}

	/**
	 * get the list of reader blueprints available
	 * 
	 * @return a list of reader blueprints
	 */
	public Map<String, ReaderBlueprint> getReaderBlueprints() {
		return new HashMap<String, ReaderBlueprint>(readerBlueprints);
	}

	/**
	 * get a complete list of all readers in this registry
	 * 
	 * @return
	 */
	public List<UIReader> getReaderList() {
		return new ArrayList<UIReader>(readerRegistry.values());
	}

	/**
	 * get one specific reader by name
	 * 
	 * @param readerName
	 *            the name of the reader
	 * @return a UIReader with the given name
	 */
	public UIReader getReaderByName(String readerName) {
		return readerRegistry.get(readerName);
	}

	/**
	 * tests if the name is not used
	 * 
	 * @param readerName
	 *            the name for which should be searched
	 * @return true if the name is not used, false otherwise
	 */
	public boolean isNameAvailable(String readerName) {
		return !readerRegistry.containsKey(readerName);
	}

	/**
	 * get the log the reader writes to the console
	 * 
	 * @param readerName
	 *            name of the reader
	 * @return a List of all cached logs (log will be empty after that)
	 */
	public List<String> getCachedLogs(String readerName) {
		List<String> cache = new ArrayList<String>();
		try {
			cache = rifidiManager.getCachedLogs(readerName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cache;
	}

}
