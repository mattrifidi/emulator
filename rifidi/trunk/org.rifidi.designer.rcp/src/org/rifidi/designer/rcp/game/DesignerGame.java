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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.monklypse.core.SWTDefaultImplementor;
import org.monklypse.core.renderer.LWJGLOffscreenRenderer;
import org.monklypse.core.renderer.OffscreenRenderer;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.entities.interfaces.SceneControl;
import org.rifidi.designer.rcp.GlobalProperties;
import org.rifidi.designer.rcp.views.minimapview.MiniMapView;
import org.rifidi.designer.services.core.camera.ZoomableLWJGLCamera;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.entities.EntitiesServiceImpl;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.highlighting.HighlightingService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.world.CommandStateService;
import org.rifidi.designer.services.core.world.RepeatedUpdateAction;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.designer.services.core.world.WorldStates;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.utilities.grid.GridNode;

import com.jme.bounding.BoundingBox;
import com.jme.light.DirectionalLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.lwjgl.LWJGLRenderer;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.BlendState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FragmentProgramState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;
import com.jmex.swt.lwjgl.LWJGLSWTCanvas;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 28, 2008
 * 
 */
public class DesignerGame extends SWTDefaultImplementor implements
		SceneDataChangedListener, ISelectionChangedListener, KeyListener,
		WorldService, CommandStateService, HighlightingService {

	private static final Log logger = LogFactory.getLog(DesignerGame.class);
	/** Wall transparency. TODO redo trans stuff */
	private BlendState wallAlpha;
	/** Zbuffer for the rootnode. */
	private ZBufferState zbufferState;
	/** The primary lightstate. */
	private LightState ls;
	/** Currently used scenedata. */
	private SceneData sceneData;
	/** Reference to the scenedataservice. */
	private SceneDataService sceneDataService;
	/** Offscreenrenderer for the minimap. */
	private OffscreenRenderer offy;
	/** Reference to the minimap. */
	private MiniMapView miniMapView;
	/** Display minimap every nth frame. */
	private int minimapCounter = 10;
	/** ImageData for the minimap. */
	private ImageData imgData;
	/** Alpha state used for highlighting. */
	private BlendState alphaState;
	/** Fragment program used for highlighting. */
	private String fragprog = "!!ARBfp1.0"
			+ "MOV result.color, program.local[0];"
			+ "MOV result.color.a, program.local[1].a;" + "END";
	/** Action submitted to the update thread for updating the highlight state. */
	private HilitedRepeatedUpdateAction repeater;
	/** Map of highlighted entities with the highlighting color as the key. */
	private Map<ColorRGBA, List<VisualEntity>> hilited;
	/** Map of fragment programs by their color. */
	private Map<ColorRGBA, FragmentProgramState> fragmentPrograms;
	/** Reference to the field service. */
	private FieldService fieldService;
	/** The current state of the world. */
	private WorldStates worldState;
	/** Maps WorldStates to command names. */
	private Map<WorldStates, List<String>> stateMap;
	/** Boolean indicators for camera directional motion */
	private boolean[] updownleftright = new boolean[4];

	/** Scene related stuff. */

	private boolean gridEnabled = true;
	/** Node for the grid. */
	private GridNode gridNode;
	/** Grid game state. */
	private BasicGameState gridState;
	/**
	 * List that contains all the spatials that should be rendered by the
	 * offscreen renderer.
	 */
	private List<Spatial> offscreenRenderSpatials;
	/** LOD of the main camera. */
	private int sceneLOD;
	/** Cursors for the canvas. */
	private Cursor invisibleCursor, defaultCursor;

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public DesignerGame(String name, int width, int height) {
		super(width, height);

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

	/**
	 * Overwritten to enable LOD.
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#doRender()
	 */
	@Override
	public void doRender() {
		if (sceneLOD != ((ZoomableLWJGLCamera) getCamera()).getLod()) {
			if (sceneDataService.getCurrentSceneData() != null) {
				sceneLOD = ((ZoomableLWJGLCamera) getCamera()).getLod();
				for (Entity entity : sceneDataService.getCurrentSceneData()
						.getEntities()) {
					if (entity instanceof VisualEntity) {
						((VisualEntity) entity).setLOD(sceneLOD);
					}
				}
			}
		}
		super.doRender();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleRender()
	 */
	// TODO: remove!!
	private Node bu = null;

	@Override
	public void simpleRender() {
		if (sceneData != null && !getCanvas().isDisposed()
				&& sceneData.getRootNode().getParent() != null) {
			GameStateManager.getInstance().render(0);
			if (GlobalProperties.physicsDebugging) {
				PhysicsDebugger.drawPhysics(sceneData.getPhysicsSpace(),
						getRenderer());
			}
			if (GlobalProperties.boundingDebugging) {
				if (bu == null) {
					bu = ((EntitiesServiceImpl) sceneDataService)
							.getRoomOctree().getTreeAsNode();
				}
				Debugger.drawBounds(bu, getRenderer());
				Debugger.drawBounds(sceneData.getRootNode(), getRenderer());
			}
			getRenderer().displayBackBuffer();
			getCanvas().swapBuffers();
			if (miniMapView == null) {
				miniMapView = (MiniMapView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().findView(
								MiniMapView.ID);
			}
			if (offy.isSupported() && miniMapView != null
					&& minimapCounter == 10) {
				miniMapView.setMapCamera(offy.getCamera());
				minimapCounter = 0;
				sceneLOD = 3;
				for (Entity entity : sceneDataService.getCurrentSceneData()
						.getEntities()) {
					if (entity instanceof VisualEntity) {
						((VisualEntity) entity).setLOD(sceneLOD);
					}
				}
				offscreenRenderSpatials.clear();
				offy.render(sceneData.getRootNode());
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
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleSetup()
	 */
	@Override
	public void simpleSetup() {
		setCamera(new ZoomableLWJGLCamera(this, 754, 584));
		getCanvas().addKeyListener(this);
		// clear keylist if the canvas loses the focus
		getCanvas().addFocusListener(new FocusListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.FocusListener#focusGained(org.eclipse.
			 * swt.events.FocusEvent)
			 */
			@Override
			public void focusGained(FocusEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
			 */
			@Override
			public void focusLost(FocusEvent e) {
				updownleftright[0] = false;
				updownleftright[1] = false;
				updownleftright[2] = false;
				updownleftright[3] = false;
			}

		});

		offy = new LWJGLOffscreenRenderer(200, 200,
				(LWJGLRenderer) getRenderer());
		if (offy.isSupported()) {
			EXTFramebufferObject.glBindFramebufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		} else {
			logger.debug("Offscreen rendering is not supported!");
		}

		GameStateManager.create();
		// alphastate for transparent walls
		wallAlpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		wallAlpha.setBlendEnabled(true);
		wallAlpha.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		wallAlpha
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		wallAlpha.setEnabled(true);
		// create ZBuffer stuff
		zbufferState = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		zbufferState.setFunction(ZBufferState.TestFunction.LessThanOrEqualTo);
		zbufferState.setEnabled(true);

		// create cullstate for backface culling
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cullState.setCullFace(CullState.Face.Back);

		// create alpha state for highlighting
		alphaState = DisplaySystem.getDisplaySystem().getRenderer()
				.createBlendState();
		alphaState.setBlendEnabled(true);
		alphaState
				.setDestinationFunction(BlendState.DestinationFunction.OneMinusSourceAlpha);
		alphaState.setSourceFunction(BlendState.SourceFunction.SourceAlpha);
		alphaState.setEnabled(true);

		fragmentPrograms.put(ColorRGBA.blue,
				createNewFragmentProgramState(ColorRGBA.blue));

		repeater = new HilitedRepeatedUpdateAction();

		// create a default light
		ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		ls.setTwoSidedLighting(false);
		ls.setEnabled(true);

		DirectionalLight light = new DirectionalLight();
		light.setAmbient(new ColorRGBA(1f, 1f, 1f, 0f));
		light.setEnabled(true);
		ls.attach(light);

		light = new DirectionalLight();
		light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 0f));
		light.setAmbient(new ColorRGBA(0f, 0f, 0f, 0f));
		light.setDirection(new Vector3f(0, -1, 0));
		light.setEnabled(true);
		ls.attach(light);

		light = new DirectionalLight();
		light.setDiffuse(new ColorRGBA(.4f, .4f, .4f, 0f));
		light.setAmbient(new ColorRGBA(0f, 0f, 0f, 0f));
		light.setDirection(new Vector3f(1, 0, 0));
		light.setEnabled(true);
		ls.attach(light);

		// light = new DirectionalLight();
		// light.setDiffuse(new ColorRGBA(1.0f, 1.0f, 1.0f, 0f));
		// light.setAmbient(new ColorRGBA(0f, 0f, 0f, 0f));
		// light.setDirection(new Vector3f(0, 0, 1));
		// light.setEnabled(true);
		// ls.attach(light);

		InputStream arb = this.getClass().getClassLoader().getResourceAsStream(
				"/org/rifidi/designer/rcp/game/phong.arb");
		BufferedReader br = new BufferedReader(new InputStreamReader(arb));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		getRenderer().setBackgroundColor(ColorRGBA.gray.clone());
		// FragmentProgramState
		// phong=display.getRenderer().createFragmentProgramState();
		// phong.load(sb.toString());
		// phong.setParameter(new float[]{.5f,.5f,.5f,.5f}, 0);
		// phong.setParameter(new float[]{1f,1f,1f,1f}, 1);
		// phong.setEnabled(true);
		getRootNode().setRenderState(zbufferState);
		getRootNode().setRenderState(cullState);
		getRootNode().setRenderState(ls);
		getRootNode().updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleUpdate()
	 */
	@Override
	public void simpleUpdate() {
		if (sceneData != null) {
			performCameraMotion();
			GameStateManager.getInstance().update(tpf);
			sceneData.getRootNode().updateGeometricState(tpf, true);
			if (WorldStates.Running.equals(worldState)) {
				sceneData.getCollisionHandler().update(tpf);
				sceneData.getPhysicsSpace().update(tpf);
				fieldService.checkFields();
			}
			repeater.doUpdate(tpf);
		}
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
	 * @seeorg.rifidi.designer.services.core.entities.SceneDataChangedListener#
	 * destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		update(new Callable<Object>() {

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
	}

	/**
	 * @param dir
	 *            direction specifying which wall to show
	 */
	public void showWall(Direction dir) {
		sceneDataService.getWalls().get(dir).clearRenderState(
				RenderState.RS_BLEND);
		sceneDataService.getWalls().get(dir).setRenderQueueMode(
				Renderer.QUEUE_SKIP);
		sceneDataService.getWalls().get(dir).updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.services.core.entities.SceneDataChangedListener#
	 * sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneDataNew) {
		this.sceneData = sceneDataNew;
		worldState = WorldStates.Paused;
		update(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				sceneData.getRootNode().attachChild(sceneData.getRoomNode());
				sceneData.getRootNode().setName("rootnode");
				sceneData.getRoomNode().setName("roomnode");
				getRootNode().attachChild(sceneData.getRootNode());
				// adjust main camera
				getCamera().setLocation(
						new Vector3f((int) (((BoundingBox) sceneData
								.getRoomNode().getWorldBound()).xExtent / 2),
								2, (int) ((BoundingBox) sceneData.getRoomNode()
										.getWorldBound()).zExtent / 2));
				getCamera().getLocation().addLocal(new Vector3f(-5, 0, -15));
				((ZoomableLWJGLCamera) getCamera()).adjustLOD();
				getCamera().update();
				// adjust minimpa camera
				if (offy.isSupported()) {
					offy.setBackgroundColor(new ColorRGBA(.667f, .667f, .851f,
							1f));
					offy.getCamera().setLocation(
							new Vector3f(((BoundingBox) sceneData.getRoomNode()
									.getWorldBound()).xExtent, 2,
									((BoundingBox) sceneData.getRoomNode()
											.getWorldBound()).zExtent));
					offy.getCamera().setParallelProjection(true);

					offy.getCamera().setAxes(new Vector3f(-1, 0, 0),
							new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));

					float ratio = ((BoundingBox) sceneData.getRoomNode()
							.getWorldBound()).xExtent
							/ ((BoundingBox) sceneData.getRoomNode()
									.getWorldBound()).zExtent;
					float sidelength = 0;
					if (ratio > 1) {
						sidelength = ((BoundingBox) sceneData.getRoomNode()
								.getWorldBound()).xExtent;
					} else {
						sidelength = ((BoundingBox) sceneData.getRoomNode()
								.getWorldBound()).zExtent;
					}
					offy.getCamera().setFrustum(-100.0f, 1000.0f,
							-.6f * sidelength * 2, .6f * sidelength * 2,
							-.6f * sidelength * 2, .6f * sidelength * 2);

					offy.getCamera().update();
					offy.getCamera().apply();
				}
				createGrid();
				getRootNode().updateRenderState();
				return null;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@SuppressWarnings("unchecked")
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
	 * @see
	 * org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.
	 * KeyEvent)
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
	 * @see
	 * org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events
	 * .KeyEvent)
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
		ZoomableLWJGLCamera cam = (ZoomableLWJGLCamera) DisplaySystem
				.getDisplaySystem().getRenderer().getCamera();
		float keyspeed = 0.9f + (cam.getZoomlevel() / 20);
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
		if (sceneData != null) {
			for (Entity entity : sceneData.getSearchableEntities()) {
				if (entity instanceof SceneControl) {
					((SceneControl) entity).pause();
				}
			}
			worldState = WorldStates.Paused;
		}
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
		update(new Callable<Object>() {

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
	 * @see
	 * org.rifidi.services.registry.core.world.CommandStateService#isEnabled
	 * (java.lang.String)
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
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
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
	 * @seeorg.rifidi.designer.services.core.highlighting.HighlightingService#
	 * changeHighlighting(com.jme.renderer.ColorRGBA, java.util.Set)
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
			// for those who forget to add the bounding volume
			if (hilit != null) {
				hilit.setCullHint(Spatial.CullHint.Dynamic);
				if (hilit.getChild("hiliter") != null) {
					Spatial hilite = (Spatial) hilit.getChild("hiliter");
					hilite.setRenderState(fragmentPrograms.get(color));
					hilite.setRenderState(alphaState);
					hilite.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
				} else {
					hilit.setRenderState(fragmentPrograms.get(color));
					hilit.setRenderState(alphaState);
					hilit.setRenderQueueMode(Renderer.QUEUE_OPAQUE);
				}
				hilit.updateRenderState();
			}
		}
		for (VisualEntity entity : unlit) {
			Node hilit = entity.getBoundingNode();
			// for those who forget to add the bounding volume
			if (hilit != null) {
				hilit.setCullHint(Spatial.CullHint.Always);
				if (hilit.getChild("hiliter") != null) {
					Spatial hilite = (Spatial) hilit.getChild("hiliter");
					hilite.clearRenderState(RenderState.RS_FRAGMENT_PROGRAM);
				} else {
					hilit.clearRenderState(RenderState.RS_FRAGMENT_PROGRAM);
				}
				hilit.updateRenderState();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.services.core.highlighting.HighlightingService#
	 * changeHighlightColor(com.jme.renderer.ColorRGBA,
	 * com.jme.renderer.ColorRGBA, java.util.Set)
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
	 * @seeorg.rifidi.designer.services.core.highlighting.HighlightingService#
	 * clearAllHighlights()
	 */
	@Override
	public void clearAllHighlights() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.designer.services.core.highlighting.HighlightingService#
	 * clearHighlights(java.util.Set)
	 */
	@Override
	public void clearHighlights(Set<VisualEntity> hilight) {
		for (ColorRGBA color : hilited.keySet()) {
			for (VisualEntity entity : hilited.get(color)) {
				if (hilight.contains(entity)) {
					entity.getBoundingNode().setCullHint(
							Spatial.CullHint.Always);
					entity.getBoundingNode().clearRenderState(
							RenderState.RS_FRAGMENT_PROGRAM);
					entity.getNode().updateRenderState();
				}
			}
			hilited.get(color).removeAll(hilight);
		}
	}

	/**
	 * Set the LOD for the scene. Can be undone using resetLOD.
	 * 
	 * @param lod
	 */
	public void setLOD(int lod) {
		if (sceneDataService.getCurrentSceneData() != null) {
			for (Entity entity : sceneDataService.getCurrentSceneData()
					.getEntities()) {
				if (entity instanceof VisualEntity) {
					((VisualEntity) entity).setLOD(lod);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.JMECanvasImplementor2#resizeCanvas(int, int)
	 */
	@Override
	public void resizeCanvas(int width, int height) {
		// don't resize if the canvas was hidden
		if (width == 0 && height == 0) {
			return;
		}
		if (width > height) {
			getRenderer().reinit(width, (int) (width * .8));
			return;
		}
		getRenderer().reinit((int) (height * .8), height);
	}

	public void showMouse() {
		getCanvas().setCursor(defaultCursor);
	}

	public void hideMouse() {
		getCanvas().setCursor(invisibleCursor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.JMECanvasImplementor2#setCanvas(java.lang.Object)
	 */
	@Override
	public void setCanvas(LWJGLSWTCanvas canvas) {
		super.setCanvas(canvas);

		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		PaletteData palette = new PaletteData(new RGB[] { white.getRGB(),
				black.getRGB() });
		ImageData sourceData = new ImageData(16, 16, 1, palette);
		sourceData.transparentPixel = 0;
		invisibleCursor = new Cursor(Display.getCurrent(), sourceData, 0, 0);
		defaultCursor = canvas.getCursor();
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
		 * @see
		 * org.rifidi.services.registry.core.world.RepeatedUpdateAction#doUpdate
		 * (float)
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