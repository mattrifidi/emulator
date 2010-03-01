/**
 * 
 */
package org.rifidi.prototyper.mapeditor.handler;

import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class RemoveContainerAction extends Action {

	private ItemPart itemToEmpty;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#getText()
	 */
	@Override
	public String getText() {
		return "Remove Contained Items";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		ISelection sel = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getSelectionService().getSelection();
		if (sel instanceof StructuredSelection) {
			StructuredSelection ssel = (StructuredSelection) sel;
			if (ssel.size() == 1
					&& (ssel.getFirstElement() instanceof ItemPart)) {
				ItemPart item = (ItemPart) ssel.getFirstElement();
				if (item.getModelElement().isContainer()
						&& item.getModelElement().getContainedItems().size() > 0){
					itemToEmpty = item;
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setType(RequestConstants.REQ_ORPHAN_CHILDREN);
		groupRequest.setEditParts(itemToEmpty.getChildren());
		itemToEmpty.performRequest(groupRequest);
	}
}
