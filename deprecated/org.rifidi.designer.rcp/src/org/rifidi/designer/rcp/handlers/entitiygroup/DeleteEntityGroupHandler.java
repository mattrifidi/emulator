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
package org.rifidi.designer.rcp.handlers.entitiygroup;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for deleting the entity groups that are currently selected in the
 * selection provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class DeleteEntityGroupHandler extends AbstractHandler {
	private EntitiesService entitiesService;

	/**
	 * Constructor.
	 */
	public DeleteEntityGroupHandler() {
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
		while (iterator.hasNext()) {
			entitiesService.removeEntityGroup((EntityGroup) iterator.next());
		}
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
