/*
 *  WorldPropertytester.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.propertytesters;

import org.eclipse.core.expressions.PropertyTester;
import org.rifidi.designer.services.core.world.CommandStateService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class WorldPropertytester extends PropertyTester {
	private CommandStateService commandStateService;

	/**
	 * Constructor.
	 */
	public WorldPropertytester() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		return (Boolean) commandStateService.isEnabled((String) args[0]);
	}

	/**
	 * @param commandStateService
	 *            the commandStateService to set
	 */
	@Inject
	public void setCommandStateService(CommandStateService commandStateService) {
		this.commandStateService = commandStateService;
	}

}
