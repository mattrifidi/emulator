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
package org.rifidi.designer.rcp.handlers.scenedata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Handler for loading a SceneData from a file.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class LoadSceneDataHandler extends AbstractHandler {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LoadSceneDataHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		logger.info("Starting load wizard.");
		Shell shell = new Shell(Display.getCurrent().getActiveShell(),
				SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.BORDER);
		new SceneDataManagementeDialog(shell);
		shell.setSize(400, 300);
		shell.open();
		return null;
	}

}
