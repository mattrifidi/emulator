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
import org.rifidi.designer.entities.internal.WatchAreaEntity;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.jmeswt.utils.Helpers;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

/**
 * A listener for drawing watch areas.
 * 
 * @author Jochen Mader Oct 30, 2007
 */
public class WatchAreaDrawMouseListener implements MouseListener,
		MouseMoveListener {

	/**
	 * Reference to the 3d view.
	 */
	private View3D view3D;

	private Box box;

	private Node boxNode;

	private float startX, startZ;

	private boolean executing = false;

	private boolean pressed = true;

	private EntitiesService entitiesService;

	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 * 
	 * @param view3D
	 *            the 3d view
	 */
	public WatchAreaDrawMouseListener(View3D view3D) {
		this.view3D = view3D;
		ServiceRegistry.getInstance().service(this);
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
		if (e.button == 1) {
			pressed = true;
			Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera();
			// create ray
			int canvasY = ((SWTDisplaySystem) DisplaySystem.getDisplaySystem())
					.getCurrentGLCanvas().getSize().y;
			Vector3f coord = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 0);
			Vector3f coord2 = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 1f);

			float m = (coord.y - coord2.y) / (coord.x - coord2.x);
			float b = -(coord.y - m * coord.x);

			float m2 = (coord.y - coord2.y) / (coord.z - coord2.z);
			float b2 = -(coord.y - m2 * coord.z);

			startX = b / m;
			startZ = b2 / m2;
			if (startX > 0
					&& startZ > 0
					&& startX < ((BoundingBox) sceneDataService
							.getCurrentSceneData().getRoomNode()
							.getWorldBound()).xExtent
					&& startZ < ((BoundingBox) sceneDataService
							.getCurrentSceneData().getRoomNode()
							.getWorldBound()).zExtent) {

				boxNode = new Node();
				box = new Box("name", Vector3f.ZERO, .5f, 7f, .5f);
				boxNode.setLocalTranslation(new Vector3f((float) Math
						.ceil(startX) - .5f, 5.6f,
						(float) Math.ceil(startZ) - .5f));
				boxNode.attachChild(box);
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see java.util.concurrent.Callable#call()
							 */
							@Override
							public Object call() throws Exception {
								MaterialState ms = DisplaySystem
										.getDisplaySystem().getRenderer()
										.createMaterialState();
								ms.setDiffuse(new ColorRGBA(0, 0, 1, .5f));
								ms.setEmissive(new ColorRGBA(0, 0, 1, .3f));
								ms.setShininess(1);
								ms.setSpecular(new ColorRGBA(0, 0, 1, .3f));
								AlphaState as = DisplaySystem
										.getDisplaySystem().getRenderer()
										.createAlphaState();
								as.setBlendEnabled(true);
								as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
								as.setDstFunction(AlphaState.DB_ONE);
								as.setEnabled(true);
								sceneDataService.getCurrentSceneData()
										.getRoomNode().attachChild(boxNode);
								boxNode.setRenderState(as);
								boxNode.setRenderState(ms);
								boxNode.updateRenderState();
								box
										.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
								return null;
							}

						});
			}
		} else if (e.button == 3) {
			pressed = false;
			if (boxNode != null) {
				Helpers.waitOnCallabel(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						boxNode.removeFromParent();
						return null;
					}

				});
				boxNode = null;
				box = null;
				pressed = false;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if (e.button == 1 && pressed) {
			WatchAreaEntity watchAreaEntity = new WatchAreaEntity();
			watchAreaEntity.setNode(boxNode);
			if (box.getLocalScale().x > 0 && box.getLocalScale().y > 0
					&& box.getLocalScale().z > 0) {
				entitiesService.addEntity(watchAreaEntity, false, null);
			} else {
				Helpers.waitOnCallabel(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						boxNode.removeFromParent();
						return null;
					}

				});
			}
			boxNode = null;
			box = null;
			pressed = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent e) {
		if (pressed == true) {
			Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera();
			// create ray
			int canvasY = ((SWTDisplaySystem) DisplaySystem.getDisplaySystem())
					.getCurrentGLCanvas().getSize().y;
			Vector3f coord = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 0);
			Vector3f coord2 = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 1f);

			float m = (coord.y - coord2.y) / (coord.x - coord2.x);
			float b = -(coord.y - m * coord.x);

			float m2 = (coord.y - coord2.y) / (coord.z - coord2.z);
			float b2 = -(coord.y - m2 * coord.z);

			final float newX = b / m;
			final float newZ = b2 / m2;
			if (!executing && boxNode != null) {
				executing = true;
				GameTaskQueueManager.getManager().update(
						new Callable<Object>() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see java.util.concurrent.Callable#call()
							 */
							@Override
							public Object call() throws Exception {
								float left = startX < newX ? (float) Math
										.ceil(startX) : (float) Math
										.floor(newX);
								float top = startZ < newZ ? (float) Math
										.ceil(startZ) : (float) Math
										.floor(newZ);
								float right = newX > startX ? (float) Math
										.floor(newX) : (float) Math
										.ceil(startX);
								float bottom = newZ > startZ ? (float) Math
										.floor(newZ) : (float) Math
										.ceil(startZ);
								if (left < 0) {
									left = 0;
								}
								if (right > ((BoundingBox) sceneDataService
										.getCurrentSceneData().getRoomNode()
										.getWorldBound()).xExtent) {
									right = ((BoundingBox) sceneDataService
											.getCurrentSceneData()
											.getRoomNode().getWorldBound()).xExtent;
								}
								if (top < 0) {
									top = 0;
								}
								if (bottom > ((BoundingBox) sceneDataService
										.getCurrentSceneData().getRoomNode()
										.getWorldBound()).zExtent) {
									bottom = ((BoundingBox) sceneDataService
											.getCurrentSceneData()
											.getRoomNode().getWorldBound()).zExtent;
								}
								boxNode.getLocalTranslation()
										.set(
												new Vector3f((right - left) / 2
														+ left, 5.6f,
														(bottom - top) / 2
																+ top));
								box.getLocalScale().set(
										new Vector3f((right - left), 1,
												(bottom - top)));
								executing = false;
								return null;
							}
						});
			}
		}
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
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
	}

}