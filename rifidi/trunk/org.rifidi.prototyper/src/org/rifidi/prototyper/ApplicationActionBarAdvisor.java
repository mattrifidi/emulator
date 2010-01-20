package org.rifidi.prototyper;

import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		IWorkbenchAction saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);
		IWorkbenchAction saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface
	 * .action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolBar = new ToolBarManager(coolBar.getStyle());
		toolBar.add(super.getAction(ActionFactory.SAVE.getId()));
		toolBar.add(super.getAction(ActionFactory.SAVE_AS.getId()));
		coolBar.add(toolBar);
	}

}
