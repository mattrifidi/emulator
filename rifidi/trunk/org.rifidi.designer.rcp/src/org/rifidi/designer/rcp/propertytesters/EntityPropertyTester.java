/*
 *  EntityPropertyTester.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.propertytesters;

import java.util.Collection;

import org.eclipse.core.expressions.PropertyTester;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.interfaces.Switch;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 18, 2008
 * 
 */
public class EntityPropertyTester extends PropertyTester {

	/**
	 * Reference to the finder service.
	 */
	private FinderService finderService;

	/**
	 * Constructor.
	 */
	public EntityPropertyTester() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if ("running".equals(property)) {
			for (Object test : (Collection) receiver) {
				if (test instanceof Switch) {
					return ((Switch) test).isRunning();
				} else if (test instanceof EntityGroup) {
					return true;
				}
			}
		} else if ("grouped".equals(property)) {
			for (Object test : (Collection) receiver) {
				if (!finderService.isEntityGrouped((Entity) test)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * @param finderService
	 *            the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}

}
