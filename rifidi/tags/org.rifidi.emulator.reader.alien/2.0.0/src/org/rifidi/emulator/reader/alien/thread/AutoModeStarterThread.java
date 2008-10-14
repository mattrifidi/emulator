/**
 * 
 */
package org.rifidi.emulator.reader.alien.thread;

import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;

/**
 * This class starts the Autonomous Mode for the alien reader in a thread.
 * 
 * @author Matthew
 */
public class AutoModeStarterThread extends Thread {
	
	private AlienReaderSharedResources asr;
	
	/**
	 * 
	 * @param asr
	 */
	public AutoModeStarterThread(AlienReaderSharedResources asr) {
		this.asr=asr;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		this.asr.getAutoStateController().startAutoMode();
	}
}
