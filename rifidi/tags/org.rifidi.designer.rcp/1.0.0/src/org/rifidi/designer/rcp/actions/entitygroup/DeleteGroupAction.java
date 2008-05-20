/*
 *  DeleteAction.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.actions.entitygroup;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Delete a set of entityGroups.
 * 
 * @see Entity
 * @author Jochen Mader Nov 26, 2007
 * 
 */
public class DeleteGroupAction implements IActionDelegate {

	/**
	 * List of selected groups.
	 */
	private List<EntityGroup> groups;
	/**
	 * Reference to the entities service.
	 */
	private EntitiesService entitiesService;

	/**
	 * Constructor.
	 */
	public DeleteGroupAction() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (groups != null && groups.size() > 0) {
			for (EntityGroup entityGroup : groups) {
				entitiesService.removeEntityGroup(entityGroup);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		groups = new ArrayList<EntityGroup>();
		Iterator<Object> iter = ((IStructuredSelection) selection).iterator();
		while (iter.hasNext()) {
			EntityGroup entityGroup = (EntityGroup) iter.next();
			groups.add(entityGroup);
		}
	}

	/**
	 * @param entitiesService the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}
}
