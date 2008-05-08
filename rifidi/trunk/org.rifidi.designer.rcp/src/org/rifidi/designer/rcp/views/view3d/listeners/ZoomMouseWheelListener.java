/*
 *  ZoomMouseWheelListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.listeners;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.rifidi.designer.services.core.camera.CameraService;

/**
 * Listener for zooming with the mouse wheel
 * 
 * @author dan
 * 
 */
public class ZoomMouseWheelListener implements MouseWheelListener {
	/**
	 * Reference to the 3d view.
	 */
	private CameraService cameraService;

	/**
	 * Constructor.
	 * @param cameraService
	 * 			  the current camera service.
	 */
	public ZoomMouseWheelListener(CameraService cameraService) {
		this.cameraService=cameraService;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseWheelListener#mouseScrolled(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseScrolled(MouseEvent e) {
		if(e.count>0){
			cameraService.zoomIn();
			return;
		}
		cameraService.zoomOut();
	}
}