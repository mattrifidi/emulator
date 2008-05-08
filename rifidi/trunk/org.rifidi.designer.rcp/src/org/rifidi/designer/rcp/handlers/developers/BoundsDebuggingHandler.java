/*
 *  NewSceneDataHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.developers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rifidi.designer.rcp.GlobalProperties;

/**
 * Handler for enabling/disabling bounding box debugging.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class BoundsDebuggingHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		if (GlobalProperties.boundingDebugging) {
			GlobalProperties.boundingDebugging = false;
			return null;
		}
		GlobalProperties.boundingDebugging = true;
		return null;
	}

}
