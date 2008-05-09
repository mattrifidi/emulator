/*
 *  CableEditPart.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor.parts;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.rifidi.designer.entities.internal.CableEntity;
import org.rifidi.designer.rcp.gpioeditor.commands.CableDeleteCommand;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * EditPart for the cable.
 * 
 * @see CableEntity
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 13, 2008
 * 
 */
public class CableEditPart extends AbstractConnectionEditPart {
	/**
	 * Reference to the cabling service.
	 */
	private CablingService cablingService;

	/**
	 * Constructor.
	 */
	public CableEditPart(CableEntity cableEntity) {
		super();
		setModel(cableEntity);
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ConnectionEndpointEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ROLE,
				new ConnectionEditPolicy() {
					protected Command getDeleteCommand(GroupRequest request) {
						return new CableDeleteCommand((CableEntity) getModel(),
								cablingService);
					}
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		PolylineConnection connection = (PolylineConnection) super
				.createFigure();
		connection.setTargetDecoration(new PolygonDecoration());
		connection.setLineStyle(Graphics.LINE_SOLID);
		return connection;
	}

	/**
	 * @param cablingService the cablingService to set
	 */
	@Inject
	public void setCablingService(CablingService cablingService) {
		this.cablingService = cablingService;
	}

}