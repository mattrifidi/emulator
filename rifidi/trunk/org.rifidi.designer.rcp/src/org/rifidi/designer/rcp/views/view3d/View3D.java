/*
 *  View3D.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityLibraryReference;
import org.rifidi.designer.library.EntityWizardIface;
import org.rifidi.designer.library.EntityWizardRifidiIface;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.rcp.views.view3d.listeners.Editor3DDropTargetListener;
import org.rifidi.designer.rcp.views.view3d.listeners.EntityMouseMoveListener;
import org.rifidi.designer.rcp.views.view3d.listeners.MouseMoveEntityListener;
import org.rifidi.designer.rcp.views.view3d.listeners.MousePickListener;
import org.rifidi.designer.rcp.views.view3d.listeners.ResizeListener;
import org.rifidi.designer.rcp.views.view3d.listeners.WatchAreaDrawMouseListener;
import org.rifidi.designer.rcp.views.view3d.listeners.ZoomMouseWheelListener;
import org.rifidi.designer.rcp.views.view3d.mode.InteractionMode;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.utilities.grid.GridNode;

import com.jme.light.DirectionalLight;
import com.jme.light.LightNode;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

/**
 * This monster baby is the view that displays the 3d scene.
 * 
 * @author Jochen Mader Nov 20, 2007
 * @author Dan West
 */
public class View3D extends ViewPart implements IPerspectiveListener {
	/**
	 * Enum for the different modes the 3dview supports.
	 */
	public static enum Mode {
		PickMode, CameraMode, PlacementMode, WatchAreaMode, MoveMode
	};

	/**
	 * Ugly but required for rotating entites while moving them.
	 */
	public static boolean moving = false;

	/**
	 * The currently active mode.
	 */
	private static Mode currentMode;

	private Map<Mode, InteractionMode> modeMap;

	/**
	 * ID.
	 */
	public static final String ID = "org.rifidi.designer.rcp.views.View3D";
	/**
	 * Logger.
	 */
	private static Log logger = LogFactory.getLog(View3D.class);
	/**
	 * Scene related stuff.
	 */
	private boolean gridEnabled = true;
	/**
	 * Wall transparency. TODO redo trans stuff
	 */
	private AlphaState wallAlpha;
	/**
	 * The rendering canvas.
	 */
	private GLCanvas glCanvas;
	// private Composite composite;

	/**
	 * Invisible cursor.
	 */
	private Cursor invisibleCursor;
	/**
	 * Visible cursor.
	 */
	private Cursor defaultCursor;
	/**
	 * The scenedata service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * The world service.
	 */
	private WorldService worldService;
	/**
	 * The selection service.
	 */
	private SelectionService selectionService;
	/**
	 * The entity service.
	 */
	private EntitiesService entitiesService;
	/**
	 * The finder service.
	 */
	private FinderService finderService;
	/**
	 * The camera service.
	 */
	private CameraService cameraService;

	// miscellany & NEW STUFF
	private Point oldpos;
	private GridNode gridNode;
	private BasicGameState gridState;

	private static boolean initialized = false;

	/**
	 * 
	 */
	public View3D() {
		Activator.display=Display.getCurrent();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		getViewSite().getWorkbenchWindow().addPerspectiveListener(this);
		TextureManager.clearCache();

		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new FillLayout());
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.depthSize = 24;
		glCanvas = new GLCanvas(composite, SWT.NONE, data);
		// let glcanvas have focus by default
		glCanvas.forceFocus();
		// create an invisible cursor and store a reference to the default
		// cursor
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		PaletteData palette = new PaletteData(new RGB[] { white.getRGB(),
				black.getRGB() });
		ImageData sourceData = new ImageData(16, 16, 1, palette);
		sourceData.transparentPixel = 0;
		invisibleCursor = new Cursor(Display.getCurrent(), sourceData, 0, 0);
		defaultCursor = glCanvas.getCursor();

		glCanvas.addListener(SWT.Resize, new ResizeListener());

		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		mgr.add(new Separator());
		mgr.add(new GroupMarker("modeRadios"));

