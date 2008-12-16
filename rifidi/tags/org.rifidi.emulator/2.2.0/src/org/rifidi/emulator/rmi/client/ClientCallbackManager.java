/**
 * 
 */
package org.rifidi.emulator.rmi.client;

import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ItemProxy;

/**
 * An abstract implementation of ClientCallbackInterface
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public abstract class  ClientCallbackManager implements ClientCallbackInterface {

	
	ItemProxy ip = null;
	
	
	public ClientCallbackManager(RemoteInvoke clientProxy) {
		ip = new ItemProxy(clientProxy, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiClientInterface#GPOPortSetHigh(int)
	 */
	public abstract void GPOPortSetHigh(int gpoPortNum);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiClientInterface#GPOPortSetLow(int)
	 */
	public abstract void GPOPortSetLow(int gpoPortNum);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiClientInterface#readerTurnedOff()
	 */
	public abstract void readerTurnedOff();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.sandbox.cajo.test.RifidiClientInterface#tagIDChanged(int,
	 *      byte[], byte[])
	 */
	public abstract void tagIDChanged(byte[] oldID, byte[] newID);

}
