/*
 *  GPIOEditPartFactory.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor.factory;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.GPO;
import org.rifidi.designer.entities.internal.CableEntity;
import org.rifidi.designer.rcp.gpioeditor.parts.CableEditPart;
import org.rifidi.designer.rcp.gpioeditor.parts.GPIEditPart;
import org.rifidi.designer.rcp.gpioeditor.parts.GPIOEditPart;
import org.rifidi.designer.rcp.gpioeditor.parts.GPOEditPart;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Factory for creating the different editparts.
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 12, 2008
 * 
 */
public class GPIOEditPartFactory implements EditPartFactory {

	/**
	 * Position counter for GPIs.
	 */
	private int gpicount = 0;
	/**
	 * Position counter for GPOs.
	 */
	private int gpocount = 0;

	/**
	 * 
	 */
	public GPIOEditPartFactory() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 *      java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof GPI && model instanceof GPO){
			return new GPIOEditPart((Entity)model, gpocount++);
		}
		else if (model instanceof GPI) {
			return new GPIEditPart((GPI) model, gpicount++);
		} else if (model instanceof GPO) {
			return new GPOEditPart((GPO) model, gpocount++);
		} else if (model instanceof CableEntity) {
			return new CableEditPart((CableEntity) model);
		}
		throw new RuntimeException("Missing EditPart for: "
				+ ((model != null) ? model.getClass().getName() : "null"));
	}

}
