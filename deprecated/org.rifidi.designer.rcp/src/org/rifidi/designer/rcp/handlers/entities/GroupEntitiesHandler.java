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
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.rcp.views.view3d.wizards.EntityGroupWizard;

/**
 * Create a group with the entities selected in the selection provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class GroupEntitiesHandler extends AbstractHandler {
	/**
	 * Constructor.
	 */
	public GroupEntitiesHandler() {
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
		ArrayList<Entity> entities = new ArrayList<Entity>();
		while (iterator.hasNext()) {
			entities.add((Entity) iterator.next());
		}
		Wizard wizard = new EntityGroupWizard(entities);
		WizardDialog dialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		dialog.open();
		return null;
	}

}
