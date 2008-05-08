/*
 *  CameraMoveListener.java
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
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.camera.CameraService;

/**
 * Listener to move the camera around.
 * 
 * @author Dan West
 */
public class CameraMoveListener implements MouseListener, MouseMoveListener {
	/**
	 * Reference to the camera service
	 */
	private CameraService cameraService;
	/**
	 * Reference to the view3d (used for cursor and wall hiding)
	 */
	private View3D view3d;
	/**
	 * Used to
	 */
	private boolean active = false;
	/**
	 * Ignore or monitor mouse move events.
	 */
	private boolean ignoreMouseMove = true;
	/**
	 * Sensitivity for moving the camera.
	 */
	private float sensitivity = 300.0f;
	/**
	 * The center of movement. USed to calculate the movement delta.
	 */
	private Point center;

	/**
	 * Default constructor
	 */
	public CameraMoveListener(View3D view3d, CameraService cameraService) {
		this.cameraService = cameraService;
		this.view3d = view3d;
	}

	/**
	 * Hide the mouse courser.
	 */
	public void hideCursor() {
		// calculate screen center and hide cursor
		Rectangle bounds = Display.getCurrent().getActiveShell().getBounds();
		center = new Point(bounds.x + bounds.width / 2, bounds.y
				+ bounds.height / 2);
		Display.getCurrent().setCursorLocation(center);
		view3d.hideMousePointer();
	}

	/**
	 * Show hte mouse courser.
	 */
	public void showCursor() {
		view3d.showMousePointer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if (!ignoreMouseMove && active) {

			// calculate how far to move the object(s)
			Point loc = Display.getCurrent().getCursorLocation();
			final float theta = ((float) loc.x - (float) center.x)
					/ sensitivity;
			final float phi = ((float) loc.y - (float) center.y) / sensitivity;

			// perform rotation and update walls
//			cameraService.getActiveCamera().rotate(phi, theta);
			view3d.showHideWalls();

			// recenter the cursor
			Display.getCurrent().setCursorLocation(center);
			ignoreMouseMove = true;
		} else if (ignoreMouseMove == true) {
			ignoreMouseMove = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			hideCursor();
			active = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if (e.button == 1) {
			active = false;
			showCursor();
		}
	}
}