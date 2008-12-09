/*
 *  HexValidator.java
 *
 *  Created:	Apr 3, 2007
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
 * FIXME: Class comment!
 * 
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 *
 */
public class HexValidator implements ICellEditorValidator {

	public String isValid(Object value) {
		String hexString = (String)value;
		if(hexString.length()==0){
			return "Please enter a hex string greater than 0";
		}
		char[] chars = hexString.toCharArray();
		for(char c : chars){
			if(!isValidHexChar(c)){
				return c + " is not a valid hex character";
			}
		}
		return null;
	}
	
	private boolean isValidHexChar(char c){
		String validHexChars = "0123456789abcdef";
		String s = new String();
		s += c;
		s = s.toLowerCase();
		if(validHexChars.contains(s)){
			return true;
		}else return false;
	}

}
