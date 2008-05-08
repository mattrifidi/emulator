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
import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Property tester for EntityGroups.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 18, 2008
 * 
 */
public class EntityGroupPropertyTester extends PropertyTester {

	/**
	 * Constructor.
	 */
	public EntityGroupPropertyTester() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 *      java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if ("locked".equals(property)) {
			for (Object test : (Collection) receiver) {
				return ((EntityGroup) test).getLocked();
			}
		}
		return false;
	}

}
