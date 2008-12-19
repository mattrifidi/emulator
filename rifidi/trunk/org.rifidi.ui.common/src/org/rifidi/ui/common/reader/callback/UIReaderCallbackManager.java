/**
 * 
 */
package org.rifidi.ui.common.reader.callback;

import gnu.cajo.invoke.RemoteInvoke;
import gnu.cajo.utils.extra.ItemProxy;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.rmi.client.ClientCallbackInterface;
import org.rifidi.tags.IGen1Tag;

/**
 * This is the dispatcher for the ReaderCallback Methods. The reader can send
 * events which get distributed to all the registered listeners for the
 * different Events.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class UIReaderCallbackManager implements ClientCallbackInterface {

	private Log logger = LogFactory.getLog(UIReaderCallbackManager.class);

	/**
	 * List of listeners for gpo port changes
	 */
	private LinkedList<GPOEventCallbackInterface> gpoPortListeners = new LinkedList<GPOEventCallbackInterface>();

	/**
	 * List of listeners for reader turned of events
	 */
	private LinkedList<ReaderTurnOffCallbackInterface> readerTurnOffListeners = new LinkedList<ReaderTurnOffCallbackInterface>();

	/**
	 * List of listeners for tag id changes
	 */
	private LinkedList<TagIDChangedCallbackInterface> tagIDChangedListeners = new LinkedList<TagIDChangedCallbackInterface>();

	/**
	 * @param clientProxy
	 */
	public UIReaderCallbackManager(RemoteInvoke clientProxy) {
		//create the ItemProxy needed for the RMI Callback
		new ItemProxy(clientProxy, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.client.ClientCallbackInterface#GPOPortSetHigh(int)
	 */
	public void GPOPortSetHigh(int gpoPortNum) {
		logger.debug("GPO is now HIGH");
		for (GPOEventCallbackInterface listener : gpoPortListeners) {
			listener.GPOPortSetHigh(gpoPortNum);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.client.ClientCallbackInterface#GPOPortSetLow(int)
	 */
	public void GPOPortSetLow(int gpoPortNum) {
		logger.debug("GPO is now LOW");
		for (GPOEventCallbackInterface listener : gpoPortListeners) {
			listener.GPOPortSetLow(gpoPortNum);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.client.ClientCallbackInterface#readerTurnedOff()
	 */
	public void readerTurnedOff() {
		logger.debug("readerTurnedOff");
		for (ReaderTurnOffCallbackInterface listener : readerTurnOffListeners) {
			listener.readerTurnedOff();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.emulator.rmi.client.ClientCallbackInterface#tagIDChanged(byte[],
	 *      byte[])
	 */
	public void tagIDChanged(Long entityID, IGen1Tag tag) {
		logger.debug("Tag ID changed");
		for (TagIDChangedCallbackInterface listener : tagIDChangedListeners) {
			listener.tagIDChanged(entityID, tag);
		}
	}

	/**
	 * add a new listener to the GPO listener list
	 * 
	 * @param listener
	 */
	public void addGPOPortListener(GPOEventCallbackInterface listener) {
		gpoPortListeners.add(listener);
	}

	/**
	 * add a new listner to the ReaderTurnedOff event listeners list
	 * 
	 * @param listener
	 */
	public void addReaderTurnedOffListener(
			ReaderTurnOffCallbackInterface listener) {
		readerTurnOffListeners.add(listener);
	}

	/**
	 * add a new TagIDChanged listner to the list
	 * 
	 * @param listener
	 */
	public void addTagIDChangedListener(TagIDChangedCallbackInterface listener) {
		tagIDChangedListeners.add(listener);
	}

	/**
	 * remove a GPO event listener
	 * 
	 * @param listener
	 */
	public void removeGPOGPOPortListener(GPOEventCallbackInterface listener) {
		gpoPortListeners.add(listener);
	}

	/**
	 * remove a ReaderTurnedOff listner
	 * 
	 * @param listener
	 */
	public void removeReaderTurnedOffListener(
			ReaderTurnOffCallbackInterface listener) {
		readerTurnOffListeners.add(listener);
	}

	/**
	 * remove a TagIDChanged listner
	 * 
	 * @param listener
	 */
	public void removeTagIDChangedListener(
			TagIDChangedCallbackInterface listener) {
		tagIDChangedListeners.add(listener);
	}

}
