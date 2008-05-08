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
package org.rifidi.designer.rcp.handlers.cameras;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for enabling/disabling bounding box debugging.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class CameraRestoreHandler extends AbstractHandler {
	/**
	 * Reference to the camera service
	 */
	private CameraService cameraService;
	
	/**
	 * Construcotr
	 */
	public CameraRestoreHandler(){
		super();
		ServiceRegistry.getInstance().service(this);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		cameraService
				.loadRecordedCameraState(Integer
						.parseInt(arg0
								.getParameter("org.rifidi.designer.rcp.commands.cameras.camerarecording.number")));
		return null;
	}

	/**
	 * @param cameraService the cameraService to set
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
	}

}
