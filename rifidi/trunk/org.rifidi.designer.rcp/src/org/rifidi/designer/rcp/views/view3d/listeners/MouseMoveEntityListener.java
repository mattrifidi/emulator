/*
 *  MouseMoveEntityListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;

/**
 * This listener moves objects around. The mouse pointer is replaced by an
 * invisible pointer and the mouse is moved to the center (somehat) of the
 * screen.
 * 
 * @author Jochen Mader Nov 1, 2007
 * @author Dan West
 */
public class MouseMoveEntityListener implements MouseMoveListener,
		MouseListener {
	/**
	 * Movement delta on the mouse x axis.
	 */
	float deltaX = 0;
	/**
	 * Movement delta on the mouse y axis.
	 */
	float deltaY = 0;
	/**
	 * Counter to keep track of how often we have rotated the object.
	 */
	private View3D view3D;
	/**
	 * Center of the scree/canvas/whatever, used to reposition the mousepointer
	 * after movement.
	 */
	private Point center;
	/**
	 * If the listener is first started it needs to ignore the first move (it's
	 * the repositioning).
	 */
	private boolean ignore = true;
	/**
	 * Mouse movement sensitivity.
	 */
	private float sensitivity = 5;
	/**
	 * Currently used scene.
	 */
	private SceneData sceneData;
	/**
	 * Current scene data service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Targets of the movement action.
	 */
	private Map<VisualEntity, Target> realTargets = new HashMap<VisualEntity, Target>();
	/**
	 * List of colliding visual entities.
	 */
	private List<VisualEntity> colliders;

	/**
	 * Indicator for whether an object is being moved.
	 */
	private boolean inPlacement = false;

	/**
	 * Reference to selection service currently in use.
	 */
	private SelectionService selectionService;

	/**
	 * Constructor.
	 * 
	 * @param view3D
	 *            reference to the 3d view
	 * @param selectionService
	 *            reference to the selection service.
	 */
	public MouseMoveEntityListener(View3D view3D,
			SelectionService selectionService, SceneDataService sceneDataService) {
		this.view3D = view3D;
		this.selectionService = selectionService;
		this.sceneDataService = sceneDataService;
	}

	/**
	 * Scans the currently hilited entities and stores their information in
	 * targetData for use during placement. Also centers the cursor for motion
	 * tracking.
	 */
	public void initPlacementMode() {

		// calculate center and move mouse there
		Rectangle bounds = Display.getCurrent().getActiveShell().getBounds();
		center = new Point(bounds.x + bounds.width / 2, bounds.y
				+ bounds.height / 2);
		Display.getCurrent().setCursorLocation(center);

		realTargets.clear();
		sceneData = sceneDataService.getCurrentSceneData();
		for (Entity target : selectionService.getSelectionList()) {
			if (target instanceof VisualEntity) {

				// store the data for this entity
				Target targetData = new Target(
						(Vector3f) ((VisualEntity) target).getNode()
								.getLocalTranslation().clone(), /* quadz, */
						new Quaternion(((VisualEntity) target).getNode()
								.getLocalRotation()));

				// add to listing of target info
				realTargets.put((VisualEntity) target, targetData);
			}
		}

		inPlacement = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if (!ignore && inPlacement) {

			// calculate how far to move the object(s)
			Point loc = Display.getCurrent().getCursorLocation();
			deltaX += ((float) loc.x - (float) center.x) / sensitivity;
			deltaY += ((float) loc.y - (float) center.y) / sensitivity;
			Vector3f up = Vector3f.UNIT_Z;
			Vector3f right = Vector3f.UNIT_X;

			Vector3f vec = up.mult((int) deltaY).add(right.mult((int) deltaX));
			vec.x = (int) vec.x;
			vec.z = (int) vec.z;

			// check if any objects would be out of bounds after moving this far
			for (VisualEntity target : realTargets.keySet()) {
				// Point curPos = target.getPositionFromTranslation();
				// int maxX = sceneData.getWidth()
				// - target.getPattern().getWidth();
				// int maxY = sceneData.getWidth()
				// - target.getPattern().getLength();

				// check x direction
				// if (curPos.x + vec.x > maxX || curPos.x + vec.x < 0)
				// vec.x = 0;

				// check y direction
				// if (curPos.y + vec.z > maxY || curPos.y + vec.z < 0)
				// vec.z = 0;
			}

			// remove whatever amount has been used for calculating motion this
			// round
			deltaY -= (int) deltaY;
			deltaX -= (int) deltaX;

			// apply the approved translation to each target
			for (VisualEntity target : realTargets.keySet()) {
				target.getNode().getLocalTranslation().addLocal(vec);

			}

			// recenter the cursor
			Display.getCurrent().setCursorLocation(center);
			ignore = true;
		} else if (ignore == true) {
			ignore = false;
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
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		// rotate the object
		// if (e.button == 3 && realTargets.size() == 1) {
		// for (VisualEntity ve : realTargets.keySet())
		// ve.rotateRight();
		// }
		if (e.button == 1) {
			drop();

			view3D.switchMode(View3D.Mode.PickMode);
			inPlacement = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		// drop it
		if (e.button == 1) {
			drop();

			view3D.switchMode(View3D.Mode.PickMode);
			inPlacement = false;
		}
	}

	/**
	 * Put the currently dragged entity on the map.
	 */
	public void drop() {
		for (VisualEntity target : realTargets.keySet()) {

			// activate physics handling for this node
			if (target.getNode() instanceof DynamicPhysicsNode) {
				((DynamicPhysicsNode) target.getNode())
						.setLinearVelocity(Vector3f.ZERO);
			}
		}
	}

	/**
	 * Move the currently selected entity back to where we started
	 */
	public void reset() {
		for (VisualEntity target : realTargets.keySet()) {
			target.getNode().setLocalTranslation(
					realTargets.get(target).translation);
			target.getNode().setLocalRotation(
					realTargets.get(target).quaternion);

			if (target.getNode() instanceof PhysicsNode) {
				// ((PhysicsNode) target.getNode()).setActive(true);
				if (target.getNode() instanceof DynamicPhysicsNode)
					((DynamicPhysicsNode) target.getNode())
							.setLinearVelocity(Vector3f.ZERO);
			}
		}
	}

	/**
	 * Helper for handling targets of a move action. Used to allow resetting of
	 * the movement after an invalid placement.
	 * 
	 * @author Jochen Mader Feb 1, 2008
	 * @tags
	 * 
	 */
	private class Target {
		/**
		 * Translation of the target.
		 */
		public Vector3f translation;
		/**
		 * Rotation quaternion.
		 */
		public Quaternion quaternion;

		/**
		 * Constructor.
		 * 
		 * @param translation
		 *            original translation
		 * @param originalPattern
		 *            original pattern
		 * @param quat
		 *            original rotation quaternion
		 */
		public Target(Vector3f translation, Quaternion quat) {
			super();
			this.translation = translation;
			this.quaternion = quat;
		}
	}
}