/*
 *  Initializer.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.cable;

import org.rifidi.designer.entities.internal.CableEntity;
import org.rifidi.designer.services.core.cabling.CablingService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.initializer.IInitializer;
import org.rifidi.services.initializer.exceptions.InitializationException;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public class Initializer implements IInitializer {

	private CablingService cablingService;
	
	/**
	 * Constructor.
	 */
	public Initializer() {
		ServiceRegistry.getInstance().service(this);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.services.initializer.IInitializer#init(java.lang.Object)
	 */
	@Override
	public void init(Object initializee) throws InitializationException {
		if(initializee instanceof CableEntity){
			cablingService.recreateCable((CableEntity) initializee);
			return;
		}
		throw new InitializationException(initializee+" not supported by this initializer.");
	}

	/**
	 * @param cablingService the cablingService to set
	 */
	@Inject
	public void setCablingService(CablingService cablingService) {
		this.cablingService = cablingService;
	}

}
