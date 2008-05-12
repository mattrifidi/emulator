/*
 *  SelectionServiceImpl.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.world.RepeatedUpdateAction;
import org.rifidi.designer.services.core.world.WorldService;
import org.rifidi.designer.utils.Helpers;

import com.jme.scene.state.AlphaState;
import com.jme.scene.state.FragmentProgramState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

/**
 * Selection service implementation.
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public class SelectionServiceImpl implements SelectionService,
		SceneDataChangedListener {
	/**
	 * Logger for this class.
	 */
	private static Log logger=LogFactory.getLog(SelectionServiceImpl.class);
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
	 * List of selected entities.
	 */
	private List<Entity> hilited = new ArrayList<Entity>();
	/**
	 * Action submitted to the update thread for updating the highlight state.
	 */
	private HilitedRepeatedUpdateAction repeater;

	/**
	 * List of registered selection listeners.
	 */
	private List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();
	/**
	 * Reference to the camera service.
	 */
	private CameraService cameraService;
	/**
	 * reference to the SceneDataService
	 */
	private SceneDataService sceneDataService;
	/**
	 * reference to the worldservice
	 */
	private WorldService worldService;

	/**
	 * Constructor.
	 */
	public SelectionServiceImpl() {
		logger.debug("SelectionService created");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.selection.SelectionService#hilite(org.rifidi.designer.entities.VisualEntity,
	 *      boolean, boolean)
	 */
	@Override
	public void select(final VisualEntity ent, boolean multiple,
			boolean informlisteners) {
		if (!hilited.contains(ent)) {
			if (!multiple) {
				clearSelection();
			}
			Helpers.waitOnCallabel(new Callable<Object>() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				public Object call() throws Exception {
					ent.hilite(fragmentProgramState, alphaState);
					return null;
				}
			});
			hilited.add(ent);
			if (informlisteners) {
				triggerSelection();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.selection.SelectionService#hilite(java.util.List,
	 *      boolean)
	 */
	@Override
	public void select(final List<VisualEntity> entities,
			boolean informlisteners) {
		// clearSelection();
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
				.enqueue(new Callable<Object>() {
					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.concurrent.Callable#call()
					 */
					public Object call() throws Exception {
						
//						if(entities.size()==1){
//							cameraService.positionCamera(entities.get(0).getNode().getWorldTranslation());
//						}
						for (Entity ent : hilited) {
							((VisualEntity)ent).clearHilite();
						}
						hilited.clear();
						for (VisualEntity entity : entities) {
							entity.hilite(fragmentProgramState, alphaState);
							hilited.add(entity);
						}
						return null;
					}
				});
		if (informlisteners) {
			triggerSelection();
		}
	}

	/**
	 * Clear selection.
	 */
	public void clearSelection() {
		if (hilited.size() > 0) {
			Helpers.waitOnCallabel(new Callable<Object>() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see java.util.concurrent.Callable#call()
				 */
				public Object call() throws Exception {
					for (Entity ent : hilited) {
						if (ent instanceof VisualEntity) {
							((VisualEntity) ent).clearHilite();
						}
					}
					hilited.clear();
					return null;
				}

			});
		}
		triggerSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneData) {

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

		if (repeater == null) {
			repeater = new HilitedRepeatedUpdateAction();
			worldService.addRepeatedUpdateActiom(repeater);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.registry.core.scenedata.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		hilited.clear();
	}

	/**
	 * @return the hilited
	 */
	public List<Entity> getSelectionList() {
		return Collections.unmodifiableList(hilited);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	@Override
	public ISelection getSelection() {
		return new StructuredSelection(hilited);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void setSelection(ISelection selection) {
		// TODO Auto-generated method stub

	}

	/**
	 * Triggers a selection event that contains the currently highlighted
	 * entities.
	 */
	public void triggerSelection() {
		for (ISelectionChangedListener listener : selectionListeners) {
			SelectionChangedEvent event = new SelectionChangedEvent(this,
					new StructuredSelection(hilited));
			listener.selectionChanged(event);
		}
	}

	/**
	 * @param cameraService the cameraService to set
	 */
	public void setCameraService(CameraService cameraService) {
		logger.debug("SelectionService got CameraService");
		this.cameraService = cameraService;
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
	

	/**
	 * @param sceneDataService the sceneDataService to set
	 */
	public void setSceneDataService(SceneDataService sceneDataService) {
		logger.debug("SelectionService got SceneDataService");
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param sceneDataService the sceneDataService to unset
	 */
	public void unsetSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService=null;
	}
	
	/**
	 * @param worldService the worldService to set
	 */
	public void setWorldService(WorldService worldService) {
		logger.debug("SelectionService got WorldService");
		this.worldService = worldService;
	}
	
	/**
	 * @param worldService the worldService to unset
	 */
	public void unsetWorldService(WorldService worldService) {
		this.worldService = null;
	}
}
