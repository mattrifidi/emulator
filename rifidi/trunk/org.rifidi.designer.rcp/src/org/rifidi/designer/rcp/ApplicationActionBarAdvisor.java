package org.rifidi.designer.rcp;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * Main actionbaradvisor.
 * 
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
	private IContributionItem cont;
	/**
	 * Constructor.
	 * 
	 * @param configurer
	 *            configurer for this actionbar
	 */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Create all actions and register them with the actionregistry.
	 * 
	 * @see ActionRegistry
	 */
	protected void makeActions(IWorkbenchWindow window) {
		// TODO: only required till 3.3, should work without this in 3.4
		register(ActionFactory.QUIT.create(window));
	}

	/**
	 * Fill the menubar.
	 */
	protected void fillMenuBar(IMenuManager menuBar) {
	}

}
