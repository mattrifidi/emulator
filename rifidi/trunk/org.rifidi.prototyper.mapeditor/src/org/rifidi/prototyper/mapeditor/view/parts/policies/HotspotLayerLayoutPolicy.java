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
import org.rifidi.prototyper.mapeditor.model.commands.HotspotMoveCommand;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotPart;

/**
 * This EditPolicy is used for the HotspotLayer to assist with placing hotspots
 * on the layer.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotLayerLayoutPolicy extends XYLayoutEditPolicy {

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
			// Handled by DNDDropPolicy
			return null;
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
		if (child instanceof HotspotPart) {
			HotspotPart part = (HotspotPart) child;
			return new HotspotMoveCommand(part.getModelElement(), request,
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
			if (o instanceof HotspotPart) {
				elements.add(((HotspotPart) o).getModelElement());
			}
		}
		ElementSet layerModel = ((HotspotLayerPart) getHost())
				.getModelElement();
		return new DeleteCommand(layerModel, elements);
	}

}
