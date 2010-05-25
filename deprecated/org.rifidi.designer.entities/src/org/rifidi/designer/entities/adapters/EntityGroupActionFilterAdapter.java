/*
 *  SwitchActionFilterAdapter.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.adapters;

import org.eclipse.ui.IActionFilter;
import org.rifidi.designer.entities.grouping.EntityGroup;

/**
 * An action filter that supports the property "running".
 * 
 * @author Jochen Mader Nov 15, 2007
 * 
 */
public class EntityGroupActionFilterAdapter implements IActionFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(final Object target, final String name,
			final String value) {
		if (target instanceof EntityGroup) {
			if ("locked".equals(name)) {
				return value.equals(Boolean.toString(((EntityGroup) target)
						.getLocked()));
			}
			if ("hasSwitchables".equals(name)) {
				return value.equals(Boolean.toString(((EntityGroup) target)
						.hasSwitchables()));
			}
		}
		return false;
	}

}
