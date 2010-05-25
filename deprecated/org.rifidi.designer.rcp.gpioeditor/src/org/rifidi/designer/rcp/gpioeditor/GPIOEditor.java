/*
 *  GPIOEditor.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor;

import java.util.EventObject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;
import org.rifidi.designer.rcp.gpioeditor.factory.GPIOEditPartFactory;
import org.rifidi.designer.rcp.gpioeditor.parts.GPIORootEditPart;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * GEF editor for connecting GPIO capable entities.
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 12, 2008
 * 
 */
public class GPIOEditor extends GraphicalEditorWithFlyoutPalette implements
		ISaveablePart2 {

	/**
	 * Reference to the finder service.
	 */
	private FinderService finderService;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Variable that makes sure that the editor only gets closed if the whole
	 * app is closing.
	 */
	private boolean closable = false;

	/**
	 * Constructor.
	 */
	public GPIOEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		PlatformUI.getWorkbench().addWorkbenchListener(
				new IWorkbenchListener() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.ui.IWorkbenchListener#postShutdown(org.eclipse
					 * .ui.IWorkbench)
					 */
					@Override
					public void postShutdown(IWorkbench workbench) {
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @see
					 * org.eclipse.ui.IWorkbenchListener#preShutdown(org.eclipse
					 * .ui.IWorkbench, boolean)
					 */
					@Override
					public boolean preShutdown(IWorkbench workbench,
							boolean forced) {
						closable = true;
						return true;
					}

				});
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new GPIOEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, viewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#
	 * initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();

		GPIORootEditPart gpioRoot = new GPIORootEditPart(finderService);
		sceneDataService.getCurrentSceneData().getDefaultGroup()
				.addListChangeListener(gpioRoot);
		getGraphicalViewer().setContents(gpioRoot);
		getEditDomain()
				.getPaletteViewer()
				.getPaletteViewerPreferences()
				.setAutoCollapseSetting(PaletteViewerPreferences.COLLAPSE_NEVER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getPaletteRoot
	 * ()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {

		PaletteRoot palette = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("Tools");

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolGroup.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolGroup.add(new MarqueeToolEntry());

		// Add a (unnamed) separator to the group
		toolGroup.add(new PaletteSeparator());

		// Add (solid-line) connection tool
		tool = new ConnectionCreationToolEntry("Solid connection",
				"Create a solid-line connection", new CreationFactory() {

					public Object getNewObject() {
						return null;
					}

					// see ShapeEditPart#createEditPolicies()
					// this is abused to transmit the desired line style
					public Object getObjectType() {
						return null;
					}
				}, ImageDescriptor.createFromFile(GPIOPlugin.class,
						"icons/connection_s16.gif"), ImageDescriptor
						.createFromFile(GPIOPlugin.class,
								"icons/connection_s24.gif"));
		toolGroup.add(tool);
		palette.add(toolGroup);
		return palette;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util
	 * .EventObject)
	 */
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart2#promptToSaveOnClose()
	 */
	@Override
	public int promptToSaveOnClose() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return !closable;
	}

	/**
	 * @param closable
	 *            the closable to set
	 */
	public void setClosable(boolean closable) {
		this.closable = closable;
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
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
	}
}
