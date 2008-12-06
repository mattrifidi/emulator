/* 
 * AddTagHandler.java
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.ide.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.ui.common.wizards.tag.MultipleNewTagsWizard;
import org.rifidi.ui.ide.views.tagview.TagView;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class AddTagHandler extends AbstractHandler {


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart view = (IViewPart) HandlerUtil
				.getActiveWorkbenchWindow(event).getActivePage().findView(
						TagView.ID);
		if (view != null) {
			MultipleNewTagsWizard wizard = new MultipleNewTagsWizard();
			WizardDialog wizardDialog = new WizardDialog(view.getSite()
					.getShell(), wizard);
			wizardDialog.open();
			((TagView) view).refresh();
		}
		return null;
	}

}
