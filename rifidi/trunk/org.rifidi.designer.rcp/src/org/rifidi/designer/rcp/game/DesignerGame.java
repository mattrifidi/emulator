/*
 *  DesignerGame.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.game;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.entities.interfaces.SceneControl;
import org.rifidi.designer.rcp.GlobalProperties;
import org.rifidi.designer.rcp.views.minimapview.MiniMapView;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.world.CommandStateService;
import org.rifidi.designer.services.core.world.RepeatedUpdateAction;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.designer.services.core.world.WorldStates;
import org.rifidi.jmeswt.SWTBaseGame;
import org.rifidi.jmeswt.utils.Helpers;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.utilities.grid.GridNode;

import com.jme.light.DirectionalLight;
import com.jme.light.LightNode;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.renderer.Renderer;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FragmentProgramState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 28, 2008
 * 
 */
public class DesignerGame extends SWTBaseGame implements
		SceneDataChangedListener, ISelectionChangedListener, KeyListener,
		WorldService, CommandStateService {
	private static final Log logger = LogFactory.getLog(DesignerGame.class);
	/**
	 * Wall transparency. TODO redo trans stuff
	 */
	private AlphaState wallAlpha;
	/**
	 * Zbuffer for the rootnode.
	 */
	private ZBufferState zbufferState;
	/**
	 * Cullstate for the rootNode.
	 */
	private CullState cullState;
	/**
	 * The primary lightstate.
	 */
	private LightState ls;
	/**
	 * Currently used scenedata.
	 */
	private SceneData sceneData;
	/**
	 * Reference to the scenedataservice.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Reference to the camera service.
	 */
	private CameraService cameraService;
	/**
	 * Offscreenrenderer for the minimap.
	 */
	private OffscreenRenderer offy;
	/**
	 * Reference to the minimap.
	 */
	private MiniMapView miniMapView;
	/**
	 * Display minimap every nth frame.
	 */
	private int minimapCounter = 10;
	/**
	 * ImageData for the minimap.
	 */
	private ImageData imgData;
	/**
	 * Alpha state used for highlighting.
	 */
	private AlphaState alphaState;
	/**
	 * Fragment program state used for highlighting.
	 */
	private FragmentProgramState fragmentProgramState;
	/**
	 * Fragment program used for highlighting.
	 */
	private String fragprog = "!!ARBfp1.0"
			+ "MOV result.color, program.local[0];"
			+ "MOV result.color.a, program.local[1].a;" + "END";
	/**
	 * Action submitted to the update thread for updating the highlight state.
	 */
	private HilitedRepeatedUpdateAction repeater;
	/**
	 * Reference to the selectionservice.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the field service.
	 */
	private FieldService fieldService;
	/**
	 * List of highlighted entities.
	 */
	private List<VisualEntity> hilited;
	/**
	 * The current state of the world.
	 */
	private WorldStates worldState;
	/**
	 * Maps WorldStates to command names.
	 */
	private Map<WorldStates, List<String>> stateMap;
	/**
	 * Boolean indicators for camera directional motion
	 */
	private boolean[] updownleftright = new boolean[4];
	/**
	 * Scene related stuff.
	 */
	private boolean gridEnabled = true;
	/**
	 * Node for the grid.
	 */
	private GridNode gridNode;
	/**
	 * Grid game state.
	 */
	private BasicGameState gridState;

	/**
	 * @param name
	 * @param updateResolution
	 * @param renderResolution
	 * @param width
	 * @param height
	 * @param parent
	 */
	public DesignerGame(String name, int updateResolution,
			int renderResolution, int width, int height, Composite parent) {
		super(name, updateResolution, renderResolution, width, height, parent,
				true);
		hilited = new ArrayList<VisualEntity>();
		logger.debug("WorldService created");
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
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#simpleInitGame()
	 */
	@Override
	protected void simpleInitGame() {
		getGlCanvas().addKeyListener(this);
		offy = ((SWTDisplaySystem) display).createOffscreenRenderer(200, 200);
		if (offy.isSupported()) {
			EXTFramebufferObject.glBindFramebufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		} else {
			logger.debug("Offscreen rendering is not supported!");
		}

		GameStateManager.create();
		// alphastate for transparent walls
		wallAlpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		wallAlpha.setBlendEnabled(true);
		wallAlpha.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		wallAlpha.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		wallAlpha.setEnabled(true);
		// create ZBuffer stuff
		zbufferState = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		zbufferState.setFunction(ZBufferState.CF_LEQUAL);
		zbufferState.setEnabled(true);

		// create cullstate for backface culling
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cullState.setCullMode(CullState.CS_BACK);

		// create alpha state for highlighting
		alphaState = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		alphaState.setBlendEnabled(true);
		alphaState.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		alphaState.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		alphaState.setEnabled(true);

		// create fragment program state for highlighting
		fragmentProgramState = DisplaySystem.getDisplaySystem().getRenderer()
				.createFragmentProgramState();
		fragmentProgramState.load(fragprog);
		fragmentProgramState.setEnabled(true);
		float[] color4f = new float[] { .45f, .55f, 1f, 0f };
		fragmentProgramState.setParameter(color4f, 0);
		repeater = new HilitedRepeatedUpdateAction();

		// create a default light
		ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		LightNode lightNode = new LightNode("Head light", ls);
		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1, 1, 1, 1));
		dl.setAmbient(new ColorRGBA(0.4f, 0.4f, 0.4f, 1));
		dl.setDirection(new Vector3f(0.1f, -1, 0.1f));
		dl.setEnabled(true);
		lightNode.setLight(dl);
		display.getRenderer().setBackgroundColor(ColorRGBA.gray.clone());
		getRootNode().setRenderState(zbufferState);
		getRootNode().setRenderState(cullState);
		getRootNode().setRenderState(ls);
		getRootNode().updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#render(float)
	 */
	@Override
	protected void render(float interpolation) {
		if (sceneData != null && !getGlCanvas().isDisposed()) {
			GameStateManager.getInstance().render(0);
			if (GlobalProperties.physicsDebugging) {
				PhysicsDebugger.drawPhysics(sceneData.getPhysicsSpace(),
						display.getRenderer());
			}
			if (GlobalProperties.boundingDebugging) {
				Debugger.drawBounds(sceneData.getRootNode(), display
						.getRenderer());
			}
			if (miniMapView == null) {
				miniMapView = (MiniMapView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().findView(
								MiniMapView.ID);
			}
			display.getRenderer().displayBackBuffer();
			getGlCanvas().swapBuffers();
			if (offy.isSupported() && miniMapView != null
					&& minimapCounter == 10) {
				miniMapView.setMapCamera(offy.getCamera());
				minimapCounter = 0;
				offy.render(getRootNode());
				IntBuffer buffer = offy.getImageData();
				if (imgData == null) {
					imgData = new ImageData(200, 200, 32, new PaletteData(
							0xFF0000, 0x00FF00, 0x0000FF));
				}
				for (int y = 0; y < 200; y++) {
					for (int x = 0; x < 200; x++) {
						imgData.setPixel(x, y, buffer.get((199 - y) * 200 + x));
					}
				}
				miniMapView.setImage(imgData);
				((SWTDisplaySystem) display).setCurrentGLCanvas(getGlCanvas());
			}
			minimapCounter++;

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#update(float)
	 */
	@Override
	protected void update(float interpolation) {
		super.update(interpolation);
		if (sceneData != null) {
			performCameraMotion();
			GameStateManager.getInstance().update(interpolation);
			sceneData.getRootNode().updateGeometricState(interpolation, true);
			if (WorldStates.Running.equals(worldState)) {
				sceneData.getCollisionHandler().update(interpolation);
				sceneData.getPhysicsSpace().update(interpolation);
				fieldService.checkFields();
			}
			repeater.doUpdate(interpolation);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		getUpdateQueue().enqueue(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getRootNode().detachAllChildren();
				return null;
			}

		});
		stopRendering();
	}

	/**
	 * Make walls (in)visible according to their position relative to the
	 * camera.
	 */
	public void showHideWalls() {
		// show all walls
		showWall(Direction.NORTH);
		showWall(Direction.SOUTH);
		showWall(Direction.EAST);
		showWall(Direction.WEST);
		// hide east/west wall
		if (cameraService.getMainCamera().getDirection().x > 0) {
			hideWall(Direction.WEST);
		} else if (cameraService.getMainCamera().getDirection().x < 0) {
			hideWall(Direction.EAST);
		}

		// hide north/south wall
		if (cameraService.getMainCamera().getDirection().z < 0) {
			hideWall(Direction.SOUTH);
		} else if (cameraService.getMainCamera().getDirection().z > 0) {
			hideWall(Direction.NORTH);
		}
	}

	/**
	 * @param dir
	 *            direction specifying which wall to hide
	 */
	public void hideWall(final Direction dir) {
		sceneDataService.getWalls().get(dir).setRenderState(wallAlpha);
		sceneDataService.getWalls().get(dir).setRenderQueueMode(
				Renderer.QUEUE_OPAQUE);
		sceneDataService.getWalls().get(dir).updateRenderState();
	}

	/**
	 * @param dir
	 *            direction specifying which wall to show
	 */
	public void showWall(Direction dir) {
		sceneDataService.getWalls().get(dir).clearRenderState(
				RenderState.RS_ALPHA);
		sceneDataService.getWalls().get(dir).setRenderQueueMode(
				Renderer.QUEUE_SKIP);
		sceneDataService.getWalls().get(dir).updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneDataNew) {
		this.sceneData = sceneDataNew;
		worldState = WorldStates.Paused;
		getUpdateQueue().enqueue(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				sceneData.getRootNode().attachChild(sceneData.getRoomNode());
				getRootNode().attachChild(sceneData.getRootNode());
				showHideWalls();
				getRootNode().updateRenderState();
				if (offy.isSupported()) {
					offy.setBackgroundColor(new ColorRGBA(.667f, .667f, .851f,
							1f));
					offy.getCamera().setLocation(
							new Vector3f(sceneData.getWidth() / 2, 2, sceneData
									.getWidth() / 2));
					offy.getCamera()
							.setDirection(new Vector3f(0f, -1f, -.001f));
					offy.getCamera().setParallelProjection(true);
					offy.getCamera().setFrustum(-100.0f, 1000.0f,
							-.6f * sceneData.getWidth(),
							.6f * sceneData.getWidth(),
							-.6f * sceneData.getWidth(),
							.6f * sceneData.getWidth());
				}
				createGrid();
				return null;
			}

		});
		resumeRendering();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		List<VisualEntity> newHighlights = ((IStructuredSelection) event
				.getSelection()).toList();
		List<VisualEntity> unlit = new ArrayList<VisualEntity>();
		for (VisualEntity entity : newHighlights) {
			if (!hilited.contains(entity)) {
				hilited.add(entity);
				entity.hilite(fragmentProgramState, alphaState);
				entity.getNode().updateRenderState();
			}
		}
		for (VisualEntity entity : hilited) {
			if (!newHighlights.contains(entity)) {
				unlit.add(entity);
				entity.clearHilite();
				entity.getNode().updateRenderState();
			}
		}
		hilited.removeAll(unlit);
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
			cam.setLocation(cam.getLocation()
					.add(new Vector3f(-keyspeed, 0, 0)));
		}
		if (updownleftright[3]) {
			cam
					.setLocation(cam.getLocation().add(
							new Vector3f(keyspeed, 0, 0)));
		}
		if (updownleftright[0]) {
			cam.setLocation(cam.getLocation()
					.add(new Vector3f(0, 0, -keyspeed)));
		}
		if (updownleftright[1]) {
			cam
					.setLocation(cam.getLocation().add(
							new Vector3f(0, 0, keyspeed)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#pause()
	 */
	@Override
	public void pause() {
		// updateThread.setPaused(true);
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
	 * @see org.rifidi.services.registry.core.world.WorldService#run()
	 */
	@Override
	public void run() {
		for (Entity entity : sceneData.getSearchableEntities()) {
			if (entity instanceof SceneControl) {
				((SceneControl) entity).start();
			}
		}
		worldState = WorldStates.Running;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.world.WorldService#stop()
	 */
	@Override
	public void stop() {
		GameTaskQueueManager.getManager().update(new Callable<Object>() {

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
		worldState = WorldStates.Stopped;
	}

	public void toggleGrid() {
		if (gridEnabled) {
			gridState.setActive(false);
			gridEnabled = false;
		} else {
			gridState.setActive(true);
			gridEnabled = true;
		}
	}

	/**
	 * Creates the grid for displaying.
	 */
	private void createGrid() {
		if (gridState != null) {
			GameStateManager.getInstance().detachChild(gridState);
		}
		// create the grid
		logger.debug("creating grid");
		int sceneWidth = sceneDataService.getWidth();
		GridNode grid = new GridNode("griddy", sceneWidth, sceneWidth, .1f);
		grid.setLocalTranslation(sceneWidth / 2, 0.01f, sceneWidth / 2);

		MaterialState ms = DisplaySystem.getDisplaySystem().getRenderer()
				.createMaterialState();
		ms.setDiffuse(new ColorRGBA(0.345f, 0.639f, 0.901f, 1.0f));
		grid.setRenderState(ms);

		gridNode = grid;
		gridState = new BasicGameState("GridState");
		gridState.getRootNode().attachChild(grid);
		gridState.setActive(true);
		gridNode.updateRenderState();
		gridNode.updateGeometricState(0, true);
		GameStateManager.getInstance().attachChild(gridState);
		gridEnabled = false;
		toggleGrid();
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
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
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
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
		selectionService.addSelectionChangedListener(this);
	}

	/**
	 * @param fieldService the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	/**
	 * Action that is submitted to the update thread to keep the highlights
	 * pulsing.
	 * 
	 * 
	 * @author Jochen Mader Feb 1, 2008
	 * @tags
	 * 
	 */
	private class HilitedRepeatedUpdateAction implements RepeatedUpdateAction {
		/**
		 * Maximum alpha value.
		 */
		private Float maxAlpha = 1f;
		/**
		 * Minimum alpha value.
		 */
		private Float minAlpha = .25f;
		/**
		 * Base alpha value
		 */
		private Float alpha = 1f;
		/**
		 * Signum.
		 */
		private Integer sign = 1;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.rifidi.services.registry.core.world.RepeatedUpdateAction#doUpdate(float)
		 */
		@Override
		public void doUpdate(float timePassed) {
			timePassed *= sign;
			alpha += timePassed;
			if (alpha <= minAlpha) {
				sign = 1;
				alpha = minAlpha;
			} else if (alpha >= maxAlpha) {
				sign = -1;
				alpha = maxAlpha;
			}
			// Dynamic update
			fragmentProgramState.setParameter(new float[] { 0f, 0f, alpha,
					alpha }, 1);
			fragmentProgramState.setNeedsRefresh(true);
		}

	}
}
