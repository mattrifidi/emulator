package org.rifidi.emulator.rmi.server;

import gnu.cajo.invoke.RemoteInvoke;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * The ReaderModuleInterface represents the methods that a reader exposes to the
 * client over RMI
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public interface ReaderModuleManagerInterface {

	/**
	 * Turn on the power of the reader
	 * 
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void turnReaderOn() throws Exception;

	/**
	 * Turn off power of the reader
	 * 
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void turnReaderOff() throws Exception;

	/**
	 * Suspend the reader
	 * 
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void suspendReader() throws Exception;

	/**
	 * Resume the reader from suspension
	 * 
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void resumeReader() throws Exception;

	/**
	 * Add a collection of tags to the reader
	 * 
	 * @param antennaNum
	 *            Antenna number the tags should be added to
	 * @param tagsToAdd
	 *            Collection of tags to add
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void addTags(int antennaNum, Collection<RifidiTag> tagsToAdd)
			throws Exception;

	/**
	 * Remove a collection of tags from the reader's antenna
	 * 
	 * @param antennaNum
	 *            Antenna number the tags should be removed
	 * @param tagIDsToRemove
	 *            Collection of byte[] representing the EPC ID of the tags to
	 *            delete
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void removeTags(int antennaNum, Collection<byte[]> tagIDsToRemove)
			throws Exception;

	// TODO Suggestion to add this method for convenience 
	//public void removeTags(int antennaNum, List<RifidiTag> tagsToRemove) throws Exception;
	
	/**
	 * Get a list of tags in the field of the readers antenna
	 * 
	 * @param antennaNum
	 *            antenna you want to get the actual tags from
	 * @return list of RifidiTags or null
	 */
	public List<RifidiTag> getTagList(int antennaNum) throws Exception;

	/**
	 * If the reader supports GPI's it's possible to simulate a GPIHigh event
	 * 
	 * @param GPIPort
	 *            the appropriate port this event belongs to
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void setGPIHigh(int GPIPort) throws Exception;

	/**
	 * If the reader supports GPI's it's possible to simulate a GPILow event
	 * 
	 * @param GPIPort
	 *            the appropriate port the event belongs to
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public void setGPILow(int GPIPort) throws Exception;

	/**
	 * Get the GeneralReaderPropertyHolder describing the Reader startup
	 * settings
	 * 
	 * @return the GeneralReaderPropertyHolder with the reader properties
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public GeneralReaderPropertyHolder getReaderProperties() throws Exception;

	/**
	 * Get the type of the reader
	 * 
	 * @return returns the class name of the reader as a string
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public String getReaderType() throws Exception;

	/**
	 * Get the ClientProxy to realize callback methods over cajo
	 * 
	 * @return the ClientProxy to associate it with the client callback
	 *         interface
	 * @throws Exception
	 *             will be thrown if errors in the RMI communication occur
	 */
	public RemoteInvoke getClientProxy() throws Exception;

	// TODO Discussion about PropertiesView
	// TODO Implemented because of PropertiesView
	public Collection<String> getAllCategories() throws Exception;

	// TODO Implemented because of PropertiesView
	public boolean setProperty(String propertyName, String propertyValue)
			throws Exception;

	// TODO Implemented because of PropertiesView
	public Map<String, String> getCommandActionTagsByCategory(String category)
			throws Exception;

	// TODO Implemented because of PropertiesView
	public String getReaderProperty(String propertyName) throws Exception;

	// TODO Implemented because of PropertiesView
	public Boolean setReaderProperty(String propertyName, String propertyValue) throws Exception;

}
