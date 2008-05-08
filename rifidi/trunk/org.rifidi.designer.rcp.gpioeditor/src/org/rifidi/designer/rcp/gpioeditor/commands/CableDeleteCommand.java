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
import org.rifidi.designer.entities.CableEntity;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Command for deleting a cable.
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 13, 2008
 * 
 */
public class CableDeleteCommand extends Command {
	/**
	 * The cable we are using.
	 */
	private CableEntity cable;
	/**
	 * Reference to the cabling service.
	 */
	private CablingService cablingService;

	/**
	 * 
	 * @param source
	 * @param cablingService
	 */
	public CableDeleteCommand(CableEntity cable, CablingService cablingService) {
		this.cablingService = cablingService;
		if (cable == null) {
			throw new IllegalArgumentException();
		}
		setLabel("connection deletion");
		this.cable = cable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		cablingService.destroyCable(cable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		cablingService.destroyCable(cable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		cablingService.createCable(cable);
	}
}
