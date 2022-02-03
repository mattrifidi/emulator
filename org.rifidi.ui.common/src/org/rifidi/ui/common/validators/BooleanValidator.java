/*
 *  ComPortValidator.java
 *
 *  Created:	Apr 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.validators;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * This validator should never be used.
 * It is just here because the framework requires a validator for everything.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class BooleanValidator implements ICellEditorValidator {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if(value instanceof Boolean){
			return null;
		}
		return "Please enter a valid boolean";
	}

}
