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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.rifidi.designer.services.core.highlighting.HighlightingService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.world.CommandStateService;
import org.rifidi.designer.services.core.world.RepeatedUpdateAction;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.designer.services.core.world.WorldStates;
import org.rifidi.jmeswt.SWTBaseGame;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.utilities.grid.GridNode;

import com.jme.bounding.BoundingBox;
import com.jme.light.DirectionalLight;
import com.jme.light.LightNode;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Spatial;
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
		WorldService, CommandStateService, HighlightingService {
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
	 * Map of highlighted entities with the highlighting color as the key.
	 */
	private Map<ColorRGBA, List<VisualEntity>> hilited;
	/**
	 * Map of fragment programs by their color.
	 */
	private Map<ColorRGBA, FragmentProgramState> fragmentPrograms;
	/**
	 * Reference to the selectionservice.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the field service.
	 */
	private FieldService fieldService;

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
	 * List that contains all the spatials that should be rendered by the
	 * offscreen renderer.
	 */
	private List<Spatial> offscreenRenderSpatials;

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
		hilited = new HashMap<ColorRGBA, List<VisualEntity>>();
		hilited.put(ColorRGBA.blue, new ArrayList<VisualEntity>());
		fragmentPrograms = new HashMap<ColorRGBA, FragmentProgramState>();

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
		offscreenRenderSpatials = new ArrayList<Spatial>();
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

		fragmentPrograms.put(ColorRGBA.blue,
				createNewFragmentProgramState(ColorRGBA.blue));

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

	private FragmentProgramState createNewFragmentProgramState(ColorRGBA color) {
		// create fragment program state for highlighting
		FragmentProgramState fragmentProgramState = DisplaySystem
				.getDisplaySystem().getRenderer().createFragmentProgramState();
		fragmentProgramState.load(fragprog);
		fragmentProgramState.setEnabled(true);
		float[] color4f = new float[] { color.r, color.g, color.b, 0f };
		fragmentProgramState.setParameter(color4f, 0);
		return fragmentProgramState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#render(float)
	 */

	@Override
	protected void render(float interpolation) {
		if (sceneData != null && !getGlCanvas().isDisposed()
				&& sceneData.getRootNode().getParent() != null) {
			GameStateManager.getInstance().render(0);
			if (GlobalProperties.physicsDebugging) {
				PhysicsDebugger.drawPhysics(sceneData.getPhysicsSpace(),
						display.getRenderer());
			}
			if (GlobalProperties.boundingDebugging) {
				Debugger.drawBounds(sceneData.getRootNode(), display
						.getRenderer());
			}
			display.getRenderer().displayBackBuffer();
			getGlCanvas().swapBuffers();
			if (miniMapView == null) {
				miniMapView = (MiniMapView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().findView(
								MiniMapView.ID);
			}
			if (offy.isSupported() && miniMapView != null
					&& minimapCounter == 10) {
				miniMapView.setMapCamera(offy.getCamera());
				minimapCounter = 0;
				cameraService.setLOD(3);
				offscreenRenderSpatials.clear();
				offscreenRenderSpatials.add(sceneData.getRoomNode());
				for (Entity entity : sceneData.getEntities()) {
					if (entity instanceof VisualEntity
							&& ((VisualEntity) entity).getNode() != null) {
						offscreenRenderSpatials.add(((VisualEntity) entity)
								.getNode());
					}
				}
				offy.render((ArrayList<Spatial>)offscreenRenderSpatials);
				cameraService.resetLOD();
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
				miniMapView.setImage(imgData, 20);
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
				// showHideWalls();
				getRootNode().updateRenderState();
				if (offy.isSupported()) {
					offy.setBackgroundColor(new ColorRGBA(.667f, .667f, .851f,
							1f));
					offy.getCamera().setLocation(
							new Vector3f(((BoundingBox) sceneData.getRoomNode()
									.getWorldBound()).xExtent, 2,
									((BoundingBox) sceneData.getRoomNode()
											.getWorldBound()).zExtent));
					offy.getCamera()
							.setDirection(new Vector3f(0f, -1f, -.001f));
					offy.getCamera().setParallelProjection(true);
					offy.getCamera().setFrustum(
							-100.0f,
							1000.0f,
							-.6f
									* ((BoundingBox) sceneData.getRoomNode()
											.getWorldBound()).xExtent * 2,
							.6f * ((BoundingBox) sceneData.getRoomNode()
									.getWorldBound()).xExtent * 2,
							-.6f
									* ((BoundingBox) sceneData.getRoomNode()
											.getWorldBound()).zExtent * 2,
							.6f * ((BoundingBox) sceneData.getRoomNode()
									.getWorldBound()).zExtent * 2);
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
		Set<VisualEntity> newHighlights = new HashSet<VisualEntity>();
		Set<VisualEntity> unlit = new HashSet<VisualEntity>();
		Iterator<VisualEntity> iter = ((IStructuredSelection) event
				.getSelection()).iterator();
		while (iter.hasNext()) {
			VisualEntity entity = iter.next();
			hilited.get(ColorRGBA.blue).add(entity);
			newHighlights.add(entity);
		}
		changeHighlighting(ColorRGBA.blue, newHighlights);
		hilited.get(ColorRGBA.blue).removeAll(unlit);
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
		int gridX = (int) ((BoundingBox) sceneDataService.getCurrentSceneData()
				.getRoomNode().getWorldBound()).xExtent * 2;
		int gridZ = (int) ((BoundingBox) sceneDataService.getCurrentSceneData()
				.getRoomNode().getWorldBound()).zExtent * 2;
		// one grid is 1 unit!!!
		gridX = Math.floor(((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound()).xExtent) < ((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound()).xExtent ? gridX + 1
				: gridX;
		gridZ = Math.floor(((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound()).zExtent) < ((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound()).zExtent ? gridZ + 1
				: gridZ;
		GridNode grid = new GridNode("griddy", gridX, gridZ, .1f);
		grid.setLocalTranslation(((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound())
				.getCenter().x, 0.01f, ((BoundingBox) sceneDataService
				.getCurrentSceneData().getRoomNode().getWorldBound())
				.getCenter().z);

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
	 * @param fieldService
	 *            the fieldService to set
	 */
	@Inject
	public void setFieldService(FieldService fieldService) {
		this.fieldService = fieldService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.highlighting.HighlightingService#changeHighlighting(com.jme.renderer.ColorRGBA,
	 *      java.util.Set)
	 */
	@Override
	public void changeHighlighting(ColorRGBA color, Set<VisualEntity> highlight) {
		if (!fragmentPrograms.containsKey(color)) {
			fragmentPrograms.put(color, createNewFragmentProgramState(color));
			hilited.put(color, new ArrayList<VisualEntity>());
		}
		ArrayList<VisualEntity> unlit = new ArrayList<VisualEntity>();
		for (VisualEntity entity : hilited.get(color)) {
			if (!highlight.contains(entity)) {
				unlit.add(entity);
			}
		}
		hilited.get(color).clear();
		hilited.get(color).addAll(highlight);

		for (VisualEntity entity : highlight) {
			Node hilit = entity.getBoundingNode();
			hilit.setCullMode(SceneElement.CULL_DYNAMIC);
			hilit.setRenderState(fragmentPrograms.get(color));
			hilit.setRenderState(alphaState);
			hilit.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
			entity.getNode().updateRenderState();
		}
		for (VisualEntity entity : unlit) {
			entity.getBoundingNode().setCullMode(SceneElement.CULL_ALWAYS);
			entity.getBoundingNode().clearRenderState(
					RenderState.RS_FRAGMENT_PROGRAM);
			entity.getNode().updateRenderState();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.highlighting.HighlightingService#changeHighlightColor(com.jme.renderer.ColorRGBA,
	 *      com.jme.renderer.ColorRGBA, java.util.Set)
	 */
	@Override
	public void changeHighlightColor(ColorRGBA color, ColorRGBA newcolor,
			Set<VisualEntity> hilight) {
		hilited.get(color).removeAll(hilight);
		if (!fragmentPrograms.containsKey(newcolor)) {
			fragmentPrograms.put(newcolor,
					createNewFragmentProgramState(newcolor));
			hilited.put(newcolor, new ArrayList<VisualEntity>());
		}
		hilited.get(newcolor).addAll(hilight);
		for (VisualEntity target : hilight) {
			target.getBoundingNode().clearRenderState(
					RenderState.RS_FRAGMENT_PROGRAM);
			target.getBoundingNode().setRenderState(
					fragmentPrograms.get(newcolor));
			target.getNode().updateRenderState();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.highlighting.HighlightingService#clearAllHighlights()
	 */
	@Override
	public void clearAllHighlights() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.highlighting.HighlightingService#clearHighlights(java.util.Set)
	 */
	@Override
	public void clearHighlights(Set<VisualEntity> hilight) {
		for (ColorRGBA color : hilited.keySet()) {
			for (VisualEntity entity : hilited.get(color)) {
				if (hilight.contains(entity)) {
					entity.getBoundingNode().setCullMode(
							SceneElement.CULL_ALWAYS);
					entity.getBoundingNode().clearRenderState(
							RenderState.RS_FRAGMENT_PROGRAM);
					entity.getNode().updateRenderState();
				}
			}
			hilited.get(color).removeAll(hilight);
		}
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
			for (FragmentProgramState fragmentProgramState : fragmentPrograms
					.values()) {
				fragmentProgramState.setParameter(new float[] { 0f, 0f, alpha,
						alpha }, 1);
				fragmentProgramState.setNeedsRefresh(true);
			}
		}

	}

}
