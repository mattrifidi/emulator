/*
 *  AntennaFieldThread.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.antennafield;

import java.util.ArrayList;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.rmi.server.ReaderModuleManagerInterface;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * Rifidi emulator RMI communication is slow so all communication is done in
 * this thread to prevent the main thread from blocking.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 12, 2008
 * 
 */
public class AntennaFieldThread extends Thread {
	/**
	 * logger for this class.
	 */
	private static Log logger = LogFactory.getLog(Thread.class);
	/**
	 * Stack that holds the submitted actions
	 */
	private Stack<AntennaFieldAction> actionStack;
	/**
	 * Flag for stopping the thread.
	 */
	private boolean keepRunning = true;
	/**
	 * RMI interface for emulator.
	 */
	private ReaderModuleManagerInterface readerInterface;
	/**
	 * Number of the antenna this thread is associate with.
	 */
	private int antennaNum;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param antennaNum
	 * @param readerInterface
	 */
	public AntennaFieldThread(String name, int antennaNum,
			ReaderModuleManagerInterface readerInterface) {
		super(name);
		actionStack = new Stack<AntennaFieldAction>();
		this.readerInterface = readerInterface;
		this.antennaNum = antennaNum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		ArrayList<RifidiTag> addTags = new ArrayList<RifidiTag>();
		ArrayList<Long> remTags = new ArrayList<Long>();
		while (keepRunning) {
			while (!actionStack.isEmpty()) {
				AntennaFieldAction action = actionStack.pop();
				if (action.add) {
					addTags.add(action.tag);
					try {
						readerInterface.addTags(antennaNum, addTags);
					} catch (Exception e) {
						logger.error(e);
					}
					addTags.clear();
				} else {
					remTags.add(action.tag.getTagEntitiyID());
					try {
						readerInterface.removeTags(antennaNum, remTags);
					} catch (Exception e) {
						logger.error(e);
					}
					remTags.clear();
				}
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * Add an action to the list of actions to execute.
	 * 
	 * @param action
	 */
	public void addAction(AntennaFieldAction action) {
		actionStack.push(action);
	}

	/**
	 * @param keepRunning
	 *            the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

}
