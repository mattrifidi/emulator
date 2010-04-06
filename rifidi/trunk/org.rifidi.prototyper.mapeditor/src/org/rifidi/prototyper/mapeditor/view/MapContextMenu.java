/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import org.rifidi.prototyper.mapeditor.handler.RemoveContainerAction;

/**
 * The object that provides a context menu for the map.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapContextMenu extends ContextMenuProvider {

	/**The registry of actions*/
	private ActionRegistry registry;

	/**
	 * @param viewer
	 */
	public MapContextMenu(EditPartViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		this.registry = actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface
	 * .action.IMenuManager)
	 */
	@Override
	public void buildContextMenu(IMenuManager menu) {
		menu.add(new RemoveContainerAction());
	}

}
