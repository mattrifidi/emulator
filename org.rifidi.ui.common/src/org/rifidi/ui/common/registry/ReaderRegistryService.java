package org.rifidi.ui.common.registry;

import java.rmi.ConnectException;
import java.util.List;
import java.util.Map;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;

public interface ReaderRegistryService {

	/**
	 * connect to a remote RMI Service
	 * 
	 * @param hostname
	 *            of the remote RMI Service
	 * @param port
	 *            of the remote RMI Service
	 * @throws ConnectException
	 */
	@Deprecated
	public abstract void connect(String hostname, int port)
			throws ConnectException;

	/**
	 * create a new reader over RMI
	 * 
	 * @param reader
	 *            UIReader representing the necessary data to create the reader
	 * @throws DuplicateReaderException
	 */
	public abstract void create(GeneralReaderPropertyHolder grph)
			throws DuplicateReaderException;

	/**
	 * remove a reader form the registry and delete the real one over RMI
	 * 
	 * @param reader
	 *            the UIReader to delete
	 */
	public abstract void remove(UIReader reader);

	/**
	 * get a complete list of all readers in this registry
	 * 
	 * @return
	 */
	public abstract List<UIReader> getReaderList();

	/**
	 * add a new listener for events
	 * 
	 * @param listener
	 *            the listner for these events
	 */
	public abstract void addListener(RegistryChangeListener listener);

	/**
	 * remove this listner
	 * 
	 * @param listener
	 *            to listen for events
	 */
	public abstract void removelListener(RegistryChangeListener listener);

	/**
	 * update event for a certain reader
	 * 
	 * @param event
	 *            the UIReader to be updated
	 */
	public abstract void update(UIReader event);

	/**
	 * cleanup the registry and all remote instances of readers
	 */
	public abstract void clean();

	/**
	 * get the list of reader blueprints available
	 * 
	 * @return a list of reader blueprints
	 */
	public abstract Map<String, ReaderBlueprint> getReaderBlueprints();

	/**
	 * get one specific reader by name
	 * 
	 * @param readerName
	 *            the name of the reader
	 * @return a UIReader with the given name
	 */
	public abstract UIReader getReaderByName(String readerName);

	/**
	 * tests if the name is not used
	 * 
	 * @param readerName
	 *            the name for which should be searched
	 * @return true if the name is not used, false otherwise
	 */
	public abstract boolean isNameAvailable(String readerName);

	/**
	 * get the log the reader writes to the console
	 * 
	 * @param readerName
	 *            name of the reader
	 * @return a List of all cached logs (log will be empty after that)
	 */
	public abstract List<String> getCachedLogs(String readerName);

}