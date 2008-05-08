/*
 *  NewSceneDataHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.scenecontrol;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.selection.SelectionServiceImpl;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for starting the scene.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class StartHandler extends AbstractHandler {
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the world service.
	 */
	private WorldService worldService;
	
	/**
	 * Constructor.
	 */
	public StartHandler(){
		super();
		ServiceRegistry.getInstance().service(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		worldService.start();
		// TODO: this is sooooo ugly. Revisit in eclipse 3.4!
		((SelectionServiceImpl) selectionService).triggerSelection();
		return null;
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
	/**
	 * @param worldService the worldService to set
	 */
	@Inject
	public void setWorldService(WorldService worldService) {
		this.worldService = worldService;
	}

}