/*
 *  GPIOPerspective.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor.perspectives;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPerspectiveListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.rcp.gpioeditor.GPIOEditor;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Perspective for the GPIO GEF editor
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 12, 2008
 * 
 */
public class GPIOPerspective implements IPerspectiveFactory,
		SceneDataChangedListener, IPerspectiveListener {
	/**
	 * ID of this perspective.
	 */
	public final static String ID = "org.rifidi.designer.rcp.perspectives.gpio";
	/**
	 * Logger for this class.
	 */
	private Log logger = LogFactory.getLog(GPIOPerspective.class);
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Reference to the currently opened editor.
	 */
	private IEditorPart editor = null;
	/**
	 * Is this perspective active.
	 */
	private boolean active = false;

	/**
	 * Constructor.
	 */
	public GPIOPerspective() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(true);
		layout.addStandaloneView(
				"org.rifidi.designer.rcp.views.entityview.EntityView", true,
				IPageLayout.LEFT, 0.25f, layout.getEditorArea());
		layout.addStandaloneView("org.rifidi.designer.rcp.views.MiniMapView", false,
				IPageLayout.BOTTOM, 0.55f,
				"org.rifidi.designer.rcp.views.entityview.EntityView");
		PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.addPerspectiveListener(this);
	}

	/**
	 * @param sceneDataSer
	 *            the sceneDataSer to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataSer) {
		this.sceneDataService = sceneDataSer;
		if (sceneDataService.getCurrentSceneData() != null) {

			try {
				editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().openEditor(new IEditorInput() {

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.ui.IEditorInput#exists()
							 */
							@Override
							public boolean exists() {
								// TODO Auto-generated method stub
								return false;
							}

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
							 */
							@Override
							public ImageDescriptor getImageDescriptor() {
								// TODO Auto-generated method stub
								return null;
							}

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.ui.IEditorInput#getName()
							 */
							@Override
							public String getName() {
								return sceneDataService.getCurrentSceneData()
										.getName();
							}

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.ui.IEditorInput#getPersistable()
							 */
							@Override
							public IPersistableElement getPersistable() {
								// TODO Auto-generated method stub
								return null;
							}

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.ui.IEditorInput#getToolTipText()
							 */
							@Override
							public String getToolTipText() {
								return sceneDataService.getCurrentSceneData()
										.getName();
							}

							/*
							 * (non-Javadoc)
							 * 
							 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
							 */
							@SuppressWarnings("unchecked")
							@Override
							public Object getAdapter(Class adapter) {
								// TODO Auto-generated method stub
								return null;
							}

						}, "org.rifidi.designer.rcp.gpioeditor.editor");
			} catch (PartInitException e) {
				logger.warn(e);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		if (editor != null) {
			((GPIOEditor) editor).setClosable(true);
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().closeEditor(editor, false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {
		try {
			editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().openEditor(new IEditorInput() {

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.ui.IEditorInput#exists()
						 */
						@Override
						public boolean exists() {
							return false;
						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
						 */
						@Override
						public ImageDescriptor getImageDescriptor() {
							return null;
						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.ui.IEditorInput#getName()
						 */
						@Override
						public String getName() {
							return sceneDataService.getCurrentSceneData()
									.getName();
						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.ui.IEditorInput#getPersistable()
						 */
						@Override
						public IPersistableElement getPersistable() {
							return null;
						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.ui.IEditorInput#getToolTipText()
						 */
						@Override
						public String getToolTipText() {
							return sceneDataService.getCurrentSceneData()
									.getName();
						}

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
						 */
						@SuppressWarnings("unchecked")
						@Override
						public Object getAdapter(Class adapter) {
							return null;
						}

					}, "org.rifidi.designer.rcp.gpioeditor.editor");
		} catch (PartInitException e) {
			logger.warn(e);
		}
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
		if (editor != null) {
			destroySceneData(null);
			sceneDataChanged(null);
		}
		if (ID.equals(perspective.getId()) && !active) {
			sceneDataService.addSceneDataChangedListener(this);
			return;
		}
		sceneDataService.removeSceneDataChangedListener(this);
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
}
