/*
 *  ResizeListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.listeners;

import java.util.concurrent.Callable;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

/**
 * Listener to adjust the frustum to the new size.
 * 
 * @author Jochen Mader Nov 1, 2007
 * @author Dan West - dan@pramari.com - 12/6/2007
 */
public class ResizeListener implements Listener {
	/**
	 * Reference to the camera service
	 */
	private CameraService cameraService;

	/**
	 * Default constructor
	 */
	public ResizeListener() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */

	@Override
	public void handleEvent(final Event event) {
		
		GameTaskQueueManager.getManager().render(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				if(((GLCanvas) event.widget).getSize().x>((GLCanvas) event.widget).getSize().y){
					DisplaySystem.getDisplaySystem().getRenderer().reinit(
							((GLCanvas) event.widget).getSize().x,
							(int)(((GLCanvas) event.widget).getSize().x*.8));
					return null;
				}
				DisplaySystem.getDisplaySystem().getRenderer().reinit(
						(int)(((GLCanvas) event.widget).getSize().y*.8),
						((GLCanvas) event.widget).getSize().y);
				return null;
			}

		});
	}

	/**
	 * @param cameraService the cameraService to set
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
	}
}
