/*
 *  CreateMultipleTagsHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.views.tags.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.views.tags.wizard.MultipleNewTagsWizard;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 19, 2008
 * 
 */
public class CreateMultipleTagsHandler extends AbstractHandler {

	private IRifidiTagService tagService;

	/**
	 * 
	 */
	public CreateMultipleTagsHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		MultipleNewTagsWizard wizard = new MultipleNewTagsWizard();
		WizardDialog wizardDialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		wizardDialog.open();
		if(wizard.getPattern()!=null){
			tagService.createTags(wizard.getPattern());	
		}
		return null;
	}

	/**
	 * @param tagService
	 *            the tagService to set
	 */
	@Inject
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

}
