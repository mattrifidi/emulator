/**
 * 
 */
package org.rifidi.emulator.manager;

import org.rifidi.tags.IGen1Tag;

/**
 * This allows clients to listen to certain events that happen inside a Rifidi
 * virtual reader so that they can respond to the events. This allows rich UIs
 * to, for example, change the EPC of a tag when the tag ID is written
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
	 * 
	 * @param UniqueID
	 *            The id of the tag
	 * @param tag
	 *            The new tag
	 */
	public void tagIDChanged(Long UniqueID, IGen1Tag tag);

	/**
	 * A certain GPO port was set to high
	 * 
	 * @param gpoPortNum
	 */
	public void GPOPortSetHigh(int gpoPortNum);

	/**
	 * A certain GPO port was set to low.
	 * 
	 * @param gpoPortNum
	 */
	public void GPOPortSetLow(int gpoPortNum);

	// public void readerProperties?

}
