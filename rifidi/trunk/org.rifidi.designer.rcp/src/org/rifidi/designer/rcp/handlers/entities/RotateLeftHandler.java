/*
 *  NewSceneDataHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.entities;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for rotating entities that are currently selected in the selection
 * provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class RotateLeftHandler extends AbstractHandler {
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 */
	public RotateLeftHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// get the selected entity and rotate it

		List<Entity> selection = selectionService.getSelectionList();
		if (!View3D.moving) {
			for (Object obj : selection.toArray()) {
				VisualEntity entity = (VisualEntity) obj;
				entity.rotateLeft(); // if no potential collisions
			}
			return null;
		}
		for (Object obj : selection.toArray()) {
			VisualEntity entity = (VisualEntity) obj;
			entity.rotateLeft();
		}
		return null;
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
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
}
