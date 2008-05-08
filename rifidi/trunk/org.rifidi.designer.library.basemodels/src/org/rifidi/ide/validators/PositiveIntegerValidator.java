/*
 *  PositiveIntegerValidator.java
 *
 *  Created:	Apr 4, 2007
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
 * @author kyle
 * 
 */
public class PositiveIntegerValidator implements ICellEditorValidator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if (value != null) {
			Integer i;

			if (value instanceof String) {
				String s = (String) value;
				try {
					i = Integer.parseInt(s);
				} catch (NumberFormatException ex) {
					return "must be a valid Integer";
				}
			}

			else if (value instanceof Integer) {
				i = (Integer) value;
			}

			else {
				return "must be a valid Integer";
			}

			if (i < 0) {
				return " must be greater than or equal to 0";
			} else {
				return null;
			}
		}

		return "must be a valid Integer";
	}

}
