/*
 *  RenameGroupAction.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.designer.rcp.actions.entitygroup;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.utilities.swt.RenameDialog;

/**
 * This action allows the renaming of an entitygroup.
 * 
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 *
 */
public class RenameGroupAction implements IActionDelegate {
	public EntityGroup target;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		if (target != null) {
			String name = target.getName();
			RenameDialog renamer = new RenameDialog(new Shell(), "Renaming "
					+ name + "...", name);

			String newname = renamer.open();
			if (newname != null)
				target.setName(newname);
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