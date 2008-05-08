/*
 *  MousePickListener.java
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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.interfaces.VisualEntityHolder;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.scenedata.SceneDataService;
import org.rifidi.designer.utils.Helpers;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.bounding.BoundingBox;
import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Matrix3f;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jmex.physics.DynamicPhysicsNode;
import com.jmex.physics.PhysicsNode;
import com.jmex.physics.material.Material;

/**
 * A listener for moving entites along all 3 axis.
 * 
 * @author Jochen Mader Oct 30, 2007
 */
public class EntityMouseMoveListener implements MouseListener,
		MouseMoveListener, MouseWheelListener {
	/**
	 * The currently picked entity.
	 */
	private VisualEntity pickedEntity = null;
	/**
	 * Reference to the 3d view.
	 */
	private View3D view3D;
	/**
	 * Center of the screen.
	 */
	private Point center;
	/**
	 * Movement delta on the mouse x axis.
	 */
	float deltaX = 0;
	/**
	 * Movement delta on the mouse y axis.
	 */
	float deltaY = 0;
	/**
	 * Mouse movement sensitivity.
	 */
	private float sensitivity = 5;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Reference to the camera service.
	 */
	private CameraService cameraService;
	/**
	 * Reference to the finder service.
	 */
	private FinderService finderService;

	/**
	 * Translation of the last hit object.
	 */
	private Vector3f lastHit;

	/**
	 * Constructor.
	 * 
	 * @param view3D
	 *            the 3d view
	 */
	public EntityMouseMoveListener(View3D view3D) {
		ServiceRegistry.getInstance().service(this);
		this.view3D = view3D;
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
		if (e.button == 1 || e.button == 3) {
			pickedEntity = null;
			Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera();
			// create ray
			int canvasY = ((SWTDisplaySystem) DisplaySystem.getDisplaySystem())
					.getCurrentGLCanvas().getSize().y;
			Vector3f coord = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 0);
			Vector3f coord2 = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 1);
			Ray ray = new Ray(coord, coord2.subtractLocal(coord)
					.normalizeLocal());
			PickResults pickResults = new BoundingPickResults();
			// shoot
			sceneDataService.getCurrentSceneData().getRootNode().findPick(ray,
					pickResults);
			Node node = null;
			// loop[ through the results to find an entity
			// this has to be done as for some reasons a bounding box appears
			// around the room and is hit first, darn
			for (int count = 0; count < pickResults.getNumber(); count++) {
				node = pickResults.getPickData(count).getTargetMesh()
						.getParentGeom().getParent();

				pickedEntity = finderService.getVisualEntityByNode(node);
			}
		}
		if (pickedEntity != null && pickedEntity instanceof VisualEntityHolder) {
			lastHit = (Vector3f) pickedEntity.getNode().getWorldTranslation()
					.clone();
			pickedEntity = ((VisualEntityHolder) pickedEntity)
					.getVisualEntity();
			if (pickedEntity != null) {
				Helpers.waitOnCallabel(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						pickedEntity.getNode().removeFromParent();
						lastHit.setY(((BoundingBox) pickedEntity.getNode()
								.getWorldBound()).yExtent + 0.1f);
						pickedEntity.getNode().setLocalTranslation(lastHit);
						pickedEntity.getNode().setLocalRotation(new Matrix3f());
						sceneDataService.getCurrentSceneData().getRootNode()
								.attachChild(pickedEntity.getNode());
						sceneDataService.getCurrentSceneData().getRootNode()
								.updateRenderState();
						return null;
					}

				});
			}
		}
		if (pickedEntity != null
				&& pickedEntity.getNode() instanceof DynamicPhysicsNode) {
			Rectangle bounds = Display.getCurrent().getActiveShell()
					.getBounds();
			center = new Point(bounds.x + bounds.width / 2, bounds.y
					+ bounds.height / 2);
			Display.getCurrent().setCursorLocation(center);
			deltaX = 0;
			deltaY = 0;
			view3D.hideMousePointer();
			GameTaskQueueManager.getManager().update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					((PhysicsNode) pickedEntity.getNode())
							.setMaterial(Material.GHOST);
					((PhysicsNode) pickedEntity.getNode()).setActive(false);
					return null;
				}

			});
		} else {
			pickedEntity = null;
		}
		ignore = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		view3D.showMousePointer();
		if (pickedEntity != null) {
			for (Entity ent : finderService
					.getEntitiesByType(VisualEntityHolder.class)) {
				if (ent instanceof VisualEntity) {
					VisualEntity ve = ((VisualEntity) ent);
					if (ve.collidesWith(pickedEntity)
							&& !((VisualEntityHolder) ve).isFull()
							&& ((VisualEntityHolder) ve).accepts(pickedEntity)) {
						((VisualEntityHolder) ve).addVisualEntity(pickedEntity);
						pickedEntity = null;
					} else {
						ve.showFootprint(false);
					}
				}
			}
			if (pickedEntity.getNode() instanceof PhysicsNode) {
				GameTaskQueueManager.getManager().update(
						new ActivationCallable((PhysicsNode) pickedEntity
								.getNode()));
				pickedEntity = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	private boolean ignore = false;

	@Override
	public void mouseMove(MouseEvent e) {
		if (pickedEntity != null && ignore == false) {
			// calculate how far to move the object(s)
			Point loc = Display.getCurrent().getCursorLocation();
			deltaX += ((float) loc.x - (float) center.x) / sensitivity;
			deltaY += ((float) loc.y - (float) center.y) / sensitivity;
			Vector3f up = Vector3f.UNIT_Z;
			Vector3f right = Vector3f.UNIT_X;

			Vector3f vec = up.mult((int) deltaY).add(right.mult((int) deltaX));
			vec.x = (int) vec.x;
			vec.z = (int) vec.z;

			// remove whatever amount has been used for calculating motion this
			// round
			deltaY -= (int) deltaY;
			deltaX -= (int) deltaX;
			pickedEntity.getNode().getLocalTranslation().addLocal(vec);
			float xDelta = pickedEntity.getNode().getLocalTranslation().x
					- ((BoundingBox) pickedEntity.getNode().getWorldBound()).xExtent
					/ 2;
			if (xDelta < 0) {
				pickedEntity.getNode().getLocalTranslation()
						.setX(
								((BoundingBox) pickedEntity.getNode()
										.getWorldBound()).xExtent / 2 + 0.5f);
			}
			xDelta = pickedEntity.getNode().getLocalTranslation().x
					+ ((BoundingBox) pickedEntity.getNode().getWorldBound()).xExtent
					/ 2;
			if (xDelta > sceneDataService.getWidth()) {
				pickedEntity.getNode().getLocalTranslation().setX(
						sceneDataService.getWidth()
								- ((BoundingBox) pickedEntity.getNode()
										.getWorldBound()).xExtent);
			}
			float zDelta = pickedEntity.getNode().getLocalTranslation().z
					- ((BoundingBox) pickedEntity.getNode().getWorldBound()).yExtent
					/ 2;
			if (zDelta < 0) {
				pickedEntity.getNode().getLocalTranslation()
						.setZ(
								((BoundingBox) pickedEntity.getNode()
										.getWorldBound()).zExtent / 2 + 0.5f);
			}
			zDelta = pickedEntity.getNode().getLocalTranslation().z
					+ ((BoundingBox) pickedEntity.getNode().getWorldBound()).zExtent
					/ 2;
			if (zDelta > sceneDataService.getWidth()) {
				pickedEntity.getNode().getLocalTranslation().setZ(
						sceneDataService.getWidth()
								- ((BoundingBox) pickedEntity.getNode()
										.getWorldBound()).zExtent);
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
	 * @see org.eclipse.swt.events.MouseWheelListener#mouseScrolled(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseScrolled(MouseEvent e) {
		if (pickedEntity != null) {
			if (pickedEntity.getNode().getLocalTranslation().y + e.count > 0) {
				pickedEntity.getNode().getLocalTranslation().addLocal(
						new Vector3f(0, e.count, 0));
			}
			return;
		}

		// int newzoom = cameraService.getActiveCamera().getZoom()
		// + (int) (e.count * 1.5f);
		// cameraService.getActiveCamera().setZoom(newzoom);
	}

	private class ActivationCallable implements Callable<Object> {

		private PhysicsNode physicsNode;

		/**
		 * @param physicsNode
		 */
		public ActivationCallable(PhysicsNode physicsNode) {
			super();
			this.physicsNode = physicsNode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {
			if (physicsNode instanceof DynamicPhysicsNode) {

				((DynamicPhysicsNode) physicsNode).clearDynamics();
				((DynamicPhysicsNode) physicsNode).clearForce();
				((DynamicPhysicsNode) physicsNode).setMaterial(Material.WOOD);
			}
			physicsNode.setActive(true);
			return null;
		}

	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
	}

	/**
	 * @param cameraService
	 *            the cameraService to set
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
	}

	/**
	 * @param finderService
	 *            the finderService to set
	 */
	@Inject
	public void setFinderService(FinderService finderService) {
		this.finderService = finderService;
	}

}