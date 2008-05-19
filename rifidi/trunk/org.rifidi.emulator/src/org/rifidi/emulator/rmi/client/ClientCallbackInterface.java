/**
 * 
 */
package org.rifidi.emulator.rmi.client;

import org.rifidi.services.tags.IGen1Tag;

/**
 * @author kyle
 *
 */
public interface ClientCallbackInterface {
	
	public void readerTurnedOff();
	
	public void tagIDChanged(byte[] oldID, IGen1Tag tag);
	
	public void GPOPortSetHigh(int gpoPortNum);
	
	public void GPOPortSetLow(int gpoPortNum);
	
	//public void readerProperties?

}
