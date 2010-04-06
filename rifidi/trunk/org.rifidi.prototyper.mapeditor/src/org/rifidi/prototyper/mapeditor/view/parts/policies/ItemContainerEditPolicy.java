/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.rifidi.prototyper.mapeditor.model.commands.AddItemCommand;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * This is an edit policy for ItemParts that are containers.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemContainerEditPolicy extends ContainerEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org
	 * .eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ContainerEditPolicy#getAddCommand(org.eclipse
	 * .gef.requests.GroupRequest)
	 */
	@Override
	protected Command getAddCommand(GroupRequest request) {
		ItemPart part = null;
		for (Object o : request.getEditParts()) {
			if (o instanceof ItemPart) {
				part = (ItemPart) o;
			}
		}
		if (part != null)
			return new AddItemCommand(part, (ItemPart) getHost());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand
	 * (org.eclipse.gef.requests.GroupRequest)
	 */
	@Override
	protected Command getOrphanChildrenCommand(GroupRequest request) {
		return super.getOrphanChildrenCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#getTargetEditPart(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (request.getType().equals(REQ_ADD)) {
			return getHost();
		}
		if (request.getType().equals(REQ_ORPHAN_CHILDREN)) {
			return getHost();
		}
		return super.getTargetEditPart(request);
	}

}
