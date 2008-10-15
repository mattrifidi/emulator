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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.highlighting.HighlightingService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.bounding.BoundingBox;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.shape.Box;
import com.jmex.physics.DynamicPhysicsNode;

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
	/** Movement delta on the mouse x axis. */
	float deltaX = 0;
	/** Movement delta on the mouse y axis. */
	float deltaY = 0;
	/** Counter to keep track of how often we have rotated the object. */
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
	/** Mouse movement sensitivity. */
	private float sensitivity = 5;
	/** Targets of the movement action. */
	private Map<VisualEntity, Target> realTargets = new HashMap<VisualEntity, Target>();
	/** List of colliding visual entities. */
	private Set<VisualEntity> colliders;
	/** List of moving entities that collide with the floorplan. */
	private Set<VisualEntity> collidesWithFloor = new HashSet<VisualEntity>();
	/** Indicator for whether an object is being moved. */
	private boolean inPlacement = false;

	/** Reference to selection service currently in use. */
	private SelectionService selectionService;

	/** Reference to the entities service. */
	private EntitiesService entitiesService;

	/** Reference to the highlightingservice. */
	private HighlightingService highlightingService;
	/** Used for collisionchecking in the octree. */
	private Set<VisualEntity> colls = new HashSet<VisualEntity>();

	/**
	 * Constructor.
	 * 
	 * @param view3D
	 *            reference to the 3d view
	 * @param selectionService
	 *            reference to the selection service.
	 * @param entitiesService
	 *            reference to the entities service
	 */
	public MouseMoveEntityListener(View3D view3D) {
		this.view3D = view3D;
		colliders = new HashSet<VisualEntity>();
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * Scans the currently hilited entities and stores their information in
	 * targetData for use during placement. Also centers the cursor for motion
	 * tracking.
	 */
	public void initPlacementMode() {
		collidesWithFloor.clear();
		colliders.clear();
		// calculate center and move mouse there
		Rectangle bounds = Display.getCurrent().getActiveShell().getBounds();
		center = new Point(bounds.x + bounds.width / 2, bounds.y
				+ bounds.height / 2);
		Display.getCurrent().setCursorLocation(center);

		realTargets.clear();
		for (Entity target : selectionService.getSelectionList()) {
			if (target instanceof VisualEntity) {

				entitiesService.getCollisionOctree().removeEntity(
						(VisualEntity) target);
				BoundingBox modelBound = (BoundingBox) ((VisualEntity) target)
						.getBoundingNode().getWorldBound();
				Box boxxy = null;
				if (modelBound.getCenter().y - modelBound.yExtent > 0.2) {
					boxxy = new Box("boom", new Vector3f(-0.1f, modelBound
							.getCenter().y, -0.1f), new Vector3f(0.1f, 0, 0.1f));
					((VisualEntity) target).getNode().attachChild(boxxy);
					((VisualEntity) target).getNode().updateRenderState();
				}

				// store the data for this entity
				Target targetData = new Target(
						(Vector3f) ((VisualEntity) target).getNode()
								.getLocalTranslation().clone(), new Quaternion(
								((VisualEntity) target).getNode()
										.getLocalRotation()), boxxy);
				// add to listing of target info
				realTargets.put((VisualEntity) target, targetData);
			}
		}

		inPlacement = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events
	 * .MouseEvent)
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

			// clear the set
			colls.clear();
			// find the ones that are colliding and remove the ones that stopped
			// colliding
			Set<VisualEntity> newColliders = new HashSet<VisualEntity>();
			Set<VisualEntity> removedColliders = new HashSet<VisualEntity>();
			for (VisualEntity target : realTargets.keySet()) {
				entitiesService.getCollisionOctree().findCollisions(target,
						colls);
			}
			for (VisualEntity res : colls) {
				if (!colliders.contains(res)) {
					newColliders.add(res);
				}
			}
			for (VisualEntity res : colliders) {
				if (!colls.contains(res)) {
					removedColliders.add(res);
				}
			}
			if (newColliders.size() > 0) {
				colliders.addAll(newColliders);
			}
			if (removedColliders.size() > 0) {
				colliders.removeAll(removedColliders);
			}

			if (newColliders.size() > 0 || removedColliders.size() > 0) {
				highlightingService
						.changeHighlighting(ColorRGBA.red, colliders);
			}

			Set<VisualEntity> collidesWithFloorNew = new HashSet<VisualEntity>();
			Set<VisualEntity> stoppedCollidingWithFloor = new HashSet<VisualEntity>();
			// check for collisions with the floorplan
			for (VisualEntity target : realTargets.keySet()) {
				if (entitiesService.collidesWithScene(target)) {
					if (!collidesWithFloor.contains(target)) {
						collidesWithFloorNew.add(target);
					}
				} else {
					if (collidesWithFloor.contains(target)) {
						stoppedCollidingWithFloor.add(target);
					}
				}
			}

			if (collidesWithFloorNew.size() > 0) {
				collidesWithFloor.addAll(collidesWithFloorNew);
				highlightingService.changeHighlightColor(ColorRGBA.blue,
						ColorRGBA.yellow, collidesWithFloorNew);
			}
			if (stoppedCollidingWithFloor.size() > 0) {
				collidesWithFloor.removeAll(stoppedCollidingWithFloor);
				highlightingService.changeHighlightColor(ColorRGBA.yellow,
						ColorRGBA.blue, stoppedCollidingWithFloor);
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

			// check if the selected geometries are still valid
			int count = 0;
			for (VisualEntity target : realTargets.keySet()) {
				if (target.isDeleted()) {
					count++;
				}
			}
			if (count == realTargets.size()) {
				view3D.switchMode(View3D.Mode.PickMode);
				inPlacement = false;
			}
		} else if (ignore == true) {
			ignore = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt
	 * .events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events
	 * .MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if (colliders.size() == 0 && collidesWithFloor.size() == 0
				&& e.button == 1) {
			System.out.println("dropping");
			drop();
			view3D.switchMode(View3D.Mode.PickMode);
			inPlacement = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		// drop it
		if (colliders.size() == 0 && collidesWithFloor.size() == 0
				&& e.button == 1) {
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
			if (!target.isDeleted()) {
				// activate physics handling for this node
				if (target.getNode() instanceof DynamicPhysicsNode) {
					((DynamicPhysicsNode) target.getNode())
							.setLinearVelocity(Vector3f.ZERO);
				}
				if (realTargets.get(target).heightindicator != null) {
					realTargets.get(target).heightindicator.removeFromParent();
				}
				entitiesService.getCollisionOctree().insertEntity(target);
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
		 * Height indicator.
		 */
		public Box heightindicator;

		/**
		 * Constructor.
		 * 
		 * @param translation
		 *            original translation
		 * @param quat
		 *            original rotation quaternion
		 * @param heightindicator
		 *            box that is used to display the elevation of an entity
		 */
		public Target(Vector3f translation, Quaternion quat, Box heightindicator) {
			this.translation = translation;
			this.quaternion = quat;
			this.heightindicator = heightindicator;
		}
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}

	/**
	 * @param entitiesService
	 *            the entitiesService to set
	 */
	@Inject
	public void setEntitiesService(EntitiesService entitiesService) {
		this.entitiesService = entitiesService;
	}

	/**
	 * @param highlightingService
	 *            the highlightingService to set
	 */
	@Inject
	public void setHighlightingService(HighlightingService highlightingService) {
		this.highlightingService = highlightingService;
	}
}