/*
 *  @(#)HeartbeatControllere.java
 *
 *  Created:	Feb 4, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.alien.heartbeat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.alien.module.AlienReaderSharedResources;
import org.rifidi.utilities.Timer;

/**
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Matt Dean - matt@pramari.com
 * 
 * This class implements the functionality needed by the alien to send out
 * heartbeat messages
 * 
 */
public class HeartbeatController implements Observer {

	private static Log logger = LogFactory.getLog(HeartbeatController.class);

	/**
	 * How often to send out messages
	 */
	private long timeInMs;

	/**
	 * The number of messages to send out. If -1, keep sending indefinetly.
	 */
	private int count;

	/**
	 * The broadcast address
	 */
	private String broadcastAddr;
	
	/**
	 * The port that the heartbeat UDP port should bind to
	 */
	private int bindingPort;

	/**
	 * The port to broadcast to
	 */
	private int broadcastPort;

	/**
	 * The shared resources
	 */
	private AlienReaderSharedResources asr;

	/**
	 * The timer that keeps track of when to send messages
	 */
	private Timer timer;

	/**
	 * Whether or not the hearbeat is enabled for this reader
	 */
	private boolean heartbeatEnabled;

	/**
	 * true if the timer is turned off
	 */
	private boolean timerStopped;

	/**
	 * 
	 * @param broadcastAddr -
	 *            The address to broadcast on
	 * @param broadcastPort -
	 *            The port to broadcast to
	 * @param asr -
	 *            Shared Resources
	 * @param sec -
	 *            time interval to send out messages
	 * @param count -
	 *            number of messages to send out. -1 is indefinate
	 * @param power -
	 *            whether or not heartbeats are enabled for this reader
	 */
	public HeartbeatController(String broadcastAddr, int broadcastPort, int bindingPort,
			AlienReaderSharedResources asr, int sec, int count, boolean power) {
		this.broadcastAddr = broadcastAddr;
		this.broadcastPort = broadcastPort;
		this.timeInMs = (long) sec * 1000;
		this.asr = asr;
		this.heartbeatEnabled = power;
		this.count = count;
		this.bindingPort = bindingPort;
	}

	/**
	 * Change the port that heartbeats are broadcasted on
	 * @param port
	 * @return
	 */
	public boolean setHeatbeatPort(int port) {
		this.broadcastPort = port;
		return true;
	}

	/**
	 * Change the IP Adress that heartbeats are broadcasted on
	 * @param address
	 * @return
	 */
	public boolean setHeartbeatIP(String address) {
		this.broadcastAddr = address;
		return true;
	}

	/**
	 * Update the time interval.  If time is 0, the heartbeats will stop
	 * @param sec
	 */
	public void updatePeriod(int sec) {

		long oldTime = this.timeInMs;
		this.timeInMs = sec * 1000;

		if (sec <= 0) {
			this.stopHeartbeat();
		}

		//if previous time was 0, we need to start things up again
		if (oldTime <= 0 && sec > 0) {
			startHeartbeat();
		}
	}

	/**
	 * Start sending heartbeats
	 */
	public void startHeartbeat() {
		if (heartbeatEnabled && (count != 0)) {
			timerStopped = false;

			// set up timer
			timer = new Timer(timeInMs);
			timer.addObserver(this);
			Thread t = new Thread(timer, "hearbeat timer");
			t.start();

			// send a heartbeat
			broadcast();

			// decrament count
			if (count > 0) {
				count--;
			}
		}
	}

	/**
	 * stop sending heartbeats
	 */
	public void stopHeartbeat() {
		if (heartbeatEnabled) {
			timer.stop();
			timerStopped = true;
		}
	}

	/**
	 * Send out a heartbeat
	 */
	private void broadcast() {
		String retString = "<Alien-RFID-Reader-Heartbeat>\n"
				+ "  <ReaderName>Alien RFID Reader</ReaderName>\n"
				+ "  <ReaderType>Alien RFID Tag Reader, Model: ALR-9800(Four Antenna / Multi-Protocol / 915 Mhz)"
				+ "</ReaderType>\n" + "  <IPAddress>" + asr.getCommandIP()
				+ "</IPAddress>\n" + "  <CommandPort>" + asr.getCommandPort()
				+ "</CommandPort>\n" + "  <HeartbeatTime>" + this.timeInMs
				/ 1000 + "</HeartbeatTime>\n"
				+ "  <MACAddress>00:00:00:00:00:00</MACAddress>\n"
				+ "</Alien-RFID-Reader-Heartbeat>\n";

		try {
			logger.debug("Attempting to send heartbeat...");
			DatagramSocket sock = new DatagramSocket(bindingPort);
			InetAddress ia = InetAddress.getByName(broadcastAddr);
			sock.setBroadcast(true);
			DatagramPacket p = new DatagramPacket(retString.getBytes(),
					retString.getBytes().length, ia, broadcastPort);
			sock.send(p);
			sock.disconnect();
			sock.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void suspend() {
		if (heartbeatEnabled && timer != null) {
			timer.suspend();
		}
	}

	public void resume() {
		if (heartbeatEnabled && timer != null) {
			timer.resume();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Timer) {
			if (!timerStopped) {
				// restart timer
				this.startHeartbeat();
			}
		}

	}

}
