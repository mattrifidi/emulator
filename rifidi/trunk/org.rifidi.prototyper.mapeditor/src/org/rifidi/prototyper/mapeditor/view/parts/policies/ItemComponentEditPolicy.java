/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class ItemComponentEditPolicy extends ComponentEditPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy#getDeleteCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		//FORWARD REQUEST TO ITEM LAYER
		GroupRequest req = new GroupRequest(REQ_DELETE_DEPENDANT);
		req.setEditParts(request.getEditParts());
		req.setEditParts(getHost());
		return getHost().getParent().getCommand(req);
	}
	
}
