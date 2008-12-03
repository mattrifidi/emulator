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
import org.eclipse.swt.opengl.GLCanvas;
import org.rifidi.designer.rcp.game.ZoomableLWJGLCamera;

/**
 * Listener for zooming with the mouse wheel
 * 
 * @author dan
 * 
 */
public class ZoomMouseWheelListener implements MouseWheelListener {
	/**
	 * The zoomable camera.
	 */
	private ZoomableLWJGLCamera camera;

	/**
	 * Constructor.
	 */
	public ZoomMouseWheelListener(ZoomableLWJGLCamera camera) {
		this.camera = camera;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseWheelListener#mouseScrolled(org.eclipse.swt
	 * .events.MouseEvent)
	 */
	public void mouseScrolled(MouseEvent e) {
		if (e.count > 0) {
			camera.zoomIn(e.x, ((GLCanvas)e.widget).getSize().y - e.y);
			return;
		}
		camera.zoomOut(e.x, ((GLCanvas)e.widget).getSize().y - e.y);
	}
}