/*
 *  WorldServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.interfaces.SceneControl;
import org.rifidi.designer.rcp.views.view3d.threads.RenderThread;
import org.rifidi.designer.rcp.views.view3d.threads.UpdateThread;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.messaging.MessagingService;
import org.rifidi.designer.utils.Helpers;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;
import com.jmex.game.state.GameStateManager;

/**
 * Base worldservice implementation.
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public class WorldServiceImpl implements WorldService, CommandStateService,
		SceneDataChangedListener {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(WorldServiceImpl.class);
	/**
	 * The canvas we are rendering to.
	 */
	private GLCanvas glCanvas;
	/**
	 * currently used scenedata.
	 */
	private SceneData sceneData;
	/**
	 * The render thread for this world.
	 */
	private RenderThread renderThread;
	/**
	 * The update thread for this world.
	 */
	private UpdateThread updateThread;
	/**
	 * lock for syncing the threads.
	 */
	private ReentrantLock lock;
	/**
	 * eclipse display to do sync/asyncExec.
	 */
	Display display;
	/**
	 * actions that need to be given to the UpdateThread.
	 */
	private List<RepeatedUpdateAction> repeatedActions;
	/**
	 * The current state of the world.
	 */
	private WorldStates worldState;
	/**
	 * Maps WorldStates to command names.
	 */
	private Map<WorldStates, List<String>> stateMap;
	/**
	 * Reference to the scenedataservice.
	 */
	private SceneDataService sceneDataService;
	/**
	 * reference to the messaging service.
	 */
	private MessagingService messagingService;
	/**
	 * Reference to the field service.
	 */
	private FieldService fieldService;
	/**
	 * Reference to the camera service.
	 */
	private CameraService cameraService;

	/**
	 * Constructor.
	 * 
	 * @param display
	 */
	public WorldServiceImpl() {
		logger.debug("WorldService created");
		repeatedActions = new ArrayList<RepeatedUpdateAction>();
		worldState = WorldStates.NoSceneDataLoaded;
		stateMap = new HashMap<WorldStates, List<String>>();
		stateMap.put(WorldStates.NoSceneDataLoaded, new ArrayList<String>());
		stateMap.get(WorldStates.NoSceneDataLoaded).add("newscene");
		stateMap.put(WorldStates.Paused, new ArrayList<String>());
		stateMap.get(WorldStates.Paused).add("newscene");
		stateMap.get(WorldStates.Paused).add("saveas");
		stateMap.get(WorldStates.Paused).add("save");
		stateMap.get(WorldStates.Paused).add("start");
		stateMap.get(WorldStates.Paused).add("stop");
		stateMap.put(WorldStates.Stopped, new ArrayList<String>());
		stateMap.get(WorldStates.Stopped).add("newscene");
		stateMap.get(WorldStates.Stopped).add("saveas");
		stateMap.get(WorldStates.Stopped).add("save");
		stateMap.get(WorldStates.Stopped).add("start");
		stateMap.put(WorldStates.Running, new ArrayList<String>());
		stateMap.get(WorldStates.Running).add("newscene");
		stateMap.get(WorldStates.Running).add("saveas");
		stateMap.get(WorldStates.Running).add("save");
		stateMap.get(WorldStates.Running).add("stop");
		stateMap.get(WorldStates.Running).add("pause");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#pause()
	 */
	@Override
	public void pause() {
		updateThread.setPaused(true);
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof SceneControl) {
				((SceneControl) entity).pause();
			}
		}
		worldState = WorldStates.Paused;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#start()
	 */
	@Override
	public void start() {
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof SceneControl) {
				((SceneControl) entity).start();
			}
		}
		updateThread.setPaused(false);
		worldState = WorldStates.Running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#stop()
	 */
	@Override
	public void stop() {
		Helpers.waitOnCallabel(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			public Object call() throws Exception {
				synchronized (sceneData.getSyncedEntities()) {
					for (Entity entity : new ArrayList<Entity>(sceneData
							.getSyncedEntities())) {
						if (entity instanceof SceneControl) {
							((SceneControl) entity).reset();
						}
					}
				}
				return new Object();
			}

		});
		updateThread.setPaused(true);
		worldState = WorldStates.Stopped;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#setGLCanvas(org.eclipse.swt.opengl.GLCanvas)
	 */
	@Override
	public void setGLCanvas(GLCanvas glCanvas) {
		this.glCanvas = glCanvas;
		SWTDisplaySystem displaySys = (SWTDisplaySystem) DisplaySystem
				.getDisplaySystem("SWTDISPLAYSYS");
		displaySys.setCurrentGLCanvas(glCanvas);
		displaySys.createCanvasRenderer(754, 584, ColorRGBA.lightGray);

		displaySys.switchContext(glCanvas);
		if (renderThread != null) {
			renderThread = new RenderThread(lock, display, glCanvas, sceneData,
					messagingService);
			renderThread.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		logger.debug("starting threads");
		this.sceneData = sceneData;

		lock = new ReentrantLock();
		// create the render thread
		renderThread = new RenderThread(lock, display, glCanvas, sceneData,
				messagingService);

		// create the update thread
		if (updateThread != null)
			glCanvas.removeKeyListener(updateThread);
		updateThread = new UpdateThread(lock, sceneData, repeatedActions,
				fieldService, cameraService);
		updateThread.setPaused(true);
		glCanvas.addKeyListener(updateThread);

		// start the render and update threads
		updateThread.start();
		renderThread.start();
		worldState = WorldStates.Paused;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		if (updateThread != null) {
			stop();
			updateThread.setKeepRunning(false);
			try {
				updateThread.join();
			} catch (InterruptedException e) {
				logger.debug("Got interrupted while joining on UpdateThread: "
						+ e);
			}

			renderThread.setKeepRunning(false);
			try {
				renderThread.join();
			} catch (InterruptedException e) {
				logger.debug("Got interrupted while joining on RenderThread: "
						+ e);
			}
			GameStateManager.getInstance().cleanup();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#addRepeatedUpdateActiom(org.rifidi.services.registry.core.world.RepeatedUpdateAction)
	 */
	@Override
	public void addRepeatedUpdateActiom(RepeatedUpdateAction action) {
		repeatedActions.add(action);
		if (updateThread != null) {
			updateThread.addRepeatedUpdateAction(action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#removeRepeatedUpdateActiom(org.rifidi.services.registry.core.world.RepeatedUpdateAction)
	 */
	@Override
	public void removeRepeatedUpdateActiom(RepeatedUpdateAction action) {
		repeatedActions.remove(action);
		if (updateThread != null) {
			updateThread.removeRepeatedUpdateAction(action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.CommandStateService#isEnabled(java.lang.String)
	 */
	@Override
	public boolean isEnabled(String commandName) {
		return stateMap.get(worldState).contains(commandName);
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		logger.debug("WorldService got SceneDataService");
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param messagingService
	 *            the messagingService to unset
	 */
	public void unsetMessagingService(MessagingService messagingService) {
		this.messagingService = null;
	}

	/**
	 * @param messagingService
	 *            the messagingService to set
	 */
	public void setMessagingService(MessagingService messagingService) {
		logger.debug("WorldService got MessagingService");
		this.messagingService = messagingService;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = null;
	}

	/**
	 * @param fieldService
	 *            the fieldService to set
	 */
	public void setFieldService(FieldService fieldService) {
		logger.debug("WorldService got FieldService");
		this.fieldService = fieldService;
	}

	/**
	 * @param fieldService
	 *            the fieldService to unset
	 */
	public void unsetFieldService(FieldService fieldService) {
		this.fieldService = null;
	}

	/**
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * @param cameraService
	 *            the cameraService to set
	 */
	public void setCameraService(CameraService cameraService) {
		logger.debug("WorldService got CameraService");
		this.cameraService = cameraService;
	}

	/**
	 * @param cameraService
	 *            the cameraService to unset
	 */
	public void unsetCameraService(CameraService cameraService) {
		this.cameraService = null;
	}
}