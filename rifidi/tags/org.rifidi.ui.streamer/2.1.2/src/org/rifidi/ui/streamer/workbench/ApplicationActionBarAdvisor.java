package org.rifidi.ui.streamer.workbench;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction newAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	protected void makeActions(IWorkbenchWindow window) {
		// default Exit Action to shut down the Application
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		// default About Action providing information though the about menu in
		// the application
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		// New File Default action for opening wizards
		newAction = ActionFactory.NEW.create(window);
		newAction.setText("New Item...");
		newAction.setToolTipText("create a new item...");
		register(newAction);
	}

	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);

		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		// add the entry for the newConnectionAction to the "File"-Menu
		fileMenu.add(newAction);
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(exitAction);
		helpMenu.add(aboutAction);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillCoolBar(org.eclipse.jface.action.ICoolBarManager)
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		IToolBarManager testSuiteToolBar = new ToolBarManager(SWT.FLAT
				| SWT.RIGHT);
		testSuiteToolBar.add(newAction);
		coolBar.add(new ToolBarContributionItem(testSuiteToolBar,
				"org.rifidi.ui.streamer.toolbars.addremove"));
		coolBar.add(new ToolBarContributionItem(toolbar, "additions"));
	}

}