		DropTarget dt = new DropTarget(glCanvas, DND.DROP_COPY | DND.DROP_MOVE
				| DND.DROP_LINK | DND.DROP_DEFAULT);
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		dt.setTransfer(transfers);
		dt.addDropListener(new Editor3DDropTargetListener(this));

		GameStateManager.create();

		worldService.setGLCanvas(glCanvas);
		worldService.setDisplay(Display.getCurrent());

		getSite().setSelectionProvider(selectionService);

		// alphastate for transparent walls
		wallAlpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		wallAlpha.setBlendEnabled(true);
		wallAlpha.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		wallAlpha.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		wallAlpha.setEnabled(true);

		modeMap = new HashMap<Mode, InteractionMode>();
		modeMap.put(Mode.PlacementMode, new PlacementMode(this));
		// modeMap.put(Mode.CameraMode, new CameraMode(this));
		modeMap.put(Mode.PickMode, new PickMode(this));
		modeMap.put(Mode.WatchAreaMode, new WatchAreaMode(this));
		modeMap.put(Mode.MoveMode, new MoveMode(this));

		if (!initialized) {
			cameraService.createCamera();
		} else {
			switchMode(currentMode);
			initializeScene();
		}
		initialized = true;

		MenuManager menuMgr = new MenuManager();
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(menuMgr, selectionService);

