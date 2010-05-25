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
package org.rifidi.designer.rcp.handlers.tools;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.rcp.views.view3d.View3D.Mode;

/**
 * Handler for enabling the watch area drawing tool.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class MoveToolHandler extends AbstractHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		View3D view3D = (View3D) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().findView(View3D.ID);
		view3D.switchMode(Mode.MoveMode);
		// if (!Mode.MoveMode.equals(view3D.getCurrentMode())) {
		//			
		// return null;
		// }
		// view3D.switchMode(Mode.CameraMode);
		return null;
	}

}
