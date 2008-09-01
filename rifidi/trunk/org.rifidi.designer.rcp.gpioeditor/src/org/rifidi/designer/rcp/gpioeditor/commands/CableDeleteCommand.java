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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.gef.commands.Command;
import org.rifidi.designer.entities.internal.CableEntity;
import org.rifidi.designer.services.core.cabling.CablingService;

/**
 * Command for deleting a cable.
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 13, 2008
 * 
 */
public class CableDeleteCommand extends Command {
	/**
	 * logger for this class.
	 */
	private static final Log logger=LogFactory.getLog(CableDeleteCommand.class);
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
		logger.debug("Deleting cable: "+cable);
		cablingService.destroyCable(cable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		logger.debug("Deleting cable: "+cable);
		cablingService.destroyCable(cable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		logger.debug("Recreating Cable: "+cable);
		cablingService.createCable(cable);
	}
}
