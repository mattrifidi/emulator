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
package org.rifidi.designer.rcp.handlers.global;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.designer.rcp.ApplicationWorkbenchAdvisor;

/**
 * Handler for switching to the runtime perspective.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class OpenPerspectiveHandler extends AbstractHandler {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(OpenPerspectiveHandler.class);

	private IContextActivation runActivation;
	private IContextActivation designActivation;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		if (ApplicationWorkbenchAdvisor.activation != null) {
			designActivation = ApplicationWorkbenchAdvisor.activation;
			ApplicationWorkbenchAdvisor.activation = null;
		}
		try {
			PlatformUI
					.getWorkbench()
					.showPerspective(
							arg0
									.getParameter("org.rifidi.designer.rcp.commands.global.perspectiveid"),
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			logger.warn(e);
		}
		IContextService contextService = (IContextService) HandlerUtil
				.getActiveWorkbenchWindow(arg0).getWorkbench().getService(
						IContextService.class);
		if (arg0.getParameter(
				"org.rifidi.designer.rcp.commands.global.perspectiveid")
				.equals("org.rifidi.designer.rcp.perspectives.runtime")) {
			if (designActivation != null) {
				contextService.deactivateContext(designActivation);
			}
			// prevent double activation
			if (!contextService.getActiveContextIds().contains(
					"org.rifidi.designer.rcp.runtimecontext")) {
				runActivation = contextService
						.activateContext("org.rifidi.designer.rcp.runtimecontext");
			}
			HandlerUtil.getActiveWorkbenchWindow(arg0).getActivePage()
					.setEditorAreaVisible(false);

		} else if (arg0.getParameter(
				"org.rifidi.designer.rcp.commands.global.perspectiveid")
				.equals("org.rifidi.designer.rcp.perspectives.designtime")) {
			if (!contextService.getActiveContextIds().contains(
					"org.rifidi.designer.rcp.designcontext")) {
				designActivation = contextService
						.activateContext("org.rifidi.designer.rcp.designcontext");
			}
			if (runActivation != null) {
				contextService.deactivateContext(runActivation);
			}
			HandlerUtil.getActiveWorkbenchWindow(arg0).getActivePage()
					.setEditorAreaVisible(false);
		}
		return null;
	}
}