		Control control = glCanvas;
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
	}

	/**
	 * Sets the renderstates of the given node to those attributed to root-level
	 * nodes.
	 * 
	 * @param rootLevel
	 *            the node to give root-level renderstates to
	 */
	private void initNode(final Node rootLevel) {
		// create ZBuffer stuff
		ZBufferState zbufferState = DisplaySystem.getDisplaySystem()
				.getRenderer().createZBufferState();
		zbufferState.setFunction(ZBufferState.CF_LEQUAL);
		zbufferState.setEnabled(true);

		// create cullstate for backface culling
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cullState.setCullMode(CullState.CS_BACK);

		// create a default light
		LightState ls = DisplaySystem.getDisplaySystem().getRenderer()
				.createLightState();
		LightNode lightNode = new LightNode("Head light", ls);
		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1, 1, 1, 1));
		dl.setAmbient(new ColorRGBA(0.4f, 0.4f, 0.4f, 1));
		dl.setDirection(new Vector3f(0.1f, -1, 0.1f));
		dl.setEnabled(true);
		lightNode.setLight(dl);

		rootLevel.setRenderState(zbufferState);
		rootLevel.setRenderState(cullState);
		rootLevel.setRenderState(ls);
		rootLevel.updateRenderState();
	}

	/**
	 * Load a scene from the given IFile.
	 * 
	 * @param file
	 *            the file to load the scene from
	 */
	@SuppressWarnings("unchecked")
	public void loadScene(final IFile file) {
		logger.debug("setting up world");
		sceneDataService.loadScene(file);
		initializeScene();
		cameraService.centerCamera();
		hideWall(Direction.SOUTH);
		hideWall(Direction.EAST);
	}

	/**
	 * Prepare everything to display the currently loaded scene on the canvas.
	 */
	private void initializeScene() {
		// create the room and grid
		createGrid();
		// set root-level renderstates for the root and room nodes
		initNode(sceneDataService.getRootNode());
		initNode(sceneDataService.getRoomNode());

		// enable the grid
		gridEnabled = false;
		toggleGrid();

		switchMode(Mode.PickMode);
		sceneDataService.getRootNode().updateRenderState();
	}

	/**
	 * This method is triggered by the droplistener to allow the editor to fire
	 * the appropriate wizards and add the result to the scene and the list of
	 * entities.
	 * 
	 * @param ref
	 *            the library
	 */
	public void createNewEntity(final EntityLibraryReference ref) {
		worldService.pause();
		try {
			if (ref.getWizard() != null) {
				RifidiEntityWizard wizard = (RifidiEntityWizard) ref
						.getWizard().newInstance();
				if (wizard instanceof EntityWizardRifidiIface) {
					((EntityWizardRifidiIface) wizard).setRMIManager(Activator
							.getDefault().rifidiManager);
				}
				wizard.setTakenNamesList(entitiesService.getEntityNames());
				WizardDialog dialog = new WizardDialog(getSite().getShell(),
						wizard);

				int returnCode = dialog.open();
				if (returnCode != SWT.CANCEL) {
					Entity ent = null;
					if (wizard instanceof EntityWizardRifidiIface) {
						ent = ((EntityWizardRifidiIface) wizard).getEntity();
					} else {
						ent = ((EntityWizardIface) wizard).getEntity();
					}
					entitiesService.addEntity(ent, true);
					if (ent instanceof VisualEntity) {
						selectionService.select((VisualEntity) ent, false,
								false);
						switchMode(Mode.PlacementMode);
					}
				}
				return;
			}
			Entity entity = (Entity) ref.getEntityClass().newInstance();
			entitiesService.addEntity(entity, true);
			if (entity instanceof VisualEntity) {
				selectionService.select((VisualEntity) entity, false, false);
				switchMode(Mode.PlacementMode);
			}

		} catch (InstantiationException e) {
			logger.error("Failed instantiating wizard: \n" + e);
		} catch (IllegalAccessException e) {
			logger.error("Failed instantiating wizard: \n" + e);
		}
	}

	/**
	 * Switch the mode for the 3d view.
	 * 
	 * @param newMode
	 */
	public void switchMode(Mode newMode) {
		logger.debug("switching mode: " + newMode);
		if (currentMode != null) {
			modeMap.get(currentMode).deactivate(glCanvas);
		}
		currentMode = newMode;
		modeMap.get(newMode).activate(glCanvas);
	}

	public Mode getCurrentMode() {
		return currentMode;
	}

	/**
	 * @return the glCanvas
	 */
	public GLCanvas getGlCanvas() {
		return glCanvas;
	}

	/**
	 * Positions for the walls.
	 * 
	 * @author dan
	 */
	public enum Direction {
		NORTH, SOUTH, EAST, WEST, DOWN
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

	/**
	 * Hide the mousepointer in this view.
	 */
	public void hideMousePointer() {
		oldpos = Display.getCurrent().getCursorLocation();
		glCanvas.setCursor(invisibleCursor);
	}

	/**
	 * Show the mousepointer in this view.
	 */
	public void showMousePointer() {
		if (oldpos != null) {
			Display.getCurrent().setCursorLocation(oldpos);
		}
		oldpos = null;
		glCanvas.setCursor(defaultCursor);
	}

	/**
	 * Toggle the state of the grid between enabled and disabled.
	 */
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor)
	 */
	@Override
	public void perspectiveActivated(IWorkbenchPage page,
			IPerspectiveDescriptor perspective) {
		if ("org.rifidi.designer.rcp.perspectives.runtime".equals(perspective
				.getId())) {
			switchMode(Mode.MoveMode);
		} else if ("org.rifidi.designer.rcp.perspectives.designtime"
				.equals(perspective.getId())) {
			switchMode(Mode.PickMode);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveListener#perspectiveChanged(org.eclipse.ui.IWorkbenchPage,
	 *      org.eclipse.ui.IPerspectiveDescriptor, java.lang.String)
	 */
	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
	}

	/**
	 * 
	 * @param cameraService
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
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
	 * @param worldService
	 *            the worldService to set
	 */
	@Inject
	public void setWorldService(WorldService worldService) {
		this.worldService = worldService;
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
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
	 * This is the implementation of the PickMode.
	 * 
	 * @author Dan West - dan@pramari.com
	 */
	public class PickMode extends InteractionMode {
		private MouseMoveEntityListener moveListener;

		/**
		 * Default constructor. Instantiates a new pickMode
		 * 
		 * @param view3d
		 *            the view3d this mode is being initialized for
		 */
		public PickMode(final View3D view3d) {
			moveListener = new MouseMoveEntityListener(view3d, selectionService, sceneDataService);
			MousePickListener pickListener = new MousePickListener(view3d,
					selectionService, sceneDataService, finderService);
			addMouseMoveListener(moveListener);
			addMouseListener(pickListener);
			addKeyListener(pickListener);
			addMouseWheelListener(new ZoomMouseWheelListener(cameraService));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public void activate(Control control) {
			moving = false;
			super.activate(control);
			showMousePointer();
		}

	}

	/**
	 * This is the implementation of the PlacementMode.
	 * 
	 * @author Dan West - dan@pramari.com
	 */
	public class PlacementMode extends InteractionMode {
		/**
		 * Default constructor. Instantiates a new pickMode
		 * 
		 * @param view3d
		 *            the view3d this mode is being initialized for
		 */
		public PlacementMode(final View3D view3d) {
			MouseMoveEntityListener moveListener = new MouseMoveEntityListener(
					view3d, selectionService, sceneDataService);
			addMouseListener(moveListener);
			addMouseMoveListener(moveListener);
			addMouseWheelListener(new ZoomMouseWheelListener(cameraService));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public void activate(Control control) {
			moving = true;
			super.activate(control);

			// hide pointer and start placement
			hideMousePointer();
			for (MouseMoveListener listener : modeMap.get(currentMode)
					.getMouseMoveListeners()) {
				if (listener instanceof MouseMoveEntityListener) {
					((MouseMoveEntityListener) listener).initPlacementMode();
				}
			}
		}

	}

	// /**
	// * This is the implementation of the CameraMode.
	// *
	// * @author Dan West - dan@pramari.com
	// */
	// public class CameraMode extends InteractionMode {
	// /**
	// * Default constructor. Instantiates a new pickMode
	// *
	// * @param view3d
	// * the view3d this mode is being initialized for
	// */
	// public CameraMode(final View3D view3d) {
	// CameraMoveListener cameraListener = new CameraMoveListener(view3d);
	// addMouseMoveListener(cameraListener);
	// addMouseListener(cameraListener);
	// addMouseWheelListener(new ZoomMouseWheelListener());
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.eclipse.swt.widgets.Control)
	// */
	// @Override
	// public void activate(Control control) {
	// moving = false;
	// super.activate(control);
	// ((CameraServiceImpl) cameraService).setActiveCamera("lookCamera");
	// }
	//
	// }

	/**
	 * Mode for drawing watch areas.
	 * 
	 * @author Jochen Mader - jochen@pramari.com
	 */
	public class WatchAreaMode extends InteractionMode {
		/**
		 * Default constructor. Instantiates a new pickMode
		 * 
		 * @param view3d
		 *            the view3d this mode is being initialized for
		 */
		public WatchAreaMode(final View3D view3d) {
			WatchAreaDrawMouseListener watchAreaDrawMouseListener = new WatchAreaDrawMouseListener(
					view3d);
			addMouseMoveListener(watchAreaDrawMouseListener);
			addMouseListener(watchAreaDrawMouseListener);
			addMouseWheelListener(new ZoomMouseWheelListener(cameraService));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public void activate(Control control) {
			moving = false;
			super.activate(control);
			// ((CameraServiceImpl)
			// cameraService).setActiveCamera("lookCamera");
		}
	}

	/**
	 * Mode for moving entities around freely.
	 * 
	 * @author Jochen Mader - jochen@pramari.com
	 */
	public class MoveMode extends InteractionMode {
		/**
		 * Default constructor. Instantiates a new pickMode
		 * 
		 * @param view3d
		 *            the view3d this mode is being initialized for
		 */
		public MoveMode(final View3D view3d) {
			EntityMouseMoveListener entityMouseMoveListener = new EntityMouseMoveListener(
					view3d);
			addMouseMoveListener(entityMouseMoveListener);
			addMouseListener(entityMouseMoveListener);
			addMouseWheelListener(entityMouseMoveListener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.eclipse.swt.widgets.Control)
		 */
		@Override
		public void activate(Control control) {
			moving = true;
			super.activate(control);
			// ((CameraServiceImpl)
			// cameraService).setActiveCamera("lookCamera");
		}
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
