/*
 *  CameraServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.camera;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.services.core.entities.SceneDataService;

import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;

/**
 * 
 * Basic implementation of the camera service.
 * 
 * @author Dan West - dan@pramari.com
 * 
 */
public class CameraServiceImpl implements CameraService {
	/**
	 * The logger.
	 */
	private Log logger = LogFactory.getLog(CameraServiceImpl.class);
	/**
	 * Array of stored camera states.
	 */
	private CameraState[] cameraStates = new CameraState[10];

	private int zoomlevel;

	private SceneDataService sceneDataService;

	private Camera camera;

	private int baseFrustumvalue = 50;

	private Camera tempCam;

	/**
	 * Default constructor.
	 */
	public CameraServiceImpl() {
		logger.debug("CamerService created");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.camera.CameraService#loadRecordedCameraState(int)
	 */
	@Override
	public void loadRecordedCameraState(int num) {
		CameraState cameraState = cameraStates[num];
		if (cameraState != null) {
			// setActiveCamera(cameraState.getCameraId());
			// getActiveCamera().placeCamera(cameraState.getLocation(),
			// cameraState.getDirection());
			// getActiveCamera().setZoom(cameraState.getZoom());
			// getActiveCamera().setLookAtPoint(cameraState.getLookAtPoint());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.camera.CameraService#recordCameraState(int)
	 */
	@Override
	public void recordCameraState(int num) {
		// CameraState cameraState = new CameraState();
		// cameraState.setLocation(getActiveCamera().getLocation());
		// cameraState.setDirection(getActiveCamera().getDirection());
		// cameraState.setZoom(getActiveCamera().getZoom());
		// cameraState.setCameraId(activeCameraId);
		// cameraState.setLookAtPoint(getActiveCamera().getLookAtPoint());
		// cameraStates[num] = cameraState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.camera.CameraService#resetZoom()
	 */
	@Override
	public void resetZoom() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.camera.CameraService#zoomIn()
	 */
	@Override
	public void zoomIn() {
		zoomlevel--;
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				camera.setFrustum(-100f, 1000.0f,
						-(baseFrustumvalue + zoomlevel) * 4 / 3,
						(baseFrustumvalue + zoomlevel) * 4 / 3,
						-(baseFrustumvalue + zoomlevel),
						(baseFrustumvalue + zoomlevel));
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.camera.CameraService#zoomOut()
	 */
	@Override
	public void zoomOut() {
		zoomlevel++;
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				camera.setFrustum(-100f, 1000.0f,
						-(baseFrustumvalue + zoomlevel) * 4 / 3,
						(baseFrustumvalue + zoomlevel) * 4 / 3,
						-(baseFrustumvalue + zoomlevel),
						(baseFrustumvalue + zoomlevel));
				return null;
			}

		});
	}

	/**
	 * Creates a camera with the default settings.
	 * 
	 * @return a new initialized Jmonkey camera
	 */
	public void createCamera() {
		float interval = (float) (Math.PI * 0.1f);
		float aspect = 1024 / 768;
		// Vector3f dir = new Vector3f(-0.1f, -.7f, -.7f);
		Vector3f dir = new Vector3f(0.0f, 1.0f, 0.0f);
		camera = DisplaySystem.getDisplaySystem().getRenderer().createCamera(
				754, 584);
		camera
				.setFrustum(-100f, 1000.0f,
						-(baseFrustumvalue + zoomlevel) * 4 / 3,
						(baseFrustumvalue + zoomlevel) * 4 / 3,
						-(baseFrustumvalue + zoomlevel),
						(baseFrustumvalue + zoomlevel));
		// camera.setLocation(new Vector3f(4, 2, 4));
		// camera.setDirection(new Vector3f(0f, -1f, -.001f));

		camera.setLocation(new Vector3f(4, 2, 4));
		camera.lookAt(new Vector3f(3.7f, 1, 3), Vector3f.UNIT_Y);
		// camera.lookAt(new Vector3f(45,0,5), Vector3f.UNIT_Y);
		// camera.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		camera.setParallelProjection(true);
		// Quaternion q = new Quaternion();
		// q.fromAngles(0, interval, 0);
		// q.toRotationMatrix(new Matrix3f()).multLocal(camera.getDirection());
		camera.update();
		DisplaySystem.getDisplaySystem().getRenderer().setCamera(camera);
	}

	public void centerCamera() {
		GameTaskQueueManager.getManager().render(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				camera.setLocation(new Vector3f((int) (sceneDataService
						.getCurrentSceneData().getWidth() / 2), 2,
						(int) (sceneDataService.getCurrentSceneData()
								.getWidth() / 2)));

				camera.getLocation().addLocal(new Vector3f(-5, 0, -15));
				camera.update();
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.camera.CameraService#getMainCamera()
	 */
	@Override
	public Camera getMainCamera() {
		return camera;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		logger.debug("CamerService got SceneDataService");
		this.sceneDataService = sceneDataService;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.camera.CameraService#positionCamera(com.jme.math.Vector3f)
	 */
	@Override
	public void positionCamera(final Vector3f targetPos) {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				camera.setLocation(targetPos);
				return null;
			}

		});
	}

}
