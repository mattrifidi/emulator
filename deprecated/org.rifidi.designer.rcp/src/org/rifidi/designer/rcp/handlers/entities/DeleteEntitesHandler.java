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
package org.rifidi.designer.rcp.handlers.entities;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for deleting the entites that are selected in the selection provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class DeleteEntitesHandler extends AbstractHandler {
	/**
	 * Reference to the entitiesService.
	 */
	private EntitiesService entitiesService;

	/**
	 * Constructor.
	 */
	public DeleteEntitesHandler() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		Iterator iterator = ((IStructuredSelection) HandlerUtil
				.getCurrentSelectionChecked(arg0)).iterator();
		ArrayList<Entity> delete = new ArrayList<Entity>();
		while (iterator.hasNext()) {
			delete.add((Entity) iterator.next());
		}
		entitiesService.deleteEntities(delete);
		return null;
	}

	/**
	 * @param entitiesService the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

}
