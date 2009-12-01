/*
 *  ReaderNameValidator.java
 *
 *  Created:	Apr 4, 2007
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
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class ReaderNameValidator implements ICellEditorValidator {
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if(value==null || ((String)value).length()==0){
			return "Please enter a reader name";
		}
		return null;
	}

}
