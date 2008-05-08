package org.rifidi.designer.rcp;

import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "org.rifidi.designer.rcp.perspectives.designtime";

	public static IContextActivation activation;
	
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#postStartup()
	 */
	@Override
	public void postStartup() {
		// TODO Auto-generated method stub
		super.postStartup();
		IContextService contextService = (IContextService) PlatformUI
				.getWorkbench().getService(IContextService.class);
		activation=contextService.activateContext("org.rifidi.designer.rcp.designcontext");
	}

}
