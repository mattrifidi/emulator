/*
 *  @(#)AbstractCommunication.java
 *
 *  Created:	Sep 18, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.io.comm.abstract_;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.io.comm.Communication;
import org.rifidi.emulator.io.comm.CommunicationConnectionState;
import org.rifidi.emulator.io.comm.CommunicationException;
import org.rifidi.emulator.io.comm.CommunicationPowerState;
import org.rifidi.emulator.io.protocol.Protocol;
import org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule;

/**
 * An abstract communication implementation. It achieves power operations by
 * extending AbstractPowerModule and implements the getConnectionState method.
 * Other abstract behaviors are defined by the abstract states located in the
 * same package.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public abstract class AbstractCommunication extends AbstractPowerModule
		implements Communication {

	/**
	 * The ControlSignal that this will touch when client connects. This is used
	 * for asynchronous control.
	 * 
	 */
	private ControlSignal<Boolean> connectionControlSignal;

	/**
	 * The current connection state for this AbstractCommunication object.
	 */
	private CommunicationConnectionState curConnectionState;

	/**
	 * The current protocol this AbstractCommunication is using.
	 */
	private Protocol curProtocol;

	/**
	 * Returns the connectionControlSignal.
	 * 
	 * @return Returns the connectionControlSignal.
	 */
	protected ControlSignal<Boolean> getConnectionControlSignal() {
		return this.connectionControlSignal;

	}

	/**
	 * Creates an AbstractCommunication with an initial state being the one
	 * passed as an argument.
	 * 
	 * @param initialPowerState
	 *            The desired initial power state of the AbstractCommunication.
	 * @param initialConnectionState
	 *            The desired initial connection state of the
	 *            AbstractCommunication.
	 * @param protocol
	 *            The protocol that this communication uses while communicating.
	 * @param powerControlSignal
	 *            The control signal for this AbstractCommunication to observe.
	 *            If <i>null</i> is passed, this will not observe a signal.
	 * @param connectionControlSignal
	 *            The ControlSignal that this will update when client connects
	 *            or disconnects.
	 * 
	 */
	public AbstractCommunication(CommunicationPowerState initialPowerState,
			CommunicationConnectionState initialConnectionState,
			Protocol protocol, ControlSignal<Boolean> powerControlSignal,
			ControlSignal<Boolean> connectionControlSignal) {
		/* Call the AbstractPowerModule's constructor */
		super(initialPowerState, powerControlSignal);

		/* Assign instance variables */
		this.curConnectionState = initialConnectionState;
		this.curProtocol = protocol;
		this.connectionControlSignal = connectionControlSignal;

		/* Register as connection observer */
		if (this.connectionControlSignal != null) {
			this.connectionControlSignal.addObserver(this);

		}

	}

	/**
	 * Passes the request to the current connection state.
	 * 
	 * @see org.rifidi.emulator.io.comm.Communication#connect()
	 */
	public void connect() {
		/* Call the current connection state object's corresponding method */
		this.curConnectionState.connect(this);
	}

	/**
	 * Passes the request to the current connection state.
	 * 
	 * @see org.rifidi.emulator.io.comm.Communication#disconnect()
	 */
	public void disconnect() {
		/* Call the current connection state object's corresponding method */
		this.curConnectionState.disconnect(this);

	}

	/**
	 * Passes the request to the current connection state.
	 * 
	 * @see org.rifidi.emulator.io.comm.Communication#isConnected()
	 */
	public boolean isConnected() {
		/* Call the current connection state object's corresponding method */
		return this.curConnectionState.isConnected(this);

	}

	/**
	 * Passes the request to the current power state.
	 * 
	 * @throws CommunicationException
	 *             If the receive is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.Communication#receiveBytes()
	 */
	public byte[] receiveBytes() throws CommunicationException {
		/* Call the power state's corresponding method, throwing exceptions. */
		return ((CommunicationPowerState) (this.getPowerState()))
				.receiveBytes(this);

	}

	/**
	 * Passes the request to the current power state.
	 * 
	 * @throws CommunicationException
	 *             If the send is interrupted.
	 * 
	 * @see org.rifidi.emulator.io.comm.Communication#sendBytes(byte[])
	 */
	public void sendBytes(byte[] data) throws CommunicationException {
		/* Call the power state's corresponding method, throwing exceptions. */
		((CommunicationPowerState) (this.getPowerState()))
				.sendBytes(data, this);

	}

	/**
	 * Changes the current power state of this Communication.
	 * 
	 * @param anotherPowerState
	 *            The power state to change to.
	 */
	protected void changePowerState(CommunicationPowerState anotherPowerState) {
		super.changePowerState(anotherPowerState);

	}

	/**
	 * Changes the current power state of this Communication.
	 * 
	 * @param anotherConnectionState
	 *            The power state to change to.
	 */
	protected void changeConnectionState(
			CommunicationConnectionState anotherConnectionState) {
		/* Save the old power state (so we can synchronize on it) */
		final CommunicationConnectionState oldPowerState = this.curConnectionState;

		/* Change the power state, notifying anyone waiting on the old state */
		synchronized (oldPowerState) {
			this.curConnectionState = anotherConnectionState;
			oldPowerState.notifyAll();

		}

	}

	/**
	 * Returns the current CommunicationConnectionState. This is of default
	 * visibilty so that only other classes in the package, i.e., the abstract
	 * states, may use this method.
	 * 
	 * @return Returns the curConnectionState.
	 */
	CommunicationConnectionState getCurConnectionState() {
		return this.curConnectionState;

	}

	/**
	 * Returns the current protocol being used by this AbstractCommunication.
	 * 
	 * @return The current protocol being used by this AbstractCommunication.
	 */
	public Protocol getProtocol() {
		return this.curProtocol;

	}

}
