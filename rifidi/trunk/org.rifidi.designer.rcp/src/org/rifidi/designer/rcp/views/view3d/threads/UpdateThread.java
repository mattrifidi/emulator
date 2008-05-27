/*
 *  UpdateThread.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.threads;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.world.RepeatedUpdateAction;
import org.rifidi.utilities.text.TextOverlayManager;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.game.state.GameStateManager;

/**
 * This thread does all the updates to the scene graph.
 * 
 * @author Jochen Mader
 * @author Dan West
 */
public class UpdateThread extends Thread implements KeyListener {
	/**
	 * Logger for this class.
	 */
	private Log logger = LogFactory.getLog(UpdateThread.class);
	/**
	 * Flag for thread termination.
	 */
	private boolean keepRunning = true;
	/**
	 * Global timer for frame calculation.
	 */
	private Timer timer;
	/**
	 * Last time the thread was running.
	 */
	private float lasttick;
	/**
	 * Lock to avoid collision with render thread.
	 */
	private ReentrantLock lock;
	/**
	 * Paused state.
	 */
	private boolean paused;
	/**
	 * Currently used scene.
	 */
	private SceneData sceneData;
	/**
	 * Time offset.
	 */
	private float offset = 0;
	/**
	 * List of actions that should be repeated on each run.
	 */
	private List<RepeatedUpdateAction> repeatedActions;
	/**
	 * Boolean indicators for camera directional motion
	 */
	private boolean[] updownleftright = new boolean[4];
	/**
	 * Reference to the camera service
	 */
	private CameraService cameraService;
	/**
	 * Reference to the collision service.
	 */
	private FieldService fieldService;

	/**
	 * 
	 * @param rootNode
	 *            root of the scene to update
	 * @param physicsSpace
	 *            associated physics space
	 * @param update
	 *            update queue to use
	 * @param fieldService
	 * 			  current fieldservice
	 */
	public UpdateThread(ReentrantLock lock, SceneData sceneData,
			List<RepeatedUpdateAction> repeatedActions, FieldService fieldService, CameraService cameraService) {
		logger.debug("New UpdateThread instantiated.");
		timer = Timer.getTimer();
		this.lock = lock;
		this.setName("UpdateThread");
		this.sceneData = sceneData;
		this.repeatedActions = Collections.synchronizedList(repeatedActions);
		this.fieldService = fieldService;
		this.cameraService=cameraService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		logger.debug("UpdateThread started.");
		lasttick = timer.getTimeInSeconds();
		float currentTime = 0.0f;
		while (keepRunning) {
			lock.lock();
			currentTime = timer.getTimeInSeconds();
			float dt = (float) (currentTime - (lasttick + offset));
			offset = 0;
			GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
					.execute();
			sceneData.getRoomNode().updateGeometricState(dt, true);
			sceneData.getRootNode().updateGeometricState(dt, true);
			performCameraMotion();
			GameStateManager.getInstance().update(dt);
			if (!isPaused()) {
				sceneData.getCollisionHandler().update(dt);
				sceneData.getPhysicsSpace().update(dt);
			}
			TextOverlayManager.getInstance().update(dt);
			float timePassed = currentTime - lasttick;
			synchronized (repeatedActions) {
				for (RepeatedUpdateAction action : repeatedActions) {
					action.doUpdate(timePassed);
				}
			}

			if (sceneData.getRootNode() != null) {
				// TODO: should probably go away
				sceneData.getRootNode().updateRenderState();
			}
			lasttick = currentTime;
			fieldService.checkFields();
			lock.unlock();
			try {
				sleep(10);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			if (isPaused()) {
				offset += 0.001f;
			}
		}
		logger.debug("UpdateThread stopped.");
	}

	/**
	 * Check the state of the thread.
	 * 
	 * @return the keepRunninung
	 */
	public boolean isKeepRunning() {
		return keepRunning;
	}

	/**
	 * Set to false to stop thread.
	 * 
	 * @param keepRunning
	 *            the keepRunninung to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	/**
	 * Check if the updating is paused.
	 * 
	 * @return
	 */
	public boolean isPaused() {
		return paused;
	}

	/**
	 * Set the paused state.
	 * 
	 * @param paused
	 *            paused value.
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	/**
	 * Add an action that should be taken on each update run.
	 * 
	 * @param action
	 *            the action that should be repeated.
	 */
	public void addRepeatedUpdateAction(RepeatedUpdateAction action) {
		repeatedActions.add(action);
	}

	/**
	 * Remove a repeated action.
	 * 
	 * @param action
	 *            the action to be removed.
	 */
	public void removeRepeatedUpdateAction(RepeatedUpdateAction action) {
		repeatedActions.remove(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.character == 'a' || e.character == 'A') {
			updownleftright[2] = true;
		} else if (e.character == 'd' || e.character == 'D') {
			updownleftright[3] = true;
		} else if (e.character == 'w' || e.character == 'W') {
			updownleftright[0] = true;
		} else if (e.character == 's' || e.character == 'S') {
			updownleftright[1] = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		updownleftright[0] = false;
		updownleftright[1] = false;
		updownleftright[2] = false;
		updownleftright[3] = false;
	}

	/**
	 * Reposition the camera if any motion keys are held down.
	 */
	public void performCameraMotion() {

		float keyspeed = 0.5f;
		Camera cam = DisplaySystem.getDisplaySystem().getRenderer().getCamera();
		if (updownleftright[2]) {
			cam.setLocation(cam.getLocation().add(new Vector3f(-keyspeed,0,0)));
		}
		if (updownleftright[3]) {
			cam.setLocation(cam.getLocation().add(new Vector3f(keyspeed,0,0)));
		}
		if (updownleftright[0]) {
			cam.setLocation(cam.getLocation().add(new Vector3f(0,0,-keyspeed)));
		}
		if (updownleftright[1]) {
			cam.setLocation(cam.getLocation().add(new Vector3f(0,0,keyspeed)));
		}
	}
}