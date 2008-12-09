/*
 *  IpAddressValidator.java
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

import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * FIXME: class comment!
 * 
 * 
 * @author Jochen Mader
 */
public class IpAddressValidator implements ICellEditorValidator {
	private static Pattern ipaddress=Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	private static Log logger=LogFactory.getLog(IpAddressValidator.class);
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if(value==null){
			return("Invalid IP Address");
		}
		if(value.getClass().equals(String.class)){
			String[] parts=((String)value).split(":");
			Integer port=null;
			if (!ipaddress.matcher(parts[0]).matches())
				return("Invalid IP Address");
			if (parts.length<2)
				return ("Port has to be between 0 and 60000");
			try{
				port=Integer.parseInt(parts[1]);
			}
			catch(NumberFormatException nfe){
				return ("Port has to be between 0 and 60000");
			}
			if(port<0 || port>60000){
				return ("Port has to be between 0 and 60000");
			}
			return null;
		}
		logger.error("Wrong value for this validator: "+value.getClass());
		return "Wrong value for this validator: "+value.getClass();
	}
	
	public String getTooltip(){
		return "<ipaddress>:<port>";
	}
}
