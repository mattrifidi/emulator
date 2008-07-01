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
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.bounding.BoundingBox;
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
public class CameraServiceImpl implements CameraService,
		SceneDataChangedListener {
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
		ServiceRegistry.getInstance().service(this);
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
		if (zoomlevel <= -44) {
			zoomlevel = -44;
		}
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
				camera.update();
				if (sceneDataService.getCurrentSceneData() != null) {
					for (Entity entity : sceneDataService.getCurrentSceneData()
							.getEntities()) {
						if (entity instanceof VisualEntity) {
							((VisualEntity) entity).setLOD(Math
									.abs((zoomlevel + 44) / 22));
						}
					}
				}
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
		if (zoomlevel >= -1) {
			zoomlevel = -1;
		}
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
				if (sceneDataService.getCurrentSceneData() != null) {
					for (Entity entity : sceneDataService.getCurrentSceneData()
							.getEntities()) {
						if (entity instanceof VisualEntity) {
							((VisualEntity) entity).setLOD(Math
									.abs((zoomlevel + 44) / 22));
						}
					}
				}
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
		camera = DisplaySystem.getDisplaySystem().getRenderer().createCamera(
				754, 584);
		camera
				.setFrustum(-100f, 1000.0f,
						-(baseFrustumvalue + zoomlevel) * 4 / 3,
						(baseFrustumvalue + zoomlevel) * 4 / 3,
						-(baseFrustumvalue + zoomlevel),
						(baseFrustumvalue + zoomlevel));

		camera.setLocation(new Vector3f(4.3f, 2, 4.6f));
		camera.lookAt(new Vector3f(3.7f, 1, 3), Vector3f.UNIT_Y);
		camera.setParallelProjection(true);
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
				camera.setLocation(new Vector3f(
						(int) (((BoundingBox) sceneDataService
								.getCurrentSceneData().getRoomNode()
								.getWorldBound()).xExtent / 2), 2,
						(int) ((BoundingBox) sceneDataService
								.getCurrentSceneData().getRoomNode()
								.getWorldBound()).zExtent / 2));
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
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		logger.debug("CamerService got SceneDataService");
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		centerCamera();
	}

}
