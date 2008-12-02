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
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * An action filter that supports the property running.
 * 
 * @author Jochen Mader Nov 15, 2007
 * 
 */
public class SwitchActionFilterAdapter implements IActionFilter {
	/**
	 * The FinderService used by this filter.
	 */
	private FinderService finderService;

	/**
	 * Constructor
	 */
	public SwitchActionFilterAdapter() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof IHasSwitch && "running".equals(name)) {
			return value
					.equals(Boolean.toString(((IHasSwitch) target).isRunning()));
		}
		if (target instanceof Entity && "grouped".equals(name)) {
			return finderService.isEntityGrouped((Entity) target);
		}
		return false;
	}

	/**
	 * @param finderService the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}

}
