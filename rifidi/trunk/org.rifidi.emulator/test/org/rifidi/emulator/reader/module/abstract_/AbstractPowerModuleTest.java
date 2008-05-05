/*
 *  @(#)AbstractPowerModuleTest.java
 *
 *  Created:	Oct 2, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.emulator.reader.module.abstract_;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.common.PowerControllable;
import org.rifidi.emulator.common.PowerState;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * A collection of unit test cases for an AbstractPowerModule.
 * 
 * @author John Olender - john@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 * 
 */
public class AbstractPowerModuleTest extends TestCase {

	/**
	 * A test implementation of an AbstractOffPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractOffPowerState extends AbstractOffPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOn(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOn(PowerControllable pcObject) {
			/* Fake a power on event */
			AbstractPowerModuleTest.this.powerOnOccurred = true;

		}

	}

	/**
	 * A test implementation of an AbstractOnPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractOnPowerState extends AbstractOnPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#suspend(org.rifidi.emulator.common.PowerControllable)
		 */
		public void suspend(PowerControllable pcObject) {
			/* Fake a power suspend event */
			AbstractPowerModuleTest.this.powerSuspendOccurred = true;

		}

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOff(PowerControllable pcObject, Class callingClass) {
			/* Fake a power off event */
			AbstractPowerModuleTest.this.powerOffOccurred = true;

		}

	}

	/**
	 * A test implementation of an AbstractPowerModule.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractReaderModule extends AbstractPowerModule {

		/**
		 * A basic constructor for a test implementation.
		 * 
		 * @param initialPowerState
		 *            The initial power state.
		 * @param powerControlSignal
		 *            The power control signal.
		 */
		public TestAbstractReaderModule(PowerState initialPowerState,
				ControlSignal<Boolean> powerControlSignal) {
			super(initialPowerState, powerControlSignal);

		}

	}

	/**
	 * A test implementation of an AbstractSuspendedPowerState.
	 * 
	 * @author John Olender - john@pramari.com
	 * @since <$INITIAL_VERSION$>
	 * @version <$CURRENT_VERSION$>
	 * 
	 */
	private class TestAbstractSuspendedPowerState extends
			AbstractSuspendedPowerState {

		/**
		 * @see org.rifidi.emulator.common.PowerState#resume(org.rifidi.emulator.common.PowerControllable)
		 */
		public void resume(PowerControllable pcObject) {
			/* Fake a power resume event */
			AbstractPowerModuleTest.this.powerResumeOccurred = true;

		}

		/**
		 * @see org.rifidi.emulator.common.PowerState#turnOff(org.rifidi.emulator.common.PowerControllable)
		 */
		public void turnOff(PowerControllable pcObject, Class callingClass) {
			/* Fake a power off event */
			AbstractPowerModuleTest.this.powerOffOccurred = true;

		}

	}

	/**
	 * The AbstractPowerModule under test for each test case.
	 */
	private AbstractPowerModule abstractPowerModule;

	/**
	 * The power control signal that will be used for testing and with the test
	 * AbstractPowerModule.
	 */
	private ControlSignal<Boolean> powerControlSignal;

	/**
	 * A flag variable to tell if a power off actually occurred.
	 */
	private boolean powerOffOccurred;

	/**
	 * A flag variable to tell if a power on actually occurred.
	 */
	private boolean powerOnOccurred;

	/**
	 * A flag variable to tell if a power resume actually occurred.
	 */
	private boolean powerResumeOccurred;

	/**
	 * A flag variable to tell if a power suspend actually occurred.
	 */
	private boolean powerSuspendOccurred;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		/* Reset flag variables */
		this.powerOffOccurred = false;
		this.powerOnOccurred = false;
		this.powerResumeOccurred = false;
		this.powerSuspendOccurred = false;
		this.powerControlSignal = new ControlSignal<Boolean>(false);

	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.abstractPowerModule = null;
		this.powerControlSignal.deleteObservers();
		this.powerControlSignal = null;

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#AbstractReaderModule(PowerState, ControlSignal)}.
	 */
	public void testAbstractPowerModule() {
		/* Create a test abstract reader module */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOffPowerState(), this.powerControlSignal);

		/* Verify that it was created successfully */
		Assert.assertNotNull(this.abstractPowerModule);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#getPowerState()}.
	 */
	public void testGetPowerState() {
		/* Create a test abstract reader module */
		PowerState powerState = new TestAbstractOffPowerState();
		this.abstractPowerModule = new TestAbstractReaderModule(powerState,
				this.powerControlSignal);

		/* Verify that the module in fact holds the passed state. */
		Assert
				.assertTrue(this.abstractPowerModule.getPowerState() == powerState);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#resume()}.
	 */
	public void testResumeWhileOff() {
		/* Create a test abstract reader module which is off. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOffPowerState(), this.powerControlSignal);

		/* Attempt to resume */
		this.abstractPowerModule.resume();

		/* Make sure that a resume did not occur. */
		Assert.assertFalse(this.powerResumeOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#resume()}.
	 */
	public void testResumeWhileOn() {
		/* Create a test abstract reader module which is on. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOnPowerState(), this.powerControlSignal);

		/* Attempt to resume */
		this.abstractPowerModule.resume();

		/* Make sure that a resume did not occur. */
		Assert.assertFalse(this.powerResumeOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#resume()}.
	 */
	public void testResumeWhileSuspended() {
		/* Create a test abstract reader module which is suspended. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractSuspendedPowerState(), this.powerControlSignal);

		/* Attempt to resume */
		this.abstractPowerModule.resume();

		/* Make sure that a resume did occur. */
		Assert.assertTrue(this.powerResumeOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#suspend()}.
	 */
	public void testSuspendWhileOff() {
		/* Create a test abstract reader module which is off. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOffPowerState(), this.powerControlSignal);

		/* Attempt to suspend */
		this.abstractPowerModule.suspend();

		/* Make sure that a suspend did not occur. */
		Assert.assertFalse(this.powerSuspendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#suspend()}.
	 */
	public void testSuspendWhileOn() {
		/* Create a test abstract reader module which is on. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOnPowerState(), this.powerControlSignal);

		/* Attempt to suspend */
		this.abstractPowerModule.suspend();

		/* Make sure that a suspend did occur. */
		Assert.assertTrue(this.powerSuspendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#suspend()}.
	 */
	public void testSuspendWhileSuspended() {
		/* Create a test abstract reader module which is suspended. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractSuspendedPowerState(), this.powerControlSignal);

		/* Attempt to suspend */
		this.abstractPowerModule.suspend();

		/* Make sure that a suspend did not occur. */
		Assert.assertFalse(this.powerSuspendOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOff()}.
	 */
	public void testTurnOffWhileOff() {
		/* Create a test abstract reader module which is off. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOffPowerState(), this.powerControlSignal);

		/* Attempt to turnOff */
		this.abstractPowerModule.turnOff(this.getClass());

		/* Make sure that a power off did not occur. */
		Assert.assertFalse(this.powerOffOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOff()}.
	 */
	public void testTurnOffWhileOn() {
		/* Create a test abstract reader module which is on. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOnPowerState(), this.powerControlSignal);

		/* Attempt to turnOff */
		this.abstractPowerModule.turnOff(this.getClass());

		/* Make sure that a power off did occur. */
		Assert.assertTrue(this.powerOffOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOff()}.
	 */
	public void testTurnOffWhileSuspended() {
		/* Create a test abstract reader module which is suspended. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractSuspendedPowerState(), this.powerControlSignal);

		/* Attempt to turnOff */
		this.abstractPowerModule.turnOff(this.getClass());

		/* Make sure that a power off did occur. */
		Assert.assertTrue(this.powerOffOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOn()}.
	 */
	public void testTurnOnWhileOff() {
		/* Create a test abstract reader module which is off. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOffPowerState(), this.powerControlSignal);

		/* Attempt to turnOn */
		this.abstractPowerModule.turnOn();

		/* Make sure that a power on did occur. */
		Assert.assertTrue(this.powerOnOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOn()}.
	 */
	public void testTurnOnWhileOn() {
		/* Create a test abstract reader module which is on. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractOnPowerState(), this.powerControlSignal);

		/* Attempt to turnOn */
		this.abstractPowerModule.turnOn();

		/* Make sure that a power on did not occur. */
		Assert.assertFalse(this.powerOnOccurred);

	}

	/**
	 * Test method for
	 * {@link org.rifidi.emulator.reader.module.abstract_.AbstractPowerModule#turnOn()}.
	 */
	public void testTurnOnWhileSuspended() {
		/* Create a test abstract reader module which is suspended. */
		this.abstractPowerModule = new TestAbstractReaderModule(
				new TestAbstractSuspendedPowerState(), this.powerControlSignal);

		/* Attempt to turnOn */
		this.abstractPowerModule.turnOn();

		/* Make sure that a power on did not occur. */
		Assert.assertFalse(this.powerOnOccurred);

	}

}
