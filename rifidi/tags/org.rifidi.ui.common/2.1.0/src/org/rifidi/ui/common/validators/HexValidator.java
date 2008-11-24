package org.rifidi.ui.common.validators;

import org.eclipse.jface.viewers.ICellEditorValidator;

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
