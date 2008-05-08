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
package org.rifidi.designer.rcp.handlers.scenedata;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.services.core.scenedata.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for saving the loaded scene under a new name.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class SaveAsHandler extends AbstractHandler {
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;
	
	/**
	 * Constructor.
	 */
	public SaveAsHandler(){
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
		SaveAsWizard wizard = new SaveAsWizard();
		WizardDialog dialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		int ret = dialog.open();
		if (WizardDialog.CANCEL != ret) {
			sceneDataService.setName(wizard.getName());
			sceneDataService.saveScene(wizard.getNewLayout());
		}
		return null;
	}

	/**
	 * @param sceneDataService the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
	}

}
