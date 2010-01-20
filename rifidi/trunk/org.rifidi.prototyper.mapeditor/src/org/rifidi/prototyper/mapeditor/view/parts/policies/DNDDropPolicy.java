/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.prototyper.mapeditor.model.commands.HotspotCreateCommand;
import org.rifidi.prototyper.mapeditor.model.commands.ItemCreateCommand;
import org.rifidi.prototyper.mapeditor.view.DNDElementFactory;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class DNDDropPolicy extends AbstractEditPolicy {

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
			return getHost();
		}
		return super.getTargetEditPart(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#getCommand(org.eclipse
	 * .gef.Request)
	 */
	@Override
	public Command getCommand(Request request) {
		if (request instanceof CreateRequest) {
			CreateRequest cr = (CreateRequest) request;
			MapModel mapModel = (MapModel) getHost().getModel();
			if (cr.getNewObjectType().equals(DNDElementFactory.ITEM)) {
				return new ItemCreateCommand(mapModel.getItems(), cr);
			} else if (cr.getNewObjectType().equals(DNDElementFactory.READER)) {
				return new HotspotCreateCommand(mapModel.getHotspots(), cr);
			}
		}
		return super.getCommand(request);
	}
	

}
