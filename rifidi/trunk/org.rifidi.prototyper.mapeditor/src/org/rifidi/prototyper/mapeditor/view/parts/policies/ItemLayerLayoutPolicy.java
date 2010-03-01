/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts.policies;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;
import org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.commands.DeleteCommand;
import org.rifidi.prototyper.mapeditor.model.commands.ItemMoveCommand;
import org.rifidi.prototyper.mapeditor.model.commands.OrphanItemCommand;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemLayerLayoutPolicy extends XYLayoutEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#getTargetEditPart(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (request instanceof CreateRequest) {
			// handled by DNDDropPolicy
			return null;
		}
		if (request instanceof ChangeBoundsRequest) {
			ChangeBoundsRequest req = (ChangeBoundsRequest) request;
			for (Object part : req.getEditParts()) {
				if (part instanceof HotspotPart) {
					return null;
				}
			}
		}
		return super.getTargetEditPart(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org
	 * .eclipse.gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		// handled by DNDDropPolicy
		return UnexecutableCommand.INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand
	 * (org.eclipse.gef.requests.ChangeBoundsRequest, org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(
			ChangeBoundsRequest request, EditPart child, Object constraint) {
		if (child instanceof ItemPart) {
			ItemPart part = (ItemPart) child;
			Rectangle r = (Rectangle) constraint;
			ItemPart containerPart = ((ItemLayerPart) getHost())
					.getIntersectionItem(part, r);
			if (containerPart != null) {
				if (containerPart.getModelElement().isContainer()) {
					// return new ItemInsertIntoContainerCommand(containerPart
					// .getModelElement(), part.getModelElement(),
					// ((ItemLayerPart) getHost()).getModelElement());
				}
			}
			return new ItemMoveCommand(part.getModelElement(), request,
					(Rectangle) constraint);
		}
		return super.createChangeConstraintCommand(request, child, constraint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new ItemResizableEditPolicy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand
	 * (org.eclipse.gef.Request)
	 */
	@Override
	protected Command getDeleteDependantCommand(Request request) {
		GroupRequest gRequest = (GroupRequest) request;
		List<AbstractMapModelElement> elements = new LinkedList<AbstractMapModelElement>();
		for (Object o : gRequest.getEditParts()) {
			if (o instanceof ItemPart) {
				elements.add(((ItemPart) o).getModelElement());
			}
		}
		ElementSet layerModel = ((ItemLayerPart) getHost()).getModelElement();
		return new DeleteCommand(layerModel, elements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getOrphanChildrenCommand
	 * (org.eclipse.gef.Request)
	 */
	@Override
	protected Command getOrphanChildrenCommand(Request request) {
		GroupRequest gRequest = (GroupRequest) request;
		ItemPart item = null;
		for (Object o : gRequest.getEditParts()) {
			if (o instanceof ItemPart) {
				item = (ItemPart) o;
			}
		}
		if (item != null)
			return new OrphanItemCommand(item, (ItemLayerPart)getHost());
		return null;
	}

}
