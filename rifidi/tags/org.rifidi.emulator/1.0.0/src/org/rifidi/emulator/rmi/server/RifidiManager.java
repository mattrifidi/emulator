/**
 * 
 */
package org.rifidi.emulator.rmi.server;

import gnu.cajo.invoke.Remote;
import gnu.cajo.utils.ItemServer;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
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

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(RifidiManager.class);

	/**
	 * Instance of the log cache
	 */
	private ReaderLogCacheSingleton readerLogCache = ReaderLogCacheSingleton
			.getInstance();

	/**
	 * Singleton Pattern
	 */
	private static RifidiManagerInterface INSTANCE = null;

	/**
	 * Is RifidiManager running
	 */
	private static boolean isStarted = false;
	
	/**
	 * List of all created readers
	 */
	private HashMap<String, ReaderModuleManager> readerRegistry = new HashMap<String, ReaderModuleManager>();

	/**
	 * Connection Informations
	 */
	private static String hostname = "127.0.0.1";
	private static int port = 1198;

	/**
	 * RMI Path for the rifidiManager
	 */
	public static final String URL = "rifidiManager";

	/**
	 * Constructor for the RifidiManager (this is a singleton so please use
	 * startManager)
	 * 
	 * @param ip
	 *            IpAddress to bind to
	 * @param port
	 *            Port to bind to
	 * @throws UnknownHostException
	 *             this exception is thrown when the hostname (ip) is not
	 *             resolve able
	 * @throws RemoteException
	 *             this exception is thrown when the port is already used
	 */
	private RifidiManager(String ip, int port) throws UnknownHostException,
			RemoteException {
		Remote.config(ip, port, null, 0);
		ItemServer.bind(this, URL);
	}

	/**
	 * Main method to start the emulator thru console
	 * 
	 * @param args
	 *            Arguments are IP-Address and Port-Number
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		if (args.length == 2) {
			if (args[0] != null) {
				hostname = args[0];
			}
			if (args[1] != null) {
				port = Integer.parseInt(args[1]);
			}
		}

		try {
			startManager(hostname, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			logger
					.fatal("Could not start RMIServer because socket allready in use!");
			System.exit(-1);
		}
	}

	/**
	 * start the RMI Service for the reader emulation at the given IP and Port
	 * both need to be local
	 * 
	 * @param _hostname
	 *            IPAddress of local Host
	 * @param _port
	 *            Port of local Host
	 * @throws UnknownHostException
	 *             when local IPAddress could not be resolved
	 * @throws RemoteException
	 *             when Port is already used by something else
	 * @throws IOException
	 */
	public static void startManager(String _hostname, Integer _port)
			throws UnknownHostException, RemoteException, IOException {
		hostname = _hostname;
		port = _port;
		if (INSTANCE == null) {
			INSTANCE = new RifidiManager(hostname, port);
			logger.info("Starting up RMI Service at hostname: " + hostname
					+ " Port: " + port);
			isStarted = true;
		}
	}

	/**
	 * Get the instance of the RifidiManager
	 * 
	 * @return the RifidiManager
	 */
	public static RifidiManagerInterface getManager() {
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiManagerInterface#createReader(java.lang.String,
	 *      java.lang.String, int, int, int, Map<String, String>)
	 */
	public Boolean createReader(GeneralReaderPropertyHolder properties) {
		if (!readerRegistry.containsKey(properties.getReaderName())) {
			// TODO Remember that the ReaderFactory is the old way of creating a
			// reader
			ReaderModule rm = ReaderModuleFactory
					.createReaderModule(properties);

			ReaderModuleManager moduleManager = new ReaderModuleManager(rm);

			try {
				ItemServer.bind(moduleManager, properties.getReaderName());
				readerRegistry.put(properties.getReaderName(), moduleManager);
			} catch (RemoteException e) {
				logger.debug("Cannot bind " + properties.getReaderName()
						+ " to cajo registry");
				return false;
			}

			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiManagerInterface#removeReader(java.lang.String)
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
	 * @see org.rifidi.sandbox.cajo.test.RifidiManagerInterface#getSupportedReaderTypes()
	 */
	@SuppressWarnings("unchecked")
	public List<String> getSupportedReaderTypes() {
		Map<String, Class> map = ReaderModuleFactory.getSupportedReaders();
		List<String> ret = new ArrayList<String>(map.keySet());
		return ret;
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
		if (!errorOccured) {
			readerRegistry.clear();
			return true;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.server.RifidiManagerInterface#getReaderXMLDescription(java.lang.String)
	 */
	public String getReaderXMLDescription(String readerModule) {
		logger.info(readerModule);
		return ReaderModuleFactory.getReaderXMLDescription(readerModule);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.server.RifidiManagerInterface#getLocalHostname()
	 */
	public String getLocalHostname() {
		return hostname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.server.RifidiManagerInterface#getLocalPort()
	 */
	public Integer getLocalPort() {
		return port;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.server.RifidiManagerInterface#getCachedLogs(java.lang.String)
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
	
}
