/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.RetargetAction;

/**
 * This class contributes actions to the various menu bars
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapEditorActionBarContributor extends ActionBarContributor {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	@Override
	protected void buildActions() {

		addRetargetAction(new ZoomInRetargetAction());
		addRetargetAction(new ZoomOutRetargetAction());

		addRetargetAction(new RetargetAction(
				GEFActionConstants.TOGGLE_GRID_VISIBILITY, "Edit Mode",
				IAction.AS_CHECK_BOX));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	@Override
	protected void declareGlobalActionKeys() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(org.eclipse
	 * .jface.action.IMenuManager)
	 */
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
		menuManager.findMenuUsingPath("Edit").add(
				getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
		menuManager.findMenuUsingPath("View").add(
				getAction(GEFActionConstants.ZOOM_IN));
		menuManager.findMenuUsingPath("View").add(
				getAction(GEFActionConstants.ZOOM_OUT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org
	 * .eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void contributeToToolBar(IToolBarManager tbm) {

		tbm.add(getAction(GEFActionConstants.ZOOM_IN));
		tbm.add(getAction(GEFActionConstants.ZOOM_OUT));
		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL,
				ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };
		tbm.add(new ZoomComboContributionItem(getPage(), zoomStrings));
		tbm.add(new Separator());
		tbm.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
	}

}
