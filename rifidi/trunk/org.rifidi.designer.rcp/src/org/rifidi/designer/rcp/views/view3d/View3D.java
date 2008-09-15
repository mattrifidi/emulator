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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.ViewPart;
import org.monklypse.core.JMECanvasImplementor2;
import org.monklypse.core.JMEComposite;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityLibraryReference;
import org.rifidi.designer.library.EntityWizardIface;
import org.rifidi.designer.library.EntityWizardRifidiIface;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.rcp.game.DesignerGame;
import org.rifidi.designer.rcp.views.view3d.listeners.AllAxisMouseMoveEntityListener;
import org.rifidi.designer.rcp.views.view3d.listeners.Editor3DDropTargetListener;
import org.rifidi.designer.rcp.views.view3d.listeners.MouseMoveEntityListener;
import org.rifidi.designer.rcp.views.view3d.listeners.MousePickListener;
import org.rifidi.designer.rcp.views.view3d.listeners.ResizeListener;
import org.rifidi.designer.rcp.views.view3d.listeners.WatchAreaDrawMouseListener;
import org.rifidi.designer.rcp.views.view3d.listeners.ZoomMouseWheelListener;
import org.rifidi.designer.rcp.views.view3d.mode.InteractionMode;
import org.rifidi.designer.services.core.camera.ZoomableLWJGLCamera;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.NewEntityListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.util.TextureManager;

/**
 * This monster baby is the view that displays the 3d scene.
 * 
 * @author Jochen Mader Nov 20, 2007
 * @author Dan West
 */
