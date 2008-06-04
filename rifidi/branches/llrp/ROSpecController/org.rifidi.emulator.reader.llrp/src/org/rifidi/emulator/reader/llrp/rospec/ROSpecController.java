/*
 *  ROSpecController.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.llrp.rospec;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.rospec.execeptions.ROSpecControllerException;

import edu.uark.csce.llrp.ROSpec;

/**
 * This is a controller that manages all of the ROSpecs.
 * 
 * LLRP defines three states for a ROSpec to be in: 1)Disabled. 2)Inactive
 * 3)Active. When a ROSpec is added, it is in the Disabled Spec. An
 * ENABLE_ROSPEC message moves the ROSpec to the inactive state. Then it waits
 * for a start trigger to put it in the active (execution) state. When it is
 * executing, a stop trigger will move it back to the inactive state. A
 * DISABLE_ROSPEC Message moves it back to the Disabled state. Finally a
 * DELETE_ROSPEC message will remove it completely. See page 25 of the LLRP spec
 * for a more detailed description of how the state transitions work
 * 
 * It is possible for ROSpecs to have a priority so that ROSpecs with higher
 * priorities can preempt those with lower priorities. This feature is not yet
 * implemented.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Kyle Neumeier
 */
public class ROSpecController {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(ROSpecController.class);

	/**
	 * A map of every ROSpec that exists in this reader.
	 */
	private HashMap<Integer, _ROSpec> allROSpecs = new HashMap<Integer, _ROSpec>();

	/**
	 * The currently active ROSpec.
	 */
	private Integer activeSpec = null;

	/**
	 * This method adds a ROSpec to the LLRP reader.
	 * 
	 * @param ROSpecToAdd
	 *            The _ROSpec object to add
	 * @throws ROSpecControllerException
	 *             Throws an exception if a ROSpec with the supplied ROSpec id
	 *             already exists
	 */
	public void addROSpec(_ROSpec ROSpecToAdd) throws ROSpecControllerException {
		if (!this.allROSpecs.containsKey(ROSpecToAdd.getId())) {
			allROSpecs.put(ROSpecToAdd.getId(), ROSpecToAdd);
			ROSpecToAdd.toDisabledState();
		} else {
			throw new ROSpecControllerException("ROSpec with ID "
					+ ROSpecToAdd.getId() + " already added");
		}

	}

	/**
	 * This method moves the ROSpec from the disabled to the inactive state
	 * 
	 * @param ROSpecToEnable
	 *            The ROSpecID of the rospec to enable
	 * @throws ROSpecControllerException
	 *             Throws an exception if a rospec with the given Rospec ID is
	 *             not found or not in the Disabled state
	 */
	public void enableROSpec(int ROSpecToEnable)
			throws ROSpecControllerException {
		_ROSpec rospec = allROSpecs.get(ROSpecToEnable);
		if (rospec != null) {
			if (rospec.getCurrentState() == ROSpecState.DISABLED) {
				allROSpecs.get(ROSpecToEnable).toInactiveState();
			} else {
				throw new ROSpecControllerException(
						"Invalid State transition for ROSpec with ID "
								+ ROSpecToEnable);
			}
		} else {
			throw new ROSpecControllerException("ROSpec ID " + ROSpecToEnable
					+ " not found");
		}
	}

	/**
	 * This method starts the ROSpec. It is called either by the handler method
	 * for a START_ROSPEC message or by a trigger.
	 * 
	 * @param ROSpecToStart
	 *            The ROSpecID of the ROSpec to start
	 * @throws ROSpecControllerException
	 *             Throws an exception if a ROSpec with the given ROSpec ID is
	 *             not found or not in the inactive state
	 */
	public void startROSpec(int ROSpecToStart) throws ROSpecControllerException {
		// TODO: Build support for Priority
		if (this.allROSpecs.containsKey(ROSpecToStart)) {
			_ROSpec rospec = this.allROSpecs.get(ROSpecToStart);
			if (rospec.getCurrentState() == ROSpecState.INACTIVE) {
				if (this.activeSpec == null) {
					this.activeSpec = ROSpecToStart;
					rospec.toActiveState();
				}
			} else {
				throw new ROSpecControllerException(
						"Invalid state transition for ROSpec with ID "
								+ ROSpecToStart);
			}
		} else {
			throw new ROSpecControllerException("ROSpec ID " + ROSpecToStart
					+ " not found");
		}

	}

	/**
	 * This method stops a currently running ROSpec. It is called either by the
	 * handler method for a STOP_ROSPEC method or by a stop trigger.
	 * 
	 * @param ROSpecToStop
	 *            The ROSpec id of the ROSpec to stop
	 * @throws ROSpecControllerException
	 *             Throws an exception if a rospec with the given ID has not
	 *             been added or is not in the active state
	 */
	public void stopROSpec(int ROSpecToStop) throws ROSpecControllerException {
		_ROSpec rospec = this.allROSpecs.get(ROSpecToStop);
		if (rospec != null) {
			if (rospec.getCurrentState() == ROSpecState.ACTIVE) {
				this.activeSpec = null;
				rospec.toInactiveState();
			}
		}
	}

	/**
	 * This method moves a ROSpec to the disabled state. It is called by the
	 * handler method for the DISABLE_ROSPEC message.
	 * 
	 * @param ROSpecToDisable
	 *            The ROSpec ID of the ROSpec to disable
	 * @throws ROSpecControllerException
	 *             Throws and exception if a ROSpec with the given ID has not
	 *             been added or is not in either the Active or Inactive state
	 */
	public void disableROSpec(int ROSpecToDisable)
			throws ROSpecControllerException {
		_ROSpec rospec = this.allROSpecs.get(ROSpecToDisable);
		if (rospec != null) {
			if (rospec.getCurrentState() == ROSpecState.ACTIVE) {
				this.activeSpec = null;
				rospec.toDisabledState();
			} else if (rospec.getCurrentState() == ROSpecState.INACTIVE) {
				rospec.toDisabledState();
			} else {
				throw new ROSpecControllerException(
						"Invalid state transition for ROSpec with ID "
								+ ROSpecToDisable);
			}
		} else {
			throw new ROSpecControllerException("ROSpec ID " + ROSpecToDisable
					+ " not found");
		}
	}

	/**
	 * This method removes a ROSpec. It is called by the handler method for the
	 * DELETE_ROSPEC message.
	 * 
	 * @param ROSpecToDelete
	 *            The ID of the ROSpec to delete
	 * @throws ROSpecControllerException
	 *             Throws an exception if a ROSpec with the supplied ID has not
	 *             been added
	 */
	public void deleteROSpec(int ROSpecToDelete)
			throws ROSpecControllerException {
		_ROSpec rospec = this.allROSpecs.get(ROSpecToDelete);
		if (rospec != null) {
			if (rospec.getCurrentState() == ROSpecState.ACTIVE) {
				this.activeSpec = null;
			}
			rospec.delete();
		} else {
			throw new ROSpecControllerException("ROSpec ID " + ROSpecToDelete
					+ " not found");
		}

	}

	/**
	 * This method returns a list of all ROSpecs that have been added
	 * 
	 * @return
	 */
	public ArrayList<ROSpec> getAllRoSpecs() {
		ArrayList<ROSpec> roSpecs = new ArrayList<ROSpec>(allROSpecs.size());
		for (_ROSpec spec : allROSpecs.values()) {
			roSpecs.add(spec.toROSpec());
		}
		return roSpecs;
	}

}