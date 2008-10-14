package org.rifidi.ui.ide.workbench;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.rifidi.ui.ide.workbench.actions.ConnectionSettingsAction;

/**
 * Eclipse generated file, defining the ActionBars in the Workbench window. It's
 * adding the main menu and the toolbar.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;

	private ConnectionSettingsAction newConnectionAction;

	/**
	 * Default Constructor
	 * 
	 * @param configurer
	 */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#makeActions(org.eclipse.ui.IWorkbenchWindow)
	 */
	protected void makeActions(IWorkbenchWindow window) {
		// default Exit Action to shut down the Application 
		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		// default About Action providing information though the about menu in the application
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		// menu action to change the RMI Connection to another server
		newConnectionAction = new ConnectionSettingsAction(
				"&connect to remote", null);
		register(newConnectionAction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillMenuBar(org.eclipse.jface.action.IMenuManager)
	 */
	protected void fillMenuBar(IMenuManager menuBar) {
		MenuManager fileMenu = new MenuManager("&File",
				IWorkbenchActionConstants.M_FILE);

		MenuManager helpMenu = new MenuManager("&Help",
				IWorkbenchActionConstants.M_HELP);

		// add the entry for the newConnectionAction to the "File"-Menu
		fileMenu.add(newConnectionAction);
		fileMenu.add(new GroupMarker("menuActions"));
		fileMenu.add(exitAction);

		helpMenu.add(aboutAction);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.application.ActionBarAdvisor#fillStatusLine(org.eclipse.jface.action.IStatusLineManager)
	 */
	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		statusLine.setMessage("RIFIDI Emulator Started");
		statusLine.update(true);
	}

}
