/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.service.DuplicateItemException;
import org.rifidi.prototyper.items.service.ItemService;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;

/**
 * The Editor for the prototype
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapViewEditor extends GraphicalEditor implements
		IPropertyChangeListener {

	/** The View ID */
	public static final String ID = "org.rifidi.prototyper.mapeditor";
	/** The Map model to display */
	private MapModel mapModel;
	/** The reader registry service */
	private ReaderRegistryService readerService;
	private ItemService itemService;
	/** The KeyHandler */
	private KeyHandler sharedKeyHandler;

	/**
	 * Constructor
	 */
	public MapViewEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		// create the root edit part
		MapScalableRootEditPart rootEditPart = new MapScalableRootEditPart();
		getGraphicalViewer().setRootEditPart(rootEditPart);
		getGraphicalViewer().setEditPartFactory(new MapEditPartFactory());
		getGraphicalViewer().setKeyHandler(
				new GraphicalViewerKeyHandler(getGraphicalViewer())
						.setParent(getCommonKeyHandler()));

		IAction showGrid = new ToggleGridAction(getGraphicalViewer());
		showGrid.addPropertyChangeListener(this);
		getActionRegistry().registerAction(showGrid);

		// Configure the ZoomManager
		ZoomManager zoomManager = (ZoomManager) getAdapter(ZoomManager.class);

		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		zoomManager.setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(zoomManager);
		IAction zoomOut = new ZoomOutAction(zoomManager);
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);

		// configure the Mousewheel
		getGraphicalViewer().setProperty(
				MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1),
				MouseWheelZoomHandler.SINGLETON);

		ContextMenuProvider provider = new MapContextMenu(getGraphicalViewer(),
				getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
		getSite().registerContextMenu("org.rifidi.prototyper.contextmenu", //$NON-NLS-1$
				provider, getGraphicalViewer());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(
					ZoomManager.class.toString());

		return super.getAdapter(type);
	}

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and
	 * Graphical Views. For example, delete is a common action.
	 */
	protected KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
		}
		return sharedKeyHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
		getGraphicalViewer().setContents(mapModel);
		getGraphicalViewer().addDropTargetListener(
				new TextTransferDropTargetListener(getGraphicalViewer()));
		getGraphicalViewer()
				.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING,
				new Dimension(30, 30));
		setEditMode(true);
		setPartName(mapModel.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
	 * IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO: put in Saferunner
		try {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			save(file, false, monitor);
			getCommandStack().markSaveLocation();
		} catch (Exception e) {

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.ui.parts.GraphicalEditor#commandStackChanged(java.util
	 * .EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		SaveAsDialog dialog = new SaveAsDialog(getSite().getWorkbenchWindow()
				.getShell());
		dialog.setOriginalFile(((IFileEditorInput) getEditorInput()).getFile());
		dialog.open();
		IPath path = dialog.getResult();
		if (path == null)
			return;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IFile file = workspace.getRoot().getFile(path);

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			public void execute(final IProgressMonitor monitor) {
				try {

					if (file.exists()) {
						file.delete(true, null);
					}
					save(file, true, null);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		try {
			new ProgressMonitorDialog(getSite().getWorkbenchWindow().getShell())
					.run(false, true, op);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// superSetInput(new FileEditorInput(file));
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * Private helper method to do the work of saving a file
	 * 
	 * @param file
	 * @param create
	 * @param monitor
	 * @throws IOException
	 * @throws CoreException
	 */
	private void save(IFile file, boolean create, IProgressMonitor monitor)
			throws IOException, CoreException {
		mapModel.setAllReaders(getAllReaders());
		mapModel.setAllItems(itemService.getItems());
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(mapModel);
		out.close();

		if (create) {
			file.create(new ByteArrayInputStream(byteOut.toByteArray()), true,
					monitor);
		} else {
			file.setContents(new ByteArrayInputStream(byteOut.toByteArray()),
					true, false, monitor);
		}

		byteOut.close();

	}

	/**
	 * Private helper method to get all teh avaialble readers
	 * 
	 * @return
	 */
	private Collection<GeneralReaderPropertyHolder> getAllReaders() {
		List<GeneralReaderPropertyHolder> readers = new LinkedList<GeneralReaderPropertyHolder>();
		for (UIReader reader : readerService.getReaderList()) {
			readers.add(reader.getGeneralReaderPropertyHolder());
		}
		return readers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			try {
				InputStream is = file.getContents(false);
				ObjectInputStream ois = new ObjectInputStream(is);
				this.mapModel = (MapModel) ois.readObject();
				ois.close();
			} catch (Exception e) {
				e.printStackTrace();
				this.mapModel = new MapModel();
			}

		} else {
			this.mapModel = new MapModel();
			this.mapModel.setName("Undefined Map Model");
		}
		readerService.clean();
		for (GeneralReaderPropertyHolder grph : mapModel.getAllReaders()) {
			try {
				readerService.create(grph);
			} catch (DuplicateReaderException e) {
			}
		}
		itemService.clear();
		for (ItemModel item : mapModel.getAllItems()) {
			try {
				itemService.addItem(item);
			} catch (DuplicateItemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Chnage the state of the edit mode
	 * 
	 * @param editMode
	 */
	public void setEditMode(boolean editMode) {

		if (getGraphicalViewer() != null) {
			((MapScalableRootEditPart) getGraphicalViewer().getRootEditPart())
					.setEditMode(editMode);
			getSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
		}
	}

	/**
	 * Called in order to inject the ReaderRegistryService
	 * 
	 * @param service
	 */
	@Inject
	public void setReaderService(ReaderRegistryService service) {
		this.readerService = service;
	}

	/**
	 * Called in order to inject the ItemService
	 * 
	 * @param service
	 */
	@Inject
	public void setItemService(ItemService service) {
		this.itemService = service;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.util.IPropertyChangeListener#propertyChange(org.eclipse
	 * .jface.util.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (event.getSource().equals(
				getActionRegistry().getAction(
						GEFActionConstants.TOGGLE_GRID_VISIBILITY))) {
			Boolean mode = (Boolean) event.getNewValue();
			setEditMode(mode);

		}

	}

}
