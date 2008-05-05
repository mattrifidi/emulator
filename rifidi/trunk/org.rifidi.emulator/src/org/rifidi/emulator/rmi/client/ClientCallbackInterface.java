/**
 * 
 */
package org.rifidi.emulator.rmi.client;

import org.rifidi.emulator.tags.Gen1Tag;

/**
 * @author kyle
 *
 */
public interface ClientCallbackInterface {
	
	public void readerTurnedOff();
	
	public void tagIDChanged(byte[] oldID, Gen1Tag tag);
	
	public void GPOPortSetHigh(int gpoPortNum);
	
	public void GPOPortSetLow(int gpoPortNum);
	
	//public void readerProperties?

}
