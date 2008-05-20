/*
 *  CameraRotateleftAction.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.rcp.views.view3d.View3D;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Action to rotate the camera to the left.
 * 
 * @author Dan West
 */
public class CameraRotateLeftAction extends Action {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(CameraRotateLeftAction.class);
	/**
	 * Eclipse ID
	 */
	public static final String ID = "org.rifidi.designer.rcp.views.view3d.actions.CameraRotateLeftAction";

	/**
	 * Constructor.
	 * 
	 * @param text
	 * @param image
	 */
	public CameraRotateLeftAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ID);
	}

	@Override
	public void run() {
		// if (view3D == null) {
		// view3D = (View3D) PlatformUI.getWorkbench()
		// .getActiveWorkbenchWindow().getActivePage().findView(
		// View3D.ID);
		// }
		// rotateCamera(view3D.getCamera(), interval);
		// showHideWalls(view3D);
		logger.info("should this be enabled");
	}

	/**
	 * Rotates the camera the given amount
	 * 
	 * @param cam
	 *            the camera to rotate
	 * @param theta
	 *            the amount to rotate
	 */
	public static void rotateCamera(Camera cam, float theta) {
		Quaternion q = new Quaternion();
		q.fromAngleAxis(theta, Vector3f.UNIT_Y);
		q.toRotationMatrix(new Matrix3f()).multLocal(cam.getDirection());
		cam.update();
	}

	/**
	 * Shows or hides blocking/nonblocking walls
	 * 
	 * @param camera
	 */
	public static void showHideWalls(View3D view3d, Camera camera) {

		// show all walls
		view3d.showWall(Direction.NORTH);
		view3d.showWall(Direction.SOUTH);
		view3d.showWall(Direction.EAST);
		view3d.showWall(Direction.WEST);

		// hide east/west wall
		if (camera.getDirection().x > 0)
			view3d.hideWall(Direction.WEST);
		else if (camera.getDirection().x < 0)
			view3d.hideWall(Direction.EAST);

		// hide north/south wall
		if (camera.getDirection().z < 0)
			view3d.hideWall(Direction.SOUTH);
		else if (camera.getDirection().z > 0)
			view3d.hideWall(Direction.NORTH);
	}
}