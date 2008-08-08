/*
 *  Handl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.rifidi.designer.ide.converter.views.View3d;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Aug 6, 2008
 * 
 */
public class Handl extends AbstractHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		((View3d)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(View3d.ID)).doSaveAs();
		System.out.println("boom");
		return null;
	}


}
