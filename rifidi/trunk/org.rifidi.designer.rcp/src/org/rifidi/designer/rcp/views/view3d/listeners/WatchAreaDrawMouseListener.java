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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.monklypse.core.JMECanvasImplementor2;
import org.rifidi.designer.entities.internal.WatchAreaEntity;
import org.rifidi.designer.rcp.game.DesignerGame;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;

/**
 * A listener for drawing watch areas.
 * 
 * @author Jochen Mader Oct 30, 2007
 */
public class WatchAreaDrawMouseListener implements MouseListener,
		MouseMoveListener {

	/** Reference to the 3d view. */
	private View3D view3D;
	/** The watchare that gets manipulated */
	private Box box;
	/** Node that the box is attached to */
	private Node boxNode;
	/** Coordinates of the box startpoint */
	private float startX, startZ;
	/** true if the runnable is currently being executed */
	private AtomicBoolean executing = new AtomicBoolean(false);
	/** true if the button is pressed */
	private boolean pressed = false;
	/** Reference to the entities service. */
	private EntitiesService entitiesService;
	/** Reference to the scene data service. */
	private SceneDataService sceneDataService;
	/** Reference to the implementor */
	private DesignerGame implementor;

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
		if (e.button == 1) {
			pressed = true;
			int canvasY = implementor.getCanvas().getSize().y;

			Vector3f coords = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera().getWorldCoordinates(
							new Vector2f(e.x, canvasY - e.y), 0);
			Vector3f coords2 = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera().getWorldCoordinates(
							new Vector2f(e.x, canvasY - e.y), 1);
			Vector3f direction = coords.subtract(coords2).normalizeLocal();
			coords.subtractLocal(direction.mult(coords.y / direction.y));
			coords.setY(0);
			// round the values to place it on the grid
			coords.x = (float) Math.floor(coords.x);
			coords.z = (float) Math.floor(coords.z);

			startX = coords.x;
			startZ = coords.z;
			boxNode = new Node();
			box = new Box("name", Vector3f.ZERO, .5f, 7f, .5f);

			boxNode.setLocalTranslation(new Vector3f(
					(float) Math.ceil(startX) - .5f, 5.6f, (float) Math
							.ceil(startZ) - .5f));
			boxNode.attachChild(box);
			implementor.update(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				@Override
				public Object call() throws Exception {
					MaterialState ms = DisplaySystem.getDisplaySystem()
							.getRenderer().createMaterialState();
					ms.setDiffuse(new ColorRGBA(0, 0, 1, .5f));
					ms.setEmissive(new ColorRGBA(0, 0, 1, .3f));
					ms.setShininess(1);
					ms.setSpecular(new ColorRGBA(0, 0, 1, .3f));
					BlendState as = DisplaySystem.getDisplaySystem()
							.getRenderer().createBlendState();
					as.setBlendEnabled(true);
					as.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
					as
							.setDestinationFunction(BlendState.DestinationFunction.One);
					as.setEnabled(true);
					sceneDataService.getCurrentSceneData().getRoomNode()
							.attachChild(boxNode);
					boxNode.setRenderState(as);
					boxNode.setRenderState(ms);
					boxNode.updateRenderState();
					box.setRenderQueueMode(Renderer.QUEUE_TRANSPARENT);
					return null;
				}

			});
		} else if (e.button == 3) {
			pressed = false;
			if (boxNode != null) {
				implementor.update(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						boxNode.removeFromParent();
						boxNode = null;
						box = null;
						pressed = false;
						return null;
					}
				});

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if (e.button == 1 && pressed) {
			WatchAreaEntity watchAreaEntity = new WatchAreaEntity();
			watchAreaEntity.setNode(boxNode);
			if (box.getLocalScale().x > 0 && box.getLocalScale().y > 0
					&& box.getLocalScale().z > 0) {
				entitiesService.addEntity(watchAreaEntity, null, null);
			} else {
				implementor.update(new Callable<Object>() {

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
	 * @see
	 * org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events
	 * .MouseEvent)
	 */
	@Override
	public void mouseMove(MouseEvent e) {
		if (pressed == true) {
			int canvasY = ((GLCanvas) ((JMECanvasImplementor2) implementor)
					.getCanvas()).getSize().y;

			Vector3f coords = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera().getWorldCoordinates(
							new Vector2f(e.x, canvasY - e.y), 0);
			Vector3f coords2 = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera().getWorldCoordinates(
							new Vector2f(e.x, canvasY - e.y), 1);
			Vector3f direction = coords.subtract(coords2).normalizeLocal();
			coords.subtractLocal(direction.mult(coords.y / direction.y));
			coords.setY(0);
			// round the values to place it on the grid
			coords.x = (float) Math.floor(coords.x);
			coords.z = (float) Math.floor(coords.z);
			final float newX = coords.x;
			final float newZ = coords.z;
			if (executing.compareAndSet(false, true) && boxNode != null) {
				implementor.update(new Callable<Object>() {

					/*
					 * (non-Javadoc)
					 * 
					 * CanvasImplementor2)implementor).getCanvas()
					 * .getCurrentGLCanvas()@see
					 * java.util.concurrent.Callable#call()
					 */
					@Override
					public Object call() throws Exception {
						try {
							float left = startX < newX ? (float) Math
									.ceil(startX) : (float) Math.floor(newX);
							float top = startZ < newZ ? (float) Math
									.ceil(startZ) : (float) Math.floor(newZ);
							float right = newX > startX ? (float) Math
									.floor(newX) : (float) Math.ceil(startX);
							float bottom = newZ > startZ ? (float) Math
									.floor(newZ) : (float) Math.ceil(startZ);
							boxNode.getLocalTranslation().set(
									new Vector3f((right - left) / 2 + left,
											5.6f, (bottom - top) / 2 + top));
							box.getLocalScale().set(
									new Vector3f((right - left), 1,
											(bottom - top)));
						} catch (NullPointerException e) {
							// TODO: yeah, it's ugly but it can happen that this
							// gets called when the box has been deleted
						} finally {
							executing.compareAndSet(true, false);
						}
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

	/**
	 * @param implementor
	 *            the implementor to set
	 */
	@Inject
	public void setImplementor(DesignerGame implementor) {
		this.implementor = implementor;
	}

}