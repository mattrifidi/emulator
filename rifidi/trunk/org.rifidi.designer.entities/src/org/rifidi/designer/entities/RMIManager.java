/*
 *  RMIManager.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities;

import gnu.cajo.utils.extra.TransparentItemProxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.emulator.rmi.server.RifidiManager;
import org.rifidi.emulator.rmi.server.RifidiManagerInterface;

/**
 * 
 * 
 */
public class RMIManager {
	/**
	 * Logger
	 */
	private static final Log logger = LogFactory.getLog(RMIManager.class);
	/**
	 * Port to use for connections
	 */
	private static final int PORT = 1198;
	/**
	 * The instances of the rmimanager for different hosts
	 */
	private static HashMap<String, RMIManager> INSTANCE = new HashMap<String, RMIManager>();
	/**
	 * The host this manager is connected to
	 */
	private String hostname = null;
	/**
	 * The interface rifidi should use for communicating with the manager
	 */
	private RifidiManagerInterface rifidiManager;
	/**
	 * A hash of the reader interfaces for this connection
	 */
	private HashMap<String, ReaderModuleManagerInterface> readerRegisty = new HashMap<String, ReaderModuleManagerInterface>();

	/**
	 * Gets the RMIManager instance for the given hostname
	 * 
	 * @param hostname
	 *            the desired hostname's instance
	 * @return the interface for the requested hostname (created if it didn't
	 *         exist)
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static RMIManager getInstance(String hostname)
			throws RemoteException, MalformedURLException, NotBoundException,
			IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		if (INSTANCE.get(hostname) == null) {
			INSTANCE.put(hostname, initReaderRegistry(hostname));
		}
		return INSTANCE.get(hostname);
	}

	/**
	 * Initializes the reader registry for the given hostname
	 * 
	 * @param hostname
	 *            the host to initialize the reader registry for
	 * @return the initialized reader registry
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static RMIManager initReaderRegistry(String hostname)
			throws RemoteException, MalformedURLException, NotBoundException,
			IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		RMIManager ret = new RMIManager();
		ret.hostname = hostname;
		ret.rifidiManager = (RifidiManagerInterface) TransparentItemProxy
				.getItem(
						"//" + hostname + ":" + PORT + "/" + RifidiManager.URL,
						new Class[] { RifidiManagerInterface.class });

		return ret;
	}

	/**
	 * Default empty constructor (not visible) Implemeting classes should use a
	 * singleton like call to getInstance to prevent multiple instantiations
	 * with the same host
	 */
	private RMIManager() {
	}

	/**
	 * Clean up the connection to the host and the reader registry
	 * 
	 * @return
	 */
	public boolean cleanup() {
		try {
			if (rifidiManager.cleanup()) {
				readerRegisty.clear();
				return true;
			}
		} catch (Exception e) {
			logger.fatal("WHO THROWS JUST EXCEPTION???: "+e);
		}
		return false;
	}

	/**
	 * Unregisters the RMIManager
	 * 
	 * @return true if it was removed from the listing of rmimanager instances,
	 *         false if it wasn't registered (shouldn't happen)
	 */
	public boolean disconnect() {
		if (INSTANCE.remove(hostname) != null)
			return true;
		return false;
	}

	/**
	 * Creates a reader with the given properties and returns an interface for
	 * communicating with it
	 * 
	 * @param generalReaderPropertyHolder
	 *            the desired properties for the reader to be created
	 * @return the interface for communicating with the reader
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public ReaderModuleManagerInterface createReader(
			GeneralReaderPropertyHolder generalReaderPropertyHolder)
			throws RemoteException, MalformedURLException, NotBoundException,
			IOException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {

		try {
			if (rifidiManager.createReader(generalReaderPropertyHolder)) {
				logger.debug("Reader created starting Managing Interface");
				String readerName = generalReaderPropertyHolder.getReaderName();
				ReaderModuleManagerInterface reader = (ReaderModuleManagerInterface) TransparentItemProxy
						.getItem(
								"//" + hostname + ":" + PORT + "/" + readerName,
								new Class[] { ReaderModuleManagerInterface.class });
				logger
						.debug("Got the RMI Management Interface for the Reader: "
								+ readerName);
				readerRegisty.put(readerName, reader);
				logger.debug("Reader registered in the RMIManager");
				return reader;
			}
		} catch (Exception e) {
			logger.fatal("WHO THROWS JUST EXCEPTION???: "+e);
		}
		return null;
	}

	/**
	 * Removes the reader from the reader registry
	 * 
	 * @param name
	 *            the name of the reader to remove
	 * @return true if reader was succesfully removd, false otherwise
	 */
	public Boolean removeReader(String name) {
		try {
			return rifidiManager.removeReader(name);
		} catch (Exception e) {
			logger.fatal("WHO THROWS JUST EXCEPTION???: "+e);
		}
		return false;
	}

	/**
	 * Gets a list of the readers registered for this host
	 * 
	 * @return a list of the registered readers
	 */
	public List<String> getReaderList() {
		try {
			return rifidiManager.getReaderList();
		} catch (Exception e) {
			logger.fatal("WHO THROWS JUST EXCEPTION???: "+e);
		}
		return null;
	}

	/**
	 * Get the types of readers that are supported
	 * 
	 * @return a list of the supported reader types
	 */
	public List<String> getSupportedReaderTypes() {
		try {
			return rifidiManager.getSupportedReaderTypes();
		} catch (Exception e) {
			logger.fatal("WHO THROWS JUST EXCEPTION???: "+e);
		}
		return null;
	}

	/**
	 * Get the reader with the given name
	 * 
	 * @param name
	 *            the name of the reader to get
	 * @return the requested reader
	 */
	public ReaderModuleManagerInterface getReader(String name) {
		return readerRegisty.get(name);
	}

	/**
	 * Gets a description of the reader specified by the readerModuel
	 * 
	 * @param readerModule
	 *            a string describing the type of reader to get a description of
	 * @return a description of the requested reader type in XML
	 */
	public String getReaderXMLDescription(String readerModule) {
		try {
			return rifidiManager.getReaderXMLDescription(readerModule);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the cached logs for the given reader
	 * 
	 * @param readerName
	 *            the name of the reader to get the cached logs for
	 * @return the cached logs for the requested reader
	 * @throws Exception
	 *             if something happens
	 */
	public List<String> getCachedLogs(String readerName) throws Exception {
		return rifidiManager.getCachedLogs(readerName);
	}
}