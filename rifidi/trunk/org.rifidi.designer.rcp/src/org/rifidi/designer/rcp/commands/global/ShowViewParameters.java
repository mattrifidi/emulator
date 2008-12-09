/*
 *  ShowViewParameters.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.commands.global;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;

/**
 * FIXME: Class comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 7, 2008
 * 
 */
public class ShowViewParameters implements IParameterValues {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IParameterValues#getParameterValues()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map getParameterValues() {
		Map params=new HashMap();
		params.put("viewid", null);
		return params;
	}

}
