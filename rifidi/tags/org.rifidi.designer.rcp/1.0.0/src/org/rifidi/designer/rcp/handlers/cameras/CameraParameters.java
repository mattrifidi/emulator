/*
 *  CameraParameters.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.cameras;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 6, 2008
 * 
 */
public class CameraParameters implements IParameterValues {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IParameterValues#getParameterValues()
	 */
	@Override
	public Map getParameterValues() {
		Map values=new HashMap();
		values.put("number", null);
		return values;
	}

}
