/**
 * 
 */
package org.rifidi.emulator.rmi.client;

import org.rifidi.services.tags.IGen1Tag;

/**
 * This class is like a listener to events that happen inside of a Rifidi
 * virtual reader. This class usually should not be directly implemented.
 * Instead, ClientCallbackManager should be used.
 * 
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public interface ClientCallbackInterface {

	/**
	 * Reader was turned off.
	 */
	public void readerTurnedOff();

	/**
	 * Data on a tag was changed.
	 * @param UniqueID The id of the tag
	 * @param tag The new tag
	 */
	public void tagIDChanged(Long UniqueID, IGen1Tag tag);

	/**
	 * A certain GPO port was set to high
	 * @param gpoPortNum
	 */
	public void GPOPortSetHigh(int gpoPortNum);

	/**
	 * A certain GPO port was set to low.
	 * @param gpoPortNum
	 */
	public void GPOPortSetLow(int gpoPortNum);

	// public void readerProperties?

}