public class View3D extends ViewPart implements IPerspectiveListener,
		NewEntityListener {
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
	 * The rendering canvas.
	 */
	private GLCanvas glCanvas;

	private JMEComposite jmeComposite;
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

	// miscellany & NEW STUFF
	private Point oldpos;
	/** Reference to the implementor. */
	private DesignerGame designerGame;

	/**
	 * 
	 */
	public View3D() {
		Activator.display = Display.getCurrent();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(final Composite parent) {
		TextureManager.clearCache();

		jmeComposite = new JMEComposite(parent, designerGame);
		glCanvas = designerGame.getCanvas();
		System.out.println(designerGame.getRenderer().getCamera());
		// let glcanvas have focus by default
		glCanvas.forceFocus();

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

		getSite().setSelectionProvider(selectionService);

		modeMap = new HashMap<Mode, InteractionMode>();
		modeMap.put(Mode.PlacementMode, new PlacementMode(this));
		// modeMap.put(Mode.CameraMode, new CameraMode(this));
		modeMap.put(Mode.PickMode, new PickMode(this));
		modeMap.put(Mode.WatchAreaMode, new WatchAreaMode(this));
		modeMap.put(Mode.MoveMode, new MoveMode(this));

		MenuManager menuMgr = new MenuManager();
		menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		getSite().registerContextMenu(menuMgr, selectionService);

		Control control = glCanvas;
		Menu menu = menuMgr.createContextMenu(control);
		control.setMenu(menu);
		getViewSite().getWorkbenchWindow().addPerspectiveListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
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
		// change the window title
		Display.getCurrent().getActiveShell().setText(
				"Rifidi Designer: " + file.getName());
		sceneDataService.loadScene(Display.getCurrent(), file);
		switchMode(Mode.PickMode);
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
			Entity ent = null;
			if (ref.getWizard() != null) {
				RifidiEntityWizard wizard = (RifidiEntityWizard) ref
						.getWizard().newInstance();
				if (wizard instanceof EntityWizardRifidiIface) {
					// TODO: ugly, fix that
					((EntityWizardRifidiIface) wizard)
							.setRMIManager(org.rifidi.designer.entities.Activator
									.getDefault().rifidiManager);
				}
				wizard.setTakenNamesList(entitiesService.getEntityNames());
				WizardDialog dialog = new WizardDialog(getSite().getShell(),
						wizard);

				int returnCode = dialog.open();
				if (returnCode != SWT.CANCEL) {
					if (wizard instanceof EntityWizardRifidiIface) {
						ent = ((EntityWizardRifidiIface) wizard).getEntity();
					} else {
						ent = ((EntityWizardIface) wizard).getEntity();
					}
				}
			} else {
				ent = (Entity) ref.getEntityClass().newInstance();
			}
			entitiesService.addEntity(ent, true, this);

		} catch (InstantiationException e) {
			logger.error("Failed instantiating wizard: \n" + e);
		} catch (IllegalAccessException e) {
			logger.error("Failed instantiating wizard: \n" + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.designer.services.core.entities.NewEntityListener#entityAdded
	 * (org.rifidi.designer.entities.VisualEntity)
	 */
	@Override
	public void entityAdded(VisualEntity ent) {
		selectionService.select((VisualEntity) ent, false, true);
		switchMode(Mode.PlacementMode);
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
	 * Hide the mousepointer in this view.
	 */
	public void hideMousePointer() {
		oldpos = Display.getCurrent().getCursorLocation();
		// designerGame.hideMouse();
	}

	/**
	 * Show the mousepointer in this view.
	 */
	public void showMousePointer() {
		if (oldpos != null) {
			Display.getCurrent().setCursorLocation(oldpos);
		}
		oldpos = null;
		// designerGame.showMouse();
	}

	/**
	 * Toggle the state of the grid between enabled and disabled.
	 */
	public void toggleGrid() {
		// designerGame.toggleGrid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IPerspectiveListener#perspectiveActivated(org.eclipse.
	 * ui.IWorkbenchPage, org.eclipse.ui.IPerspectiveDescriptor)
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
	 * @see
	 * org.eclipse.ui.IPerspectiveListener#perspectiveChanged(org.eclipse.ui
	 * .IWorkbenchPage, org.eclipse.ui.IPerspectiveDescriptor, java.lang.String)
	 */
	@Override
	public void perspectiveChanged(IWorkbenchPage page,
			IPerspectiveDescriptor perspective, String changeId) {
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
			moveListener = new MouseMoveEntityListener(view3d);
			MousePickListener pickListener = new MousePickListener(view3d);
			addMouseMoveListener(moveListener);
			addMouseListener(pickListener);
			addKeyListener(pickListener);
			addMouseWheelListener(new ZoomMouseWheelListener(
					(ZoomableLWJGLCamera) designerGame.getRenderer().getCamera()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate
		 * (org.eclipse.swt.widgets.Control)
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
					view3d);
			addMouseListener(moveListener);
			addMouseMoveListener(moveListener);
			addMouseWheelListener(new ZoomMouseWheelListener(
					(ZoomableLWJGLCamera) designerGame.getRenderer().getCamera()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate
		 * (org.eclipse.swt.widgets.Control)
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
	// org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate(org.
	// eclipse.swt.widgets.Control)
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
			addMouseWheelListener(new ZoomMouseWheelListener(
					(ZoomableLWJGLCamera) designerGame.getRenderer().getCamera()));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate
		 * (org.eclipse.swt.widgets.Control)
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
			AllAxisMouseMoveEntityListener allAxisMouseMoveEntityListener = new AllAxisMouseMoveEntityListener(
					view3d);
			addMouseMoveListener(allAxisMouseMoveEntityListener);
			addMouseListener(allAxisMouseMoveEntityListener);
			addMouseWheelListener(allAxisMouseMoveEntityListener);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.rifidi.designer.rcp.views.view3d.mode.InteractionMode#activate
		 * (org.eclipse.swt.widgets.Control)
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

	/**
	 * @param designerGame
	 *            the designerGame to set
	 */
	@Inject
	public void setDesignerGame(DesignerGame designerGame) {
		this.designerGame = designerGame;
	}
}
