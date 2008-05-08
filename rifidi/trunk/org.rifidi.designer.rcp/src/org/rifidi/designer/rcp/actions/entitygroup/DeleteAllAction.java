/*
 *  DeleteAllAction.java
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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.grouping.EntityGroup;

/**
 * This action deletes all entities from a given group
 * 
 * @author Dan West - dan@pramari.com
 */
public class DeleteAllAction implements IActionDelegate {
	/**
	 * The selected group.
	 */
	private EntityGroup target;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		for (Entity ent : new ArrayList<Entity>(((EntityGroup) target)
				.getEntities())) {
			ent.destroy();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		target = (EntityGroup) ((IStructuredSelection) selection)
				.getFirstElement();
	}
}