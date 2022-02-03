/**
 * 
 */
package org.rifidi.emulator.manager;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is an interface which can be used by an outside program to control
 * Emulator. The ReaderManger will keep up with all created readers and allows a
 * user to access the funtionality of each reader via a readerID that is the
 * readerName in the GeneralReaderPropertyHolder
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 */
public interface ReaderManager {

	/**
	 * Returns a list of reader types that are available this session.
	 * 
	 * @return A list of reader types that can be used this session.
	 */
	Set<String> getReaderTypes();

	/**
	 * Creates a reader with the given GeneralReaderPropertyHolder. If the
	 * reader is successfully created, the ID for the reader is returned.
	 * 
	 * @param grph
	 *            The property holder used to create the reader.
	 * @return The ID of the reader that has been successfully created.
	 */
	String createReader(GeneralReaderPropertyHolder grph);

	/**
	 * Deletes a reader with the given ID.
	 * 
	 * @param readerID
	 *            The ID of the reader you wish to delete.
	 */
	void deleteReader(String readerID);

	/**
	 * Starts the reader with the given ID.
	 * 
	 * @param readerID
	 *            The ID of the reader you want to start.
	 */
	void start(String readerID);

	/**
	 * Stop the reader with the given ID.
	 * 
	 * @param readerID
	 *            The ID of the reader you want to stop.
	 */
	void stop(String readerID);

	/**
	 * Suspend reader
	 * 
	 * @param readerID
	 */
	void suspend(String readerID);

	/**
	 * Resume reader
	 * 
	 * @param readerID
	 */
	void resume(String readerID);

	/**
	 * Add tags to the reader with the given ID on the given antenna number.
	 * 
	 * @param readerID
	 *            The ID of the reader you wish to add tags to.
	 * @param antenna
	 *            The ID of the antenna you wish you add tags to on the given
	 *            reader.
	 * @param tagList
	 *            The tags you wish to add to the antenna.
	 */
	void addTags(String readerID, int antenna, Set<RifidiTag> tagList);

	/**
	 * Removes tags from the given antenna.
	 * 
	 * @param readerID
	 *            The ID of the reader you wish to remove tags from.
	 * @param antenna
	 *            The ID of the antenna you wish to remove tags from on the
	 *            given reader.
	 * @param tagList
	 *            The tags you wish to remove from the reader.
	 */
	void removeTags(String readerID, int antenna, Set<RifidiTag> tagList);

	/**
	 * Get the tags that are currently on an antenna
	 * 
	 * @param readerID
	 * @param antennaNum
	 * @return
	 */
	List<RifidiTag> getTagList(String readerID, int antennaNum);

	/**
	 * Sets the given GPI port to high for the given reader.
	 * 
	 * @param readerID
	 *            The ID of the reader you want to modify.
	 * @param port
	 *            The port you wish to modify.
	 */
	void setGPIPortHigh(String readerID, int port);

	/**
	 * Sets the given GPI port to low for the given reader.
	 * 
	 * @param readerID
	 *            The ID of the reader you want to modify.
	 * @param port
	 *            The port you wish to modify.
	 */
	void setGPIPortLow(String readerID, int port);

	/**
	 * Tests a GPO port for the given reader.
	 * 
	 * @param readerID
	 *            The ID of the reader.
	 * @param port
	 *            The ID of the port you wish to check.
	 * @return True if the port is high, false if the port is low.
	 */
	boolean testGPOPort(String readerID, int port);

	/**
	 * Creates a Gen1 RifidiTag with the given data.
	 * 
	 * @param data
	 *            The data that the tag will be created from.
	 * @return A Gen2 RifidiTag containing the given data.
	 */
	RifidiTag createGen1Tag(byte[] epcID);

	/**
	 * Creates a Gen2 Class 1 RifidiTag with the given data.
	 * 
	 * @param data
	 *            The data that the tag will be created from.
	 * @return A Gen2 RifidiTag containing the given data.
	 */
	RifidiTag createGen2Class1Tag(byte[] epcID, byte[] accessPass,
			byte[] killPass);

	/**
	 * Returns a default GeneralReaderPropertyHolder for the given reader type.
	 * 
	 * @param readerType
	 *            The type of reader that you wish to get a
	 *            GeneralReaderPropertyHolder for.
	 * @return A default GeneralReaderPropertyHolder for the given reader type.
	 */
	GeneralReaderPropertyHolder getDefault(String readerType);

	/**
	 * Gets the XML description of a reader module factory so that a user knows
	 * how to create a GRPH
	 * 
	 * @param readerType
	 * @return
	 */
	String getXMLDescription(String readerType);

	/**
	 * Get the categories of the properties
	 * 
	 * @param readerID
	 * @return
	 */
	Collection<String> getAllCategories(String readerID);

	/**
	 * Get the properties sorted into categories
	 * 
	 * @param readerID
	 * @param category
	 * @return
	 */
	Map<String, String> getCommandActionTagsByCategory(String readerID,
			String category);

	/**
	 * Get a property
	 * 
	 * @param readerID
	 * @param propertyName
	 * @return
	 */
	String getReaderProperty(String readerID, String propertyName);

	/**
	 * Set a property
	 * 
	 * @param readerID
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 */
	Boolean setReaderProperty(String readerID, String propertyName,
			String propertyValue);

	/**
	 * Get the numbers (i.e. IDs) of available GPI ports
	 * 
	 * @param readerID
	 * @return
	 */
	List<Integer> getGPIList(String readerID);

	/**
	 * Get the numbers (i.e. Ids) of available GPO ports
	 * 
	 * @param readerID
	 *            The Reader
	 * @return
	 */
	List<Integer> getGPOList(String readerID);

	/**
	 * Set a callback manager on the reader.
	 * 
	 * @param readerID
	 *            the Reader to set it on
	 * @param callback
	 */
	void setReaderCallback(String readerID, ClientCallbackInterface callback);

}
