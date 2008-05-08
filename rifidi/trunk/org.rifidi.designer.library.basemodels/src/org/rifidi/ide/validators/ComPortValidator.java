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
package org.rifidi.ide.validators;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * 
 * @author Jochen Mader
 * 
 */
public class ComPortValidator implements ICellEditorValidator {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if(value.getClass().equals(String.class)){
			if(((String)value).length()>1){
				return null;
			}
		}
		return "Please enter a valid com descriptor";
	}

}
