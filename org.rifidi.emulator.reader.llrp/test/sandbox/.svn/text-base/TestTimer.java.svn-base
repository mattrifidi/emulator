/*
 *  TimeController.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package sandbox;

import java.util.Observable;

/**
 * This class will fire off a given trigger in a certain amount of time.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 */
public class TestTimer extends Observable implements Runnable {
	
	long timeLeftToWait;
	String name;
	boolean suspended=false;
	long pausedTime=0;
	long extraPauseTime=0;
	long lastSuspendTime=0;

	/**
	 * Constructs a timeController, which will fire the given trigger
	 * 
	 * @param timeToWait
	 *            The time, in milliseconds.
	 */
	public TestTimer(long timeToWait) {
		this.timeLeftToWait = timeToWait;
	}

	/**
	 * Constructs a timeController, which will fire the given trigger
	 * 
	 * @param timeToWait
	 *            The time, in milliseconds.
	 */
	public TestTimer(long timeToWait, String name) {
		this.timeLeftToWait = timeToWait;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		boolean done = false;
		while(!done){
			sleepSome(timeLeftToWait);
			extraPauseTime=0;
			while(this.suspended){
				sleepSome(500);
				extraPauseTime+=500;
			}
			timeLeftToWait = timeLeftToWait - (timeLeftToWait - (pausedTime - extraPauseTime));
			if(timeLeftToWait<=0){
				done = true;
			}else{
				synchronized (this) {
					pausedTime=0;
				}
			}
		}

		this.setChanged();
		if (this.name != null) {
			this.notifyObservers(name);
		} else {
			this.notifyObservers();
		}
	}


	private void sleepSome(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void suspend() {
		System.out.println("Timer suspended");
		suspended = true;
		lastSuspendTime = System.currentTimeMillis();
	}

	public void resume() {
		System.out.println("Timer resumed");
		suspended = false;
		synchronized (this) {
			pausedTime += System.currentTimeMillis() - lastSuspendTime;
		}
	}
}
