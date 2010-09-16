/**
 * 
 */
package org.rifidi.emulator.scripting;

import java.util.Set;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is an interface which can be used by an outside program to control
 * Emulator.
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public interface ReaderManager {

	/**
	 * Returns a list of reader types that are available this session.
	 * 
	 * @return A list of reader types that can be used this session.
	 */
	public Set<String> getReaderTypes();

	/**
	 * Creates a reader with the given GeneralReaderPropertyHolder. If the
	 * reader is successfully created, the ID for the reader is returned.
	 * 
	 * @param grph	The property holder used to create the reader.  
	 * @return		The ID of the reader that has been successfully created.  
	 */
	public String createReader(GeneralReaderPropertyHolder grph);

	/**
	 * Deletes a reader with the given ID.  
	 * 
	 * @param readerID	The ID of the reader you wish to delete.  
	 */
	public void deleteReader(String readerID);

	/**
	 * Starts the reader with the given ID.  
	 * 
	 * @param readerID	The ID of the reader you want to start.  
	 */
	public void start(String readerID);

	/**
	 * Stop the reader with the given ID.  
	 * 
	 * @param readerID	The ID of the reader you want to stop.  
	 */
	public void stop(String readerID);

	/**
	 * Add tags to the reader with the given ID on the given antenna number.  
	 * 
	 * @param readerID	The ID of the reader you wish to add tags to.  
	 * @param antenna	The ID of the antenna you wish you add tags to on the given reader.
	 * @param tagList	The tags you wish to add to the antenna.  
	 */
	public void addTags(String readerID, int antenna, Set<RifidiTag> tagList);

	/**
	 * Removes tags from the given antenna.  
	 * 
	 * @param readerID	The ID of the reader you wish to remove tags from.
	 * @param antenna	The ID of the antenna you wish to remove tags from on the given reader.   
	 * @param tagList	The tags you wish to remove from the reader.  
	 */
	public void removeTags(String readerID, int antenna, Set<RifidiTag> tagList);

	/**
	 * Sets the given GPI port to high for the given reader.  
	 * 
	 * @param readerID	The ID of the reader you want to modify.  
	 * @param port		The port you wish to modify.  
	 */
	public void setGPIPortHigh(String readerID, int port);

	/**
	 * Sets the given GPI port to low for the given reader.  
	 * 
	 * @param readerID	The ID of the reader you want to modify.  
	 * @param port		The port you wish to modify.  
	 */
	public void setGPIPortLow(String readerID, int port);

	/**
	 * Tests a GPO port for the given reader.  
	 * 
	 * @param readerID	The ID of the reader.  
	 * @param port		The ID of the port you wish to check.  
	 * @return			True if the port is high, false if the port is low.  
	 */
	public boolean testGPOPort(String readerID, int port);

	/**
	 * Creates a Gen1 RifidiTag with the given data.  
	 * 
	 * @param data	The data that the tag will be created from.  
	 * @return		A Gen2 RifidiTag containing the given data.  
	 */
	public RifidiTag createGen1Tag(byte[] epcID);
	/**
	 * Creates a Gen2 Class 1 RifidiTag with the given data.  
	 * 
	 * @param data	The data that the tag will be created from.  
	 * @return		A Gen2 RifidiTag containing the given data.  
	 */
	public RifidiTag createGen2Class1Tag(byte[] epcID, byte[] accessPass, byte[] killPass);

	
	/**
	 * Returns a default GeneralReaderPropertyHolder for the given reader type.  
	 * 
	 * @param readerType	The type of reader that you wish to get a GeneralReaderPropertyHolder for.
	 * @return				A default GeneralReaderPropertyHolder for the given reader type.  
	 */
	public GeneralReaderPropertyHolder getDefault(String readerType);
}
