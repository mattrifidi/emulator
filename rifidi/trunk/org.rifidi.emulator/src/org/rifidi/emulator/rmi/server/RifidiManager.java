/**
 * 
 */
package org.rifidi.emulator.rmi.server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;

import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.log.ReaderLogCacheSingleton;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * General Emulator Manager. Can be started in two Modes. 1st Mode: startup by a
 * IDE by calling startManager(hostname, port) 2nd Mode: Start by Console
 * main(hostname, port)
 * 
 * Creates an RMI Server and registers every Reader and itself to the RMI
 * Server.
 * 
 * The manager can be accessed by the URI "rifidiManager". Each reader will be
 * registered under it's name.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class RifidiManager implements RifidiManagerInterface {

	/** The log4j logger for this class. */
	private static Log logger = LogFactory.getLog(RifidiManager.class);
	/** Instance of the log cache */
	private ReaderLogCacheSingleton readerLogCache = ReaderLogCacheSingleton
			.getInstance();
	/** Is RifidiManager running */
	private static boolean isStarted = false;
	/** List of all created readers */
	private HashMap<String, ReaderModuleManager> readerRegistry = new HashMap<String, ReaderModuleManager>();
	/** Connection hostname */
	private static String hostname = "127.0.0.1";
	/** Connection port */
	private static int port = 1198;
	/** RMI Path for the rifidiManager */
	private static final String URL = "rifidiManager";
	/** A factory for creating readers */
	private Set<ReaderModuleFactory> readerModuleFactory;

	/**
	 * Called by spring. Constructor for the RifidiManager
	 * 
	 * @throws UnknownHostException
	 *             this exception is thrown when the hostname (ip) is not
	 *             resolve able
	 * @throws RemoteException
	 *             this exception is thrown when the port is already used
	 */
	public RifidiManager() throws UnknownHostException, RemoteException {
		logger.info("Starting up RMI Service at hostname: " + hostname
				+ " Port: " + port);
		Remote.config(hostname, port, null, 0);
		ItemServer.bind(this, URL);
		isStarted = true;
	}

	/**
	 * Called by spring
	 * 
	 * @param readerModuleFactory
	 *            the readerModuleFactory to set
	 */
	public void setReaderFactories(Set<ReaderModuleFactory> readerFactories) {
		this.readerModuleFactory = readerFactories;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.sandbox.cajo.test.RifidiManagerInterface#createReader(java
	 * .lang.String, java.lang.String, int, int, int, Map<String, String>)
	 */
	public Boolean createReader(GeneralReaderPropertyHolder properties) {
		if (readerRegistry.containsKey(properties.getReaderName())) {
			return false;
		}
		ReaderModuleFactory factory = getReaderFactoryByModuleClass(properties
				.getReaderClassName());
		if (factory == null) {
			return false;
		}
		ReaderModule reader = factory.createReaderModule(properties);

		ReaderModuleManager moduleManager = new ReaderModuleManager(reader);

		try {
			logger.info("Binding new Reader /" + properties.getReaderName());
			ItemServer.bind(moduleManager, properties.getReaderName());
			readerRegistry.put(properties.getReaderName(), moduleManager);
		} catch (RemoteException e) {
			logger.debug("Cannot bind " + properties.getReaderName()
					+ " to cajo registry");
			return false;
		}

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.sandbox.cajo.test.RifidiManagerInterface#removeReader(java
	 * .lang.String)
	 */
	public Boolean removeReader(String name) {
		try {
			ItemServer.registry.unbind(name);
			readerRegistry.get(name).turnReaderOff();
			readerRegistry.remove(name);
			logger.debug("try to remove reader " + name);
		} catch (AccessException e) {
			e.printStackTrace();
			return false;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (NotBoundException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiManagerInterface#getReaderList()
	 */
	public List<String> getReaderList() {
		return new ArrayList<String>(readerRegistry.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.sandbox.cajo.test.RifidiManagerInterface#getSupportedReaderTypes
	 * ()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSupportedReaderTypes() {
		List<String> types = new ArrayList<String>();
		for (ReaderModuleFactory factory : readerModuleFactory) {
			types.add(factory.getReaderType());
		}
		return types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.server.RifidiManagerInterface#cleanup()
	 */
	public Boolean cleanup() {
		boolean errorOccured = false;
		for (String readerName : readerRegistry.keySet()) {
			try {
				ItemServer.registry.unbind(readerName);
			} catch (AccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorOccured = true;
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorOccured = true;
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				errorOccured = true;
			}
		}
		logger.debug("Try to unbind the socket the rmi registry was using");
		try {
			UnicastRemoteObject.unexportObject(ItemServer.registry, false);
		} catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
		if (!errorOccured) {
			readerRegistry.clear();
			return true;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.rmi.server.RifidiManagerInterface#getReaderXMLDescription
	 * (java.lang.String)
	 */
	public String getReaderXMLDescription(String readerModule) {
		logger.info(readerModule);
		ReaderModuleFactory factory = getReaderFactoryByReaderType(readerModule);
		if (factory != null) {
			return factory.getReaderXMLDescription();
		} else {
			logger.warn("No factory found for " + readerModule);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.rmi.server.RifidiManagerInterface#getCachedLogs(java
	 * .lang.String)
	 */
	public List<String> getCachedLogs(String readerName) throws Exception {
		// TODO: Implement this Method as a Callback because it get's invoked
		// every 50ms
		return readerLogCache.getCache(readerName);
	}

	/**
	 * @return the isStarted
	 */
	public static boolean isStarted() {
		return isStarted;
	}

	/**
	 * Helper method to lookup reader factory by the reader type
	 * 
	 * @param name
	 * @return
	 */
	private ReaderModuleFactory getReaderFactoryByReaderType(String name) {
		for (ReaderModuleFactory factory : this.readerModuleFactory) {
			if (factory.getReaderType().equals(name)) {
				return factory;
			}
		}
		return null;
	}

	/**
	 * Helper method to look up reader factory by the reader module class name
	 * 
	 * @param className
	 * @return
	 */
	private ReaderModuleFactory getReaderFactoryByModuleClass(String className) {
		for (ReaderModuleFactory factory : this.readerModuleFactory) {
			if (factory.getReaderModuleClassName().equals(className)) {
				return factory;
			}
		}
		return null;
	}

}
