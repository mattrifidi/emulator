/*
 *  CableCreateCommand.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.entities.internal.CableEntity;
import org.rifidi.designer.services.core.cabling.CablingService;

/**
 * Command for creating a cable.
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 13, 2008
 * 
 */
public class CableCreateCommand extends Command {
	/**
	 * The cable we are using.
	 */
	private CableEntity cable;
	/**
	 * GPO.
	 */
	private GPO source;
	/**
	 * GPI.
	 */
	private GPI target;
	/**
	 * Reference to the cabling service.
	 */
	private CablingService cablingService;

	/**
	 * Constructor.
	 * @param source
	 * @param cablingService
	 */
	public CableCreateCommand(GPO source, CablingService cablingService) {
		this.cablingService = cablingService;
		if (source == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection creation");
		this.source = source;	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		if (source.equals(target)) {
			return false;
		}
		if (cablingService.cableExists(new CableEntity(source, target))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		cable = new CableEntity();
		cable.setGpi((Entity)target);
		cable.setGpo((Entity)source);
		cablingService.createCable(cable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		cablingService.destroyCable(cable);
	}

	public void setTarget(GPI target) {
		if (target == null) {
			throw new IllegalArgumentException();
		}
		this.target = target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		cablingService.destroyCable(cable);
	}
}
